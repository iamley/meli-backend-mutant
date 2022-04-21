package com.meli.service.backend.mutant.controller;

import com.meli.service.backend.mutant.controller.api.MutantApi;
import com.meli.service.backend.mutant.controller.dto.StatusDTO;
import com.meli.service.backend.mutant.controller.dto.StatusMutantResponseDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantResponseDTO;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ResultsStatisticsLogic;
import com.meli.service.backend.mutant.logic.ValidateMutantLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.meli.service.backend.mutant.enums.MLStatus.FATAL_ERROR;
import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;

@RestController
public class MutantController implements MutantApi {

    @Autowired
    private ValidateMutantLogic validateMutantLogic;

    @Autowired
    private ResultsStatisticsLogic resultsStatisticsLogic;

    @Override
    public ResponseEntity<ValidateMutantResponseDTO> validateMutant(@Valid ValidateMutantDTO body) {

        ResponseEntity<ValidateMutantResponseDTO> response;

        try {
            final var resp= validateMutantLogic.invoke(body);
            final var reply = new ValidateMutantResponseDTO();
            final var status = new StatusDTO();
            status.setCode(SUCCEED.getCode());
            status.setDescription(SUCCEED.getDescription());
            reply.setBody(resp);
            reply.setStatus(status);

            response = new ResponseEntity<>(reply, HttpStatus.OK);

        } catch (BusinessCapabilityException be) {
            final var fatalError = new ValidateMutantResponseDTO();
            final var errorStatus = new StatusDTO();
            errorStatus.setCode(be.getErrorCodeBusiness());
            errorStatus.setDescription(be.getMessage());
            fatalError.setStatus(errorStatus);

            response = new ResponseEntity<>(fatalError, HttpStatus.BAD_REQUEST);
        } catch (Exception e) { ;
            final var fatalError = new ValidateMutantResponseDTO();
            final var errorStatus = new StatusDTO();
            errorStatus.setCode(FATAL_ERROR.getCode());
            errorStatus.setDescription(FATAL_ERROR.getDescription());
            fatalError.setStatus(errorStatus);

            response = new ResponseEntity<>(fatalError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public ResponseEntity<StatusMutantResponseDTO> statusMutants() {

        ResponseEntity<StatusMutantResponseDTO> response;

        try {
            final var resp= resultsStatisticsLogic.invoke();
            final var reply = new StatusMutantResponseDTO();
            final var status = new StatusDTO();
            status.setCode(FATAL_ERROR.getCode());
            status.setDescription(FATAL_ERROR.getDescription());
            reply.setBody(resp);
            reply.setStatus(status);

            response = new ResponseEntity<>(reply, HttpStatus.OK);

        } catch (BusinessCapabilityException be) {
            final var fatalError = new StatusMutantResponseDTO();
            final var errorStatus = new StatusDTO();
            errorStatus.setCode(be.getErrorCodeBusiness());
            errorStatus.setDescription(be.getMessage());
            fatalError.setStatus(errorStatus);

            response = new ResponseEntity<>(fatalError, HttpStatus.BAD_REQUEST);
        } catch (Exception e) { ;
            final var fatalError = new StatusMutantResponseDTO();
            final var errorStatus = new StatusDTO();
            errorStatus.setCode(FATAL_ERROR.getCode());
            errorStatus.setDescription(FATAL_ERROR.getDescription());
            fatalError.setStatus(errorStatus);

            response = new ResponseEntity<>(fatalError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}