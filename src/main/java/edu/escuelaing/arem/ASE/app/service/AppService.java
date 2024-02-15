package edu.escuelaing.arem.ASE.app.service;

import java.io.IOException;
import java.net.URI;

/**
 * Interfaz funcional que define un servicio de la aplicación.
 */
@FunctionalInterface
public interface AppService {

    /**
     * Maneja la solicitud URI y devuelve un arreglo de bytes como respuesta.
     *
     * @param requestURI La URI de la solicitud.
     * @return Un arreglo de bytes que representa la respuesta.
     * @throws IOException Si ocurre un error de entrada/salida al manejar la solicitud.
     */
    byte[] handle(URI requestURI) throws IOException;
}
