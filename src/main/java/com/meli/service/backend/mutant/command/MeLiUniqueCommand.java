package com.meli.service.backend.mutant.command;

import com.meli.service.backend.mutant.entities.MeLiResponseEntity;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;

public interface MeLiUniqueCommand<R> {

    MeLiResponseEntity<R> execute() throws BusinessCapabilityException;

    MeLiResponseEntity<R> executeFallback(Throwable throwable)
            throws BusinessCapabilityException;
}
