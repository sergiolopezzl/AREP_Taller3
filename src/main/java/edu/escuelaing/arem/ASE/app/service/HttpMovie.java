package edu.escuelaing.arem.ASE.app.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.escuelaing.arem.ASE.app.utils.Cache;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpMovie {

    // Definición de constantes
    private static final String USER_AGENT = "Mozilla/5.0"; // Agente de usuario para la solicitud HTTP
    private static final String API_KEY = "95abfe39"; // Clave del API para acceder a los servicios de OMDB
    private static final String BASE_URL = "http://www.omdbapi.com/?apikey=" + API_KEY + "&t="; // URL base para las solicitudes de películas

    /**
     * Realiza una solicitud HTTP para obtener información sobre una película específica.
     *
     * @param URIStr La cadena de consulta que contiene el nombre de la película.
     * @return Un objeto JsonObject que contiene la información de la película.
     * @throws IOException Si ocurre un error de entrada/salida durante la solicitud.
     */
    public JsonObject get(String URIStr) throws IOException {
        String movie = URIStr.split("=")[1]; // Extrae el nombre de la película de la cadena de consulta

        if (Cache.getInstance().contains(movie)) {
            System.out.println("La película está en la caché");
            return Cache.getInstance().get(movie); // Devuelve la película desde la caché si está disponible
        }

        URL obj = new URL(BASE_URL + movie); // Construye la URL completa para la solicitud
        System.out.println("URL GET: " + obj.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode(); // Obtiene el código de respuesta HTTP
        System.out.println("Código de respuesta GET: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // Si la solicitud fue exitosa
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine); // Lee la respuesta del servidor y la almacena en un StringBuffer
            }
            in.close();

            // Imprime la respuesta del servidor
            System.out.println(response.toString());
            // Agrega la película a la caché para futuras solicitudes
            Cache.getInstance().add(movie, JsonParser.parseString(response.toString()).getAsJsonObject());
        } else {
            System.out.println("La solicitud GET no funcionó");
        }
        System.out.println("GET terminado");
        return Cache.getInstance().get(movie); // Devuelve la información de la película
    }
}
