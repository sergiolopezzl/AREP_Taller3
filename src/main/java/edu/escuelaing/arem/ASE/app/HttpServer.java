package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.service.AppService;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Clase HttpServer para iniciar el servidor HTTP.
 * Esta clase implementa un servidor HTTP básico capaz de manejar solicitudes y devolver respuestas.
 * Utiliza un diseño singleton para garantizar una sola instancia del servidor en toda la aplicación.
 */
public class HttpServer {

    // Tipos de contenido admitidos
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_CSS = "text/css";
    public static final String APPLICATION_JAVASCRIPT = "application/javascript";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPG = "image/jpg";

    // URI base para los archivos estáticos
    public static String baseStaticFileURI;

    // Instancia única del servidor
    private static HttpServer instance;

    // Estado del servidor
    private boolean running = false;

    /**
     * Constructor privado para evitar la creación de instancias fuera de la clase.
     */
    private HttpServer() {
    }

    /**
     * Método estático para obtener la instancia única del servidor.
     *
     * @return La instancia única del servidor.
     */
    public static HttpServer getInstance() {
        if (instance == null) {
            instance = new HttpServer();
        }
        return instance;
    }

    /**
     * Método para iniciar el servidor.
     *
     * @throws URISyntaxException Si la URI es inválida.
     */
    public void start() throws URISyntaxException {
        try (ServerSocket serverSocket = new ServerSocket(35000)) {
            running = true;
            while (running) {
                System.out.println("\nListo para recibir ...");
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     OutputStream os = clientSocket.getOutputStream()) {
                    handleRequest(clientSocket, in, os);
                } catch (IOException e) {
                    System.err.println("Error handling request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
    }

    /**
     * Método para manejar una solicitud entrante.
     *
     * @param clientSocket El socket del cliente.
     * @param in           El flujo de entrada para leer la solicitud.
     * @param os           El flujo de salida para escribir la respuesta.
     * @throws IOException        Si ocurre un error de entrada/salida.
     * @throws URISyntaxException Si la URI es inválida.
     */
    private void handleRequest(Socket clientSocket, BufferedReader in, OutputStream os) throws IOException, URISyntaxException {
        String inputLine;
        boolean firstLine = true;
        String uriStr = "";
        String httpMethod = "";

        while ((inputLine = in.readLine()) != null) {
            if (firstLine) {
                System.out.println("Received: " + inputLine);
                httpMethod = inputLine.split(" ")[0];
                uriStr = inputLine.split(" ")[1];
                firstLine = false;
            }
            if (!in.ready()) {
                break;
            }
        }

        URI requestURI = new URI(uriStr);
        String contentType = TEXT_PLAIN;
        byte[] responseHeader;
        byte[] responseBody;

        try {
            if (requestURI.getPath().startsWith("/action")) {
                responseBody = callService(requestURI, httpMethod);
            } else {
                URI localURI = new URI(baseStaticFileURI + requestURI.getPath());
                contentType = determineContentType(localURI);
                responseBody = httpResponseBody(contentType, localURI);
            }
        } catch (IOException e) {
            URI localURI = new URI(baseStaticFileURI + "/404.html");
            contentType = TEXT_HTML;
            responseBody = httpResponseBody(TEXT_HTML, localURI);
        }
        responseHeader = httpResponseHeader(contentType);

        os.write(responseHeader);
        os.write(responseBody);
        os.flush();

        in.close();
        clientSocket.close();
    }

    /**
     * Método para llamar al servicio correspondiente según la URI y el método HTTP.
     *
     * @param serviceURI La URI del servicio.
     * @param httpMethod El método HTTP utilizado.
     * @return Los datos de respuesta del servicio.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private byte[] callService(URI serviceURI, String httpMethod) throws IOException {
        String calledServiceURI = serviceURI.getPath().substring(7);
        AppService handlerService = SLSpark.findHandler(httpMethod, calledServiceURI);
        return handlerService.handle(serviceURI);
    }

    /**
     * Método para determinar el tipo de contenido basado en la URI del recurso.
     *
     * @param resourceURI La URI del recurso.
     * @return El tipo de contenido del recurso.
     */
    private String determineContentType(URI resourceURI) {
        String contentType = TEXT_PLAIN;
        String path = resourceURI.getPath();
        if (path.endsWith(".html")) {
            contentType = TEXT_HTML;
        } else if (path.endsWith(".css")) {
            contentType = TEXT_CSS;
        } else if (path.endsWith(".js")) {
            contentType = APPLICATION_JAVASCRIPT;
        } else if (path.endsWith(".png")) {
            contentType = IMAGE_PNG;
        } else if (path.endsWith(".jpg")) {
            contentType = IMAGE_JPG;
        }
        return contentType;
    }

    /**
     * Método para obtener el cuerpo de la respuesta HTTP.
     *
     * @param contentType El tipo de contenido de la respuesta.
     * @param resourceURI La URI del recurso.
     * @return Los datos del cuerpo de la respuesta.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public byte[] httpResponseBody(String contentType, URI resourceURI) throws IOException {
        if (contentType.startsWith("image")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(new File(resourceURI.getPath()));
            String formatName = contentType.split("/")[1];
            ImageIO.write(image, formatName, baos);
            return baos.toByteArray();
        }
        return Files.readAllBytes(Paths.get(resourceURI.getPath()));
    }

    /**
     * Método para generar el encabezado de la respuesta HTTP.
     *
     * @param contentType El tipo de contenido de la respuesta.
     * @return Los datos del encabezado de la respuesta.
     */
    public byte[] httpResponseHeader(String contentType) {
        String responseHeader = "HTTP/1.1 200 OK\r\n"
                + "Accept-Ranges: bytes\r\n"
                + "Server: Java HTTP Server\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "\r\n";
        return responseHeader.getBytes();
    }

    /**
     * Método para establecer la ubicación de los archivos estáticos.
     *
     * @param path La ruta de la ubicación de los archivos estáticos.
     */
    public void setStaticFileLocation(String path) {
        baseStaticFileURI = path;
    }

    public static void responseType(String responseType) {
        responseType = TEXT_HTML;
    }
}
