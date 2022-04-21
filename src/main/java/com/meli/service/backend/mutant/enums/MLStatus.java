package com.meli.service.backend.mutant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MLStatus {
    SUCCEED("0000", "Servicio ejecutado con exito."),
    FATAL_ERROR("0001", "Error inesperado en la ejecución del servicio."),
    DB_ERROR("0002", "Error al guardar los datos en la BD."),
    EMPTY_BODY("0003", "La información recibida es inválida."),
    EMPTY("0004","No se han encontrado datos para visualizar el estado."),
    DATABASE_ERROR("0005", "No se ha podido conectar a la base de datos."),
    INVALID_BODY("0006", "Error al mapear el request."),
    TIMEOUT("0007", "Llamado a servicio externo superó el tiempo de espera máximo."),
    BAD_REQUEST("0008", "Solicitud incorrecta, faltan datos de entrada en el request.");

    private final String code;
    private final String description;

}