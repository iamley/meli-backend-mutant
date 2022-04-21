package com.meli.service.backend.mutant.logic.impl;

import com.meli.service.backend.mutant.adapter.repository.DynamoDBRepository;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantInputDTO;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.meli.service.backend.mutant.enums.MLStatus.BAD_REQUEST;
import static com.meli.service.backend.mutant.enums.MLStatus.EMPTY_BODY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ValidateMutantLogicImplTest {

    @InjectMocks
    ValidateMutantLogicImpl implementation;

    @Mock
    private DynamoDBRepository dynamoDBRepository;

    private ValidateMutantDTO request;

    @BeforeEach
    public void setUp() throws Exception {
        request = new ValidateMutantDTO();
        request.setBody(getBody());
    }

    @Test
    void successMutantValidation() {

        var result = implementation.invoke(request);
        assertNotNull(result);
        assertEquals(true, result.isIsMutant());
    }

    @Test
    void successfulNonMutantValidation() {

        List<String> listItems = new ArrayList<>();
        listItems.add("ATGCAA");
        listItems.add("CAGTGC");
        listItems.add("TTATCT");
        listItems.add("AGAAGG");
        listItems.add("CTCCTA");
        listItems.add("TCACTG");

        request.getBody().setDna(listItems);

        var result = implementation.invoke(request);
        assertNotNull(result);
        assertEquals(false, result.isIsMutant());
    }

    @Test
    void successfulProfileValidation() {

        List<String> listItems = new ArrayList<>();
        listItems.add("ATLCAA");
        listItems.add("CAGTGC");
        listItems.add("TTATCT");
        listItems.add("AGAMGG");
        listItems.add("CTCCTA");
        listItems.add("TCXCTG");

        request.getBody().setDna(listItems);

        var result = assertThrows(BusinessCapabilityException.class,
                () -> implementation.invoke(request));

        assertEquals(EMPTY_BODY.getCode(), result.getErrorCodeBusiness());
    }

    @Test
    void successfulProfileNullValidation() {

        List<String> listItems = new ArrayList<>();
        listItems.add("ATA");
        listItems.add("CAG");
        listItems.add("TTA");

        request.getBody().setDna(listItems);

        var result = assertThrows(BusinessCapabilityException.class,
                () -> implementation.invoke(request));

        assertEquals(BAD_REQUEST.getCode(), result.getErrorCodeBusiness());
    }

    @Test
    void successfulProfileCountValidation() {

        request.getBody().setDna(null);

        var result = assertThrows(BusinessCapabilityException.class,
                () -> implementation.invoke(request));

        assertEquals(BAD_REQUEST.getCode(), result.getErrorCodeBusiness());
    }

    private static ValidateMutantInputDTO getBody() {

        List<String> listItems = new ArrayList<>();
        listItems.add("ATGCGA");
        listItems.add("CAGTGC");
        listItems.add("TTATGT");
        listItems.add("AGAAGG");
        listItems.add("CCCCTA");
        listItems.add("TCACTG");

        ValidateMutantInputDTO body = new ValidateMutantInputDTO();
        body.setDna(listItems);

        return body;
    }
}
