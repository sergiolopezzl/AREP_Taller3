package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.service.AppService;
import java.util.HashMap;
import java.util.Map;

/**
 * SLSpark es un enrutador HTTP que maneja las rutas GET y POST.
 */
public class SLSpark {

    // Instancia única de SLSpark
    private static SLSpark instance;

    // Mapas para almacenar las rutas GET y POST junto con sus controladores
    private final Map<String, AppService> getRoutes = new HashMap<>();
    private final Map<String, AppService> postRoutes = new HashMap<>();

    /**
     * Constructor privado para evitar la creación de instancias fuera de la clase.
     */
    private SLSpark() {
    }

    /**
     * Método estático para obtener la instancia única de SLSpark.
     *
     * @return La instancia única de SLSpark.
     */
    public static SLSpark getInstance() {
        if (instance == null) {
            instance = new SLSpark();
        }
        return instance;
    }

    /**
     * Establece la ubicación de los archivos estáticos.
     *
     * @param path La ruta de la ubicación de los archivos estáticos.
     */
    public static void staticFileLocation(String path) {
        HttpServer.getInstance().setStaticFileLocation(path);
    }

    /**
     * Registra un controlador para la ruta GET especificada.
     *
     * @param path    La ruta GET.
     * @param handler El controlador asociado a la ruta.
     */
    public static void get(String path, AppService handler) {
        getInstance().getRoutes.put(path, handler);
    }

    /**
     * Registra un controlador para la ruta POST especificada.
     *
     * @param path    La ruta POST.
     * @param handler El controlador asociado a la ruta.
     */
    public static void post(String path, AppService handler) {
        getInstance().postRoutes.put(path, handler);
    }

    /**
     * Encuentra el controlador asociado al método y ruta especificados.
     *
     * @param method El método HTTP.
     * @param path   La ruta HTTP.
     * @return El controlador asociado al método y ruta especificados, o null si no se encuentra.
     */
    public static AppService findHandler(String method, String path) {
        if ("GET".equalsIgnoreCase(method)) {
            return getInstance().getRoutes.get(path);
        } else if ("POST".equalsIgnoreCase(method)) {
            return getInstance().postRoutes.get(path);
        } else {
            return null;
        }
    }

}
