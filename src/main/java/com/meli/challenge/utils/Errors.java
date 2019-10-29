package com.meli.challenge.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that contains application errors.
 */
@AllArgsConstructor
@Getter
public enum Errors {

    IP_FORMAT_ERROR("Formato de IP incorrecto"),
    IP_NOT_FOUND_ERROR("Información sobre IP no encontrada"),
    DATABASE_ERROR("Error al conectarse a la base de datos, las estadísticas no se guardarán");

    private String message;
}
