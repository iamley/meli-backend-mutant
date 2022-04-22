package com.meli.service.backend.mutant.command.impl;

import com.meli.service.backend.mutant.command.MeLiUniqueCommand;
import com.meli.service.backend.mutant.controller.dto.StatusDTO;
import com.meli.service.backend.mutant.controller.dto.StatusMutantOutputDTO;
import com.meli.service.backend.mutant.controller.dto.StatusMutantResponseDTO;
import com.meli.service.backend.mutant.entities.MeLiResponseEntity;
import com.meli.service.backend.mutant.enums.LogLevel;
import com.meli.service.backend.mutant.enums.MLStatus;
import com.meli.service.backend.mutant.enums.ServerErrorCommon;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ResultsStatisticsLogic;
import com.meli.service.backend.mutant.model.LoggerDataDTO;
import com.meli.service.backend.mutant.utils.MeLiStatusResponseUtil;
import com.meli.service.backend.mutant.utils.UtilityLog;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;

@Slf4j
@Component("ResultsStatisticsCommand")
public class ResultsStatisticsCommand implements MeLiUniqueCommand<StatusMutantResponseDTO> {

    private static final String COMMAND_NAME = "ValidateMutantCommand";

    @Autowired
    private ResultsStatisticsLogic resultsStatisticsLogic;

    @Autowired
    private MeLiStatusResponseUtil responseUtil;

    @Autowired
    private UtilityLog loggerUtil;

    @Override
    @CircuitBreaker(name = COMMAND_NAME, fallbackMethod = "executeFallback")
    public MeLiResponseEntity<StatusMutantResponseDTO> execute() throws BusinessCapabilityException {

        var response = new StatusMutantResponseDTO();
        var status = new StatusDTO();
        StatusMutantOutputDTO statusMutantOutputDTO = null;

        try {
            statusMutantOutputDTO = new StatusMutantOutputDTO();

            var countOfMutant = resultsStatisticsLogic.amountOfMutantOrHuman(true);
            var countOfHuman = resultsStatisticsLogic.amountOfMutantOrHuman(false);
            var ratio = resultsStatisticsLogic.averageMutants(countOfMutant, countOfHuman);

            statusMutantOutputDTO.setCountMutantDna(BigDecimal.valueOf(countOfMutant));
            statusMutantOutputDTO.setCountHumanDna(BigDecimal.valueOf(countOfHuman));
            statusMutantOutputDTO.setRatio(BigDecimal.valueOf(ratio));

            status.setCode(SUCCEED.getCode());
            status.setDescription(SUCCEED.getDescription());
        } catch (BusinessCapabilityException e) {
            writeLog(e);
            status.code(e.getErrorCodeBusiness());
            status.description(e.getMessage());
        } catch (Exception e) {
            writeLog(e);
            status.setCode(MLStatus.FATAL_ERROR.getCode());
            status.setDescription(MLStatus.FATAL_ERROR.getDescription());
        }

        response.setBody(statusMutantOutputDTO);
        response.setStatus(status);

        return new MeLiResponseEntity<>(response);
    }

    @Override
    public MeLiResponseEntity<StatusMutantResponseDTO> executeFallback(Throwable throwable) throws BusinessCapabilityException {

        final var dataLog = new LoggerDataDTO();
        dataLog.setMessage(throwable.toString());
        dataLog.setCode(ServerErrorCommon.FALLBACK.getCode());
        dataLog.setCodeMessage(ServerErrorCommon.FALLBACK.getMessage());
        dataLog.setLevel(LogLevel.ERROR);
        loggerUtil.put(log, dataLog);


        final var response = new MeLiResponseEntity<StatusMutantResponseDTO>();
        response.setStatus(responseUtil.getStatusResponse(ServerErrorCommon.FALLBACK, throwable));

        return response;
    }

    private void writeLog(Exception e) {
        final var dataLog = new LoggerDataDTO();
        dataLog.setMessage(e.toString());
        dataLog.setCode(MLStatus.FATAL_ERROR.getCode());
        dataLog.setCodeMessage(MLStatus.FATAL_ERROR.getDescription());
        dataLog.setLevel(LogLevel.ERROR);
        loggerUtil.put(log, dataLog);
    }

}