package com.meli.service.backend.mutant.command.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.service.backend.mutant.Application;
import com.meli.service.backend.mutant.adapter.repository.DynamoDBRepository;
import com.meli.service.backend.mutant.controller.dto.StatusMutantOutputDTO;
import com.meli.service.backend.mutant.controller.dto.StatusMutantResponseDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.entities.MeLiResponseEntity;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ResultsStatisticsLogic;
import com.meli.service.backend.mutant.model.StatusDataDTO;
import com.meli.service.backend.mutant.utils.MeLiStatusResponseUtil;
import com.meli.service.backend.mutant.utils.UtilityLog;
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

import java.math.BigDecimal;

import static com.meli.service.backend.mutant.enums.MLStatus.FATAL_ERROR;
import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = Application.class, properties = { "spring.main.allow-bean-definition-overriding=true" })
class ResultsStatisticsCommandTest {

    @SpyBean
    private ResultsStatisticsCommand resultsStatisticsCommand;

    @MockBean
    private ResultsStatisticsLogic resultsStatisticsLogic;

    @MockBean
    private DynamoDBRepository dynamoDBRepository;

    @MockBean
    private AmazonDynamoDBClient amazonDynamoDBClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<StatusMutantResponseDTO> messageCaptor;

    @Captor
    private ArgumentCaptor<Throwable> exceptionCaptor;

    @Autowired
    private UtilityLog loggerUtil;

    @Autowired
    private MeLiStatusResponseUtil responseUtil;

    private StatusMutantResponseDTO commandResponse;

    private MeLiResponseEntity<StatusMutantResponseDTO> response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        commandResponse = new StatusMutantResponseDTO();
        commandResponse.setBody(getBodyResponse());

        response = new MeLiResponseEntity<>();
        response.setBody(commandResponse);
        response.setStatus(StatusDataDTO.builder()
                .code(SUCCEED.getCode())
                .description(SUCCEED.getDescription())
                .build());
    }

    @Test
    void handleComplete() {

        Mockito.when(resultsStatisticsLogic.amountOfMutantOrHuman(Mockito.anyBoolean())).thenReturn(100);
        Mockito.when(resultsStatisticsLogic.averageMutants(Mockito.anyInt(), Mockito.anyInt())).thenReturn(50F);

        final var response = resultsStatisticsCommand.execute();

        assertNotNull(response);
        assertEquals(SUCCEED.getCode(), response.getStatus().getCode());
    }

    @Test
    void handleCompleteWithException() {

        Mockito.when(resultsStatisticsLogic.amountOfMutantOrHuman(Mockito.anyBoolean())).thenReturn(100);
        Mockito.when(resultsStatisticsLogic.averageMutants(Mockito.anyInt(), Mockito.anyInt())).thenThrow(RuntimeException.class);

        final var response = resultsStatisticsCommand.execute();

        assertNotNull(response);
        assertEquals(FATAL_ERROR.getCode(), response.getStatus().getCode());
    }

    @Test
    void fallbackExecutionTest() {
        Mockito.when(resultsStatisticsLogic.amountOfMutantOrHuman(Mockito.anyBoolean())).thenThrow(new BusinessCapabilityException());
        Mockito.when(resultsStatisticsLogic.averageMutants(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new BusinessCapabilityException());

        resultsStatisticsCommand.executeFallback(new BusinessCapabilityException());

        verify(resultsStatisticsCommand).executeFallback(
                exceptionCaptor.capture()
        );

        assertNotNull(exceptionCaptor.getValue());

    }

    private static StatusMutantOutputDTO getBodyResponse() {

        StatusMutantOutputDTO response = new StatusMutantOutputDTO();
        response.setRatio(BigDecimal.valueOf(2));
        response.setCountHumanDna(BigDecimal.valueOf(40));
        response.setCountMutantDna(BigDecimal.valueOf(80));

        return response;
    }

}