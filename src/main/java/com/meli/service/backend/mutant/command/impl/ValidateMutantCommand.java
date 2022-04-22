package com.meli.service.backend.mutant.command.impl;

import com.meli.service.backend.mutant.command.MeLiCommand;
import com.meli.service.backend.mutant.controller.dto.StatusDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantOutputDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantResponseDTO;
import com.meli.service.backend.mutant.entities.MeLiResponseEntity;
import com.meli.service.backend.mutant.enums.LogLevel;
import com.meli.service.backend.mutant.enums.MLStatus;
import com.meli.service.backend.mutant.enums.ServerErrorCommon;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ValidateMutantLogic;
import com.meli.service.backend.mutant.model.LoggerDataDTO;
import com.meli.service.backend.mutant.utils.MeLiStatusResponseUtil;
import com.meli.service.backend.mutant.utils.UtilityLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;

@Slf4j
@Component("ValidateMutantCommand")
public class ValidateMutantCommand implements MeLiCommand<ValidateMutantDTO, ValidateMutantResponseDTO> {

    private static final String COMMAND_NAME = "ValidateMutantCommand";

    @Autowired
    private ValidateMutantLogic validateMutantLogic;

    @Autowired
    private MeLiStatusResponseUtil responseUtil;

    @Autowired
    private UtilityLog loggerUtil;

    @Override
    @CircuitBreaker(name = COMMAND_NAME, fallbackMethod = "executeFallback")
    public MeLiResponseEntity<ValidateMutantResponseDTO> execute(ValidateMutantDTO request) throws BusinessCapabilityException {

        var response = new ValidateMutantResponseDTO();
        var status = new StatusDTO();
        ValidateMutantOutputDTO validateMutantOutputDTO = null;

        try {
            validateMutantOutputDTO = new ValidateMutantOutputDTO();
            validateMutantOutputDTO.setIsMutant(validateMutantLogic.invoke(request));
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

        response.setBody(validateMutantOutputDTO);
        response.setStatus(status);

        return new MeLiResponseEntity<>(response);
    }

    @Override
    public MeLiResponseEntity<ValidateMutantResponseDTO> executeFallback(ValidateMutantDTO request, Throwable throwable) throws BusinessCapabilityException {

        final var dataLog = new LoggerDataDTO();
        dataLog.setMessage(throwable.toString());
        dataLog.setCode(ServerErrorCommon.FALLBACK.getCode());
        dataLog.setCodeMessage(ServerErrorCommon.FALLBACK.getMessage());
        dataLog.setLevel(LogLevel.ERROR);
        loggerUtil.put(log, dataLog);


        final var response = new MeLiResponseEntity<ValidateMutantResponseDTO>();
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