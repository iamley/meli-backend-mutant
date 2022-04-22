package com.meli.service.backend.mutant.controller;

import com.meli.service.backend.mutant.command.impl.ResultsStatisticsCommand;
import com.meli.service.backend.mutant.command.impl.ValidateMutantCommand;
import com.meli.service.backend.mutant.controller.api.MutantApi;
import com.meli.service.backend.mutant.controller.dto.StatusMutantResponseDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.meli.service.backend.mutant.enums.MLStatus.BAD_REQUEST;
import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;

@RestController
public class MutantController implements MutantApi {

    @Autowired
    private ValidateMutantCommand validateMutantCommand;

    @Autowired
    private ResultsStatisticsCommand resultsStatisticsCommand;

    @Override
    public ResponseEntity<ValidateMutantResponseDTO> validateMutant(@Valid ValidateMutantDTO body) {

        var response= validateMutantCommand.execute(body);

        if (response.getBody().getStatus().getCode().equals(SUCCEED.getCode())) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }

        if (response.getBody().getStatus().getCode().equals(BAD_REQUEST.getCode())) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<StatusMutantResponseDTO> statusMutants() {

        var response = resultsStatisticsCommand.execute();

        if (response.getBody().getStatus().getCode().equals(SUCCEED.getCode())) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        }

        if (response.getBody().getStatus().getCode().equals(BAD_REQUEST.getCode())) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}