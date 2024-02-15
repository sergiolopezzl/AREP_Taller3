package edu.escuelaing.arem.ASE.app.utils;

import com.google.gson.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase para manejar una caché de películas.
 */
public class Cache {

    private static Cache instance;
    private Map<String, JsonObject> data;

    /**
     * Constructor privado para garantizar la implementación del patrón Singleton.
     */
    private Cache() {
        this.data = new ConcurrentHashMap<>();
    }

    /**
     * Obtiene la instancia única de Cache (patrón Singleton).
     *
     * @return La instancia única de Cache.
     */
    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    /**
     * Agrega una película a la caché.
     *
     * @param movie El nombre de la película.
     * @param json  El objeto JSON que representa la película.
     */
    public void add(String movie, JsonObject json) {
        data.putIfAbsent(movie, json);
    }

    /**
     * Obtiene el objeto JSON de una película de la caché.
     *
     * @param movie El nombre de la película.
     * @return El objeto JSON de la película, o null si no está en la caché.
     */
    public JsonObject get(String movie) {
        return data.get(movie);
    }

    /**
     * Verifica si la caché contiene información sobre una película específica.
     *
     * @param movie El nombre de la película.
     * @return true si la caché contiene la película, false de lo contrario.
     */
    public boolean contains(String movie) {
        return data.containsKey(movie);
    }
}
