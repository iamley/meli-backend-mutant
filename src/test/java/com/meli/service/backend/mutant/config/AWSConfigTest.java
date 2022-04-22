package com.meli.service.backend.mutant.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AWSConfigTest {

    @InjectMocks
    private AWSConfig awsConfig;

    @Test
    void configSuccess(){
        ReflectionTestUtils.setField(awsConfig, "awsRegion", "us-west-1");
        assertNotNull(awsConfig.getClientDynamo());
    }
}