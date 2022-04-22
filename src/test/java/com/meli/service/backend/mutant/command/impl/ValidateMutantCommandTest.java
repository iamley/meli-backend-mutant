package com.meli.service.backend.mutant.command.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.service.backend.mutant.Application;
import com.meli.service.backend.mutant.adapter.repository.DynamoDBRepository;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantInputDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantOutputDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantResponseDTO;
import com.meli.service.backend.mutant.entities.MeLiResponseEntity;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ValidateMutantLogic;
import com.meli.service.backend.mutant.model.StatusDataDTO;
import com.meli.service.backend.mutant.utils.MeLiStatusResponseUtil;
import com.meli.service.backend.mutant.utils.UtilityLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static com.meli.service.backend.mutant.enums.MLStatus.BAD_REQUEST;
import static com.meli.service.backend.mutant.enums.MLStatus.FATAL_ERROR;
import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class, properties = { "spring.main.allow-bean-definition-overriding=true" })
class ValidateMutantCommandTest {

    @SpyBean
    private ValidateMutantCommand validateMutantCommand;

    @MockBean
    private ValidateMutantLogic validateMutantLogic;

    @MockBean
    private DynamoDBRepository dynamoDBRepository;

    @MockBean
    private AmazonDynamoDBClient amazonDynamoDBClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<ValidateMutantDTO> messageCaptor;

    @Captor
    private ArgumentCaptor<Throwable> exceptionCaptor;

    @Autowired
    private UtilityLog loggerUtil;

    @Autowired
    private MeLiStatusResponseUtil responseUtil;

    private ValidateMutantDTO command;

    private ValidateMutantResponseDTO commandResponse;

    private MeLiResponseEntity<ValidateMutantResponseDTO> response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        command = new ValidateMutantDTO();
        command.setBody(getBody());

        commandResponse = new ValidateMutantResponseDTO();
        commandResponse.setBody(getBodyResponse());

        response = new MeLiResponseEntity<>();
        response.setBody(commandResponse);
        response.setStatus(StatusDataDTO.builder()
                .code(SUCCEED.getCode())
                .description(SUCCEED.getDescription())
                .build());
    }

    @AfterEach
    void tearDown() {
        command = null;
    }

    @Test
    void handleComplete() {

        Mockito.when(validateMutantLogic.invoke(Mockito.any(ValidateMutantDTO.class))).thenReturn(true);

        final var response = validateMutantCommand.execute(command);

        assertNotNull(response);
        assertEquals(SUCCEED.getCode(), response.getStatus().getCode());
    }

    @Test
    void handleCompleteWithException() {

        Mockito.when(validateMutantLogic.invoke(Mockito.any(ValidateMutantDTO.class))).thenThrow(RuntimeException.class);

        final var response = validateMutantCommand.execute(command);

        assertNotNull(response);
        assertEquals(FATAL_ERROR.getCode(), response.getStatus().getCode());
    }

    @Test
    void fallbackExecutionTest() {
        when((validateMutantLogic).invoke(Mockito.any(ValidateMutantDTO.class))).thenThrow(new BusinessCapabilityException());

        validateMutantCommand.executeFallback(new ValidateMutantDTO(), new BusinessCapabilityException());

        verify(validateMutantCommand).executeFallback(
                messageCaptor.capture(),
                exceptionCaptor.capture()
        );

        assertNotNull(messageCaptor.getValue());
        assertNotNull(exceptionCaptor.getValue());

    }

    private static ValidateMutantInputDTO getBody() {

        List<String> listItems = new ArrayList<>();
        listItems.add("ATGCGA");
        listItems.add("CTGTGC");
        listItems.add("TTATGT");
        listItems.add("AGAAGG");
        listItems.add("ACCCTA");
        listItems.add("TCACTG");

        ValidateMutantInputDTO body = new ValidateMutantInputDTO();
        body.setDna(listItems);

        return body;
    }

    private static ValidateMutantOutputDTO getBodyResponse() {

        ValidateMutantOutputDTO response = new ValidateMutantOutputDTO();
        response.setIsMutant(true);

        return response;
    }

}