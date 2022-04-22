package com.meli.service.backend.mutant.command;

import com.meli.service.backend.mutant.entities.MeLiResponseEntity;
import com.meli.service.backend.mutant.exception.BusinessCapabilityException;

public interface MeLiCommand<T, R> {

    MeLiResponseEntity<R> execute(T request) throws BusinessCapabilityException;

    MeLiResponseEntity<R> executeFallback(T request, Throwable throwable)
            throws BusinessCapabilityException;
}
