package com.meli.service.backend.mutant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.service.backend.mutant.Application;
import com.meli.service.backend.mutant.command.impl.ResultsStatisticsCommand;
import com.meli.service.backend.mutant.command.impl.ValidateMutantCommand;
import com.meli.service.backend.mutant.controller.dto.StatusMutantResponseDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantInputDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantResponseDTO;
import com.meli.service.backend.mutant.logic.ValidateMutantLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.meli.service.backend.mutant.enums.MLStatus.SUCCEED;
import static com.meli.service.backend.mutant.enums.MLStatus.BAD_REQUEST;

@SpringBootTest(classes = Application.class, properties = { "spring.main.allow-bean-definition-overriding=true" })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private MutantController controller;

    @Mock
    private ValidateMutantCommand validateMutantCommand;

    @Mock
    private ResultsStatisticsCommand resultsStatisticsCommand;

    private ValidateMutantDTO request;

    @BeforeEach
    public void setUp() throws Exception {
        request = new ValidateMutantDTO();
        request.setBody(getBody());
    }

    @Test
    void validateMutantSuccess() throws Exception {

        MvcResult mvcResult = mockMvc
                .perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();

        var reply = objectMapper.readValue(resultContent, ValidateMutantResponseDTO.class);
        assertEquals(SUCCEED.getCode(), reply.getStatus().getCode());
    }

    @Test
    void validateMutantErrorListItem() throws Exception {

        List<String> listItems = new ArrayList<>();
        listItems.add("ATGC");
        listItems.add("CTG");
        listItems.add("TTA");

        request.getBody().setDna(listItems);

        MvcResult mvcResult = mockMvc
                .perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();

        var reply = objectMapper.readValue(resultContent, ValidateMutantResponseDTO.class);
        assertEquals(BAD_REQUEST.getCode(), reply.getStatus().getCode());
    }

    @Test
    void validateMutantErrorNullRequest() throws Exception {

        request.getBody().setDna(null);

        MvcResult mvcResult = mockMvc
                .perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();

        var reply = objectMapper.readValue(resultContent, ValidateMutantResponseDTO.class);
        assertEquals(BAD_REQUEST.getCode(), reply.getStatus().getCode());
    }

    @Test
    void validateMutantBadRequest() throws Exception {

        List<String> listItems = new ArrayList<>();
        listItems.add("MTG");
        listItems.add("XLG");
        listItems.add("TPA");

        request.getBody().setDna(listItems);

        MvcResult mvcResult = mockMvc
                .perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();

        var reply = objectMapper.readValue(resultContent, ValidateMutantResponseDTO.class);
        assertEquals(BAD_REQUEST.getCode(), reply.getStatus().getCode());
    }

    @Test
    void resultsStatisticsSuccess() throws Exception {

        MvcResult mvcResult = mockMvc
                .perform(get("/mutant/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();

        var reply = objectMapper.readValue(resultContent, StatusMutantResponseDTO.class);
        assertEquals(SUCCEED.getCode(), reply.getStatus().getCode());
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
}