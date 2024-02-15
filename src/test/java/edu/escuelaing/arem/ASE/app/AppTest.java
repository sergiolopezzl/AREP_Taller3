package edu.escuelaing.arem.ASE.app;

import com.google.gson.JsonObject;
import edu.escuelaing.arem.ASE.app.service.HttpMovie;
import org.junit.Test;
import java.io.IOException;


import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testHttpMovie() throws IOException {
        HttpMovie httpMovie = new HttpMovie();
        String movieName = "Guardians of the Galaxy";
        JsonObject jsonObject = httpMovie.get("/movies?name=" + movieName);
        assertNotNull(jsonObject);
        assertEquals(movieName, jsonObject.get("Title").getAsString());
    }

    @Test
    public void testStaticFileLocation() {
        SLSpark.staticFileLocation("src/main/resources/public");
        assertEquals("src/main/resources/public", HttpServer.baseStaticFileURI);
    }

}
