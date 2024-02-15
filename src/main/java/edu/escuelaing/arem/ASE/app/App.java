package edu.escuelaing.arem.ASE.app;

import com.google.gson.JsonObject;
import edu.escuelaing.arem.ASE.app.service.HttpMovie;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Clase principal de la aplicación.
 * Autor: Sergio López
 */
public class App {

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     * @throws IOException        Si ocurre un error de entrada/salida.
     * @throws URISyntaxException Si ocurre un error de sintaxis de URI.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Establece la ubicación de los archivos estáticos para SLSpark.
        SLSpark.staticFileLocation("src/main/resources/public");

        // Define una ruta para manejar las solicitudes GET a "/movie".
        SLSpark.get("/movie", (requestURI) -> {
            // Crea una instancia de HttpMovie para realizar la solicitud de la película.
            HttpMovie service = new HttpMovie();
            // Obtiene la cadena de consulta de la solicitud URI.
            String query = requestURI.getQuery();
            // Obtiene el JsonObject de la película utilizando el servicio HttpMovie.
            JsonObject response = service.get(query);
            // Convierte el JsonObject en una matriz de bytes y la devuelve como respuesta.
            return response.toString().getBytes();
        });

        // Define una ruta para manejar las solicitudes POST a "/movie".
        SLSpark.post("/movie", (requestURI) -> {
            // Aquí puedes manejar la lógica para procesar la solicitud POST.
            // Por ejemplo, puedes obtener datos de la solicitud y realizar acciones en base a esos datos.
            // En este ejemplo, se crea una instancia de HttpMovie para manejar la solicitud POST.
            HttpMovie service = new HttpMovie();
            // Obtiene la cadena de consulta de la solicitud URI.
            String query = requestURI.getQuery();
            // Realiza alguna lógica con los datos recibidos.
            // En este caso, simplemente devuelve un mensaje de éxito.
            JsonObject response = new JsonObject();
            response.addProperty("message", "POST request received successfully");
            // Convierte el JsonObject en una matriz de bytes y lo devuelve como respuesta.
            return response.toString().getBytes();
        });

        SLSpark.post("/users", p -> {
            // Cuerpo para agregar un nuevo usuario
            HttpServer.responseType("text/html");
            return "<h1>Se ha agregado el usuario exitosamente</h1>".getBytes();
        });


        // Inicia el servidor HTTP.
        HttpServer.getInstance().start();
    }
}
