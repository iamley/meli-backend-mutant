package com.meli.service.backend.mutant.logic.impl;

import com.meli.service.backend.mutant.adapter.repository.DynamoDBRepository;
import com.meli.service.backend.mutant.adapter.repository.dto.MutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantDTO;
import com.meli.service.backend.mutant.controller.dto.ValidateMutantInputDTO;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;
import com.meli.service.backend.mutant.logic.ValidateMutantLogic;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.meli.service.backend.mutant.enums.MLStatus.BAD_REQUEST;
import static com.meli.service.backend.mutant.enums.MLStatus.EMPTY_BODY;
import static com.meli.service.backend.mutant.enums.MLStatus.FATAL_ERROR;

@Slf4j
@Component("ValidateMutantLogicImpl")
public class ValidateMutantLogicImpl implements ValidateMutantLogic {

    private static Logger LOGGER = LoggerFactory.getLogger(ValidateMutantLogicImpl.class);

    private static final String LABEL_ERROR = "Error generate {}";

    @Value("${ml.ws.aws.table}")
    private String tableName;

    @Autowired
    private DynamoDBRepository dynamoDBRepository;

    @Override
    public Boolean invoke(ValidateMutantDTO dna) {

        try {
            validateProfile(dna);
            LOGGER.info("Resquest send to service {}", dna);

            boolean reply = validateMutant(dna.getBody());

            LOGGER.info("Response send to service {}", reply);

            MutantDTO mutantDTO = new MutantDTO();
            mutantDTO.setId(UUID.randomUUID().toString());
            mutantDTO.setIsMutant(reply);
            mutantDTO.setDna(dna.getBody().getDna());

            dynamoDBRepository.saveItem(tableName, mutantDTO);
            return reply;
        } catch (BusinessCapabilityException e) {
            LOGGER.error(LABEL_ERROR, e);
            throw new BusinessCapabilityException(e.getErrorCodeBusiness(), e.getErrorMessage());
        } catch (Exception e) {
            LOGGER.error(LABEL_ERROR, e);
            throw new BusinessCapabilityException(FATAL_ERROR.getCode(), FATAL_ERROR.getDescription());
        }
    }

    public Boolean validateMutant(ValidateMutantInputDTO dna) {

        try {

            final var reply= createArrays(dna.getDna());
            boolean isMutant = false;
            int valueVH;
            int count = 0;

            for(int i = 0; i < dna.getDna().size(); i++) {
                valueVH = validateHorizontal(dna.getDna().get(i));
                count+=valueVH;
                if(count > 1) {
                    isMutant = true;
                }
            }

            if(count <= 1){
                int valueVV = validateVertical(reply);
                count+=valueVV;
                if(count > 1) {
                    isMutant = true;
                } else {
                    int valueVD = validateDiagonal(reply);
                    count+=valueVD;
                    if(count > 1) {
                        isMutant = true;
                    }
                }
            }

            return isMutant;
        } catch (Exception e) {
            LOGGER.error(LABEL_ERROR, e);
            throw new BusinessCapabilityException(
                    FATAL_ERROR.getCode(),
                    FATAL_ERROR.getDescription());
        }
    }

    private Integer validateHorizontal(String dna) {

        Integer countRow = 0;
        Integer count = 0;
        Integer lengthRow = dna.length() - 1;
        char value;

        for(int j = 0; j < lengthRow; j++) {
            value = dna.charAt(j);
            if (j != lengthRow) {
                if (value != dna.charAt(j+1)) {
                } else {
                    count+=1;
                    if(count == 3) {
                        countRow+=1;
                    }
                }
            } else {
                if (value != dna.charAt(j)) {
                } else {
                    count+=1;
                    if(count == 3){
                        countRow+=1;
                    }
                }
            }
        }
        return countRow;
    }

    private Integer validateVertical(String[][] reply) {

        int countRow = 0;
        int valueVV;

        for (int i = 0; i < reply.length; i++) {

            StringBuilder dna = new StringBuilder();

            for (int j = 0; j < reply.length; j++) {
                dna.append(reply[j][i]);
            }
            valueVV = validateHorizontal(dna.toString());
            countRow+=valueVV;
        }
        return countRow;
    }

    private Integer validateDiagonal(String[][] reply) {

        int countRow = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((reply[i][j].equals(reply[i+1][j+1])) && (reply[i][j].equals(reply[i+2][j+2])) && (reply[i][j].equals(reply[i+3][j+3]))) {
                    countRow+=1;
                }
            }
        }
        return countRow;
    }

    private String[][] createArrays(List<String> dna) {

        final var sizeA = dna.size();
        String[][] values = new String[sizeA][sizeA];

        for(int i = 0; i < values.length; i++){
            for(int j = 0; j < values[i].length; j++) {
                values[i][j] = String.valueOf(dna.get(i).charAt(j));
            }
        }

        return values;
    }

    private void validateProfile(ValidateMutantDTO profile) throws BusinessCapabilityException {

        if(profile.getBody().getDna() == null ||
                profile.getBody().getDna().isEmpty() ||
                profile.getBody().getDna().stream().count() < 5) {
            throw new BusinessCapabilityException(
                    BAD_REQUEST.getCode(),
                    BAD_REQUEST.getDescription());
        }

        for (String str : profile.getBody().getDna()) {

            if(!str.matches("(A|C|T|G)+") ) {
                throw new BusinessCapabilityException(
                        EMPTY_BODY.getCode(),
                        EMPTY_BODY.getDescription());
            }
        }

    }
}