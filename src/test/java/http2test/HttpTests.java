package http2test;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.logging.log4j.util.Strings;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
        "server.http2.enabled=false",
    }
)
class HttpTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(
            this.restTemplate.getForObject("/", String.class)
        ).contains("Hello!");
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 1024, 4096, 4097, 9_000_000})
    void httpPostShouldWork(int size) throws Exception {
        assertThat(
           HttpClient.newBuilder().build().send(HttpRequest.newBuilder(
               URI.create(this.restTemplate.getRootUri())
           ).POST(BodyPublishers.ofString(
               Strings.repeat("x", size)
           )).version(Version.HTTP_2).expectContinue(true).build(), BodyHandlers.ofString())
        )
        .satisfies(res -> assertThat(res.version()).isEqualTo(HttpClient.Version.HTTP_1_1))
        .extracting(HttpResponse::body, InstanceOfAssertFactories.STRING)
        .contains("Heard: " + size);
    }

}