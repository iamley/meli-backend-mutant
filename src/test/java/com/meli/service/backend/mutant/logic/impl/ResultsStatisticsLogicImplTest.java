package com.meli.service.backend.mutant.logic.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.meli.service.backend.mutant.adapter.repository.DynamoDBRepository;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static com.meli.service.backend.mutant.enums.MLStatus.DATABASE_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultsStatisticsLogicImplTest {

    @InjectMocks
    ResultsStatisticsLogicImpl implementation;

    @Mock
    private DynamoDBRepository dynamoDBRepository;

    @Mock
    private AmazonDynamoDBClient amazonDynamoDBClient;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(implementation, "tableName", "mutants");
    }

    @Test
    void successAmountOfMutantOrHuman() {

        when(dynamoDBRepository.scanCountItems(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(3);

        var result =  implementation.amountOfMutantOrHuman(true);

        assertNotNull(result);
        assertEquals(3, result);
    }

    @Test
    void errorAmountOfMutantOrHuman() {

        when(dynamoDBRepository.scanCountItems(Mockito.anyString(), Mockito.anyBoolean())).thenThrow(RuntimeException.class);

        var result = assertThrows(BusinessCapabilityException.class,
                () -> implementation.amountOfMutantOrHuman(true));

        assertNotNull(result);
        assertEquals(DATABASE_ERROR.getCode(), result.getErrorCodeBusiness());
    }

    @Test
    void successAverageMutants() {

        var result = implementation.averageMutants(10, 20);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0), result);
    }

    @Test
    void successValueAverageMutants() {

        var result = implementation.averageMutants(40, 0);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0), result);
    }

}