package com.meli.service.backend.mutant.utils;

import com.meli.service.backend.mutant.enums.ServerErrorCommon;
import com.meli.service.backend.mutant.model.StatusDataDTO;

public interface MeLiStatusResponseUtil {

    StatusDataDTO getStatusResponse(final ServerErrorCommon commonError);
    StatusDataDTO getStatusResponse(final Throwable throwable);
    StatusDataDTO getStatusResponse(final ServerErrorCommon commonError, final Throwable throwable);

}