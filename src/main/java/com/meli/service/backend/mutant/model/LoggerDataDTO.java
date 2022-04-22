package com.meli.service.backend.mutant.model;

import com.meli.service.backend.mutant.enums.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggerDataDTO implements Serializable {
    private static final long serialVersionUID = -4521148943105738592L;

    @NonNull
    private LogLevel level;
    private String message;
    private String path;
    private String code;
    private String codeMessage;
    private Long executeTime;
    private String headers;
    private String requestId;

}