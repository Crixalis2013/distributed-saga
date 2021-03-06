package no.ssb.saga.samples.polyglot;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class PolyglotMainTest {

    @Test
    public void thatCoordinatorCanExecuteSaga() throws URISyntaxException, IOException, InterruptedException {
        PolyglotMain polyglotMain = new PolyglotMain(8342, "127.0.0.1", "target/saga.log").start();

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(new URI("http://127.0.0.1:8342/test1"))
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofString("Hello Polyglot", StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assert.assertEquals(response.statusCode(), 201);
        String json = response.body();
        System.out.println(new JSONObject(json).toString(2));

        polyglotMain.stop();
    }
}
