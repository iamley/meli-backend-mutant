package com.meli.service.backend.mutant.adapter.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.meli.service.backend.mutant.adapter.repository.dto.MutantDTO;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.meli.service.backend.mutant.enums.MLStatus.DATABASE_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamoDBRepositoryTest {

    @InjectMocks
    private DynamoDBRepository dynamoDBRepository;

    public List<String> listItems;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        listItems = new ArrayList<>();
        listItems.add("ATGCGA");
        listItems.add("CTGTGC");
        listItems.add("TTATGT");
        listItems.add("AGAAGG");
        listItems.add("ACCCTA");
        listItems.add("TCACTG");
    }

    @Test
    public void initializingClientNull() {

        Mockito.mock(AmazonDynamoDBClient.class);
        Table mockTable = Mockito.mock(Table.class);

        MutantDTO mutantDTO = new MutantDTO();
        mutantDTO.setIsMutant(true);
        mutantDTO.setId(UUID.randomUUID().toString());
        mutantDTO.setDna(listItems);

        when(mockTable.getItem((GetItemSpec) any())).thenReturn(responseDynamo());

        var result = assertThrows(BusinessCapabilityException.class,
                () -> dynamoDBRepository.saveItem("tableName",  mutantDTO));

        assertEquals(DATABASE_ERROR.getCode(), result.getErrorCodeBusiness());
        Assert.assertNotNull(result);
    }

    private Item responseDynamo() {

        Item mockItem = new Item()
                .withString("id", "8445f7c2-72bc-4e17-af1f-e7a2d2d243d7")
                .withList("dna", listItems)
                .withBoolean("mutant", true);

        return mockItem;
    }

}