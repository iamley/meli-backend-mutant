package com.meli.service.backend.mutant.utils;

import com.meli.service.backend.mutant.model.LoggerDataDTO;
import org.slf4j.Logger;

public interface UtilityLog {

    void put(final Logger logger, final LoggerDataDTO data);
    void put(final Logger logger, final LoggerDataDTO data, final Exception exception);

}