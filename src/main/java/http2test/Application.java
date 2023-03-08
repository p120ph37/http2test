package http2test;

import static org.springframework.boot.WebApplicationType.SERVLET;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
            .web(SERVLET)
            .run(args);
    }

    @RestController
    @RequestMapping("/")
    public static class HelloController {

        @GetMapping
        @ResponseBody
        public String hello() {
            return "Hello!";
        }

        @PostMapping
        @ResponseBody
        public String post(
            @RequestBody String req
        ) {
            return "Heard: " + req.length();
        }

    }

}
