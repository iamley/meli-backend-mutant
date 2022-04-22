package com.meli.service.backend.mutant.utils.impl;

import com.meli.service.backend.mutant.enums.LogLevel;
import com.meli.service.backend.mutant.model.LoggerDataDTO;
import com.meli.service.backend.mutant.utils.UtilityLog;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component("UtilityLog")
public class UtilityLogImpl implements UtilityLog {

    @Override
    public void put(final Logger logger, final LoggerDataDTO data) {
        if (null != data) {
            MDC.put("path", data.getPath());
            MDC.put("apiCode", data.getCode());
            MDC.put("apiMsg", data.getCodeMessage());
            MDC.put("executeTime", null != data.getExecuteTime() ? data.getExecuteTime().toString() + " ms" : null);
            MDC.put("headers", data.getHeaders());
            MDC.put("requestId", data.getRequestId());

            executeLog(logger, data.getLevel(), data.getMessage());

            MDC.clear();
        }
    }

    @Override
    public void put(final Logger logger, final LoggerDataDTO data, final Exception exception) {
        if (null != data) {
            data.setMessage(exceptionToString(exception));

            put(logger, data);
        }
    }

    private void executeLog(final Logger logger, final LogLevel level, final String message) {
        switch (level) {
            case INFO:
                logger.info(message);
            case ERROR:
                logger.error(message);
            default:
                logger.debug(message);
        }
    }

    private static String exceptionToString(final Exception exception) {
        var stringWriter = new StringWriter();

        if (null != exception) {
            exception.printStackTrace(new PrintWriter(stringWriter));
        }

        return stringWriter.toString();
    }

}