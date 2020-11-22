package team.combinatorics.shuwashuwa.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.service.HelloWorldService;

@RestController
@AllArgsConstructor
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    @GetMapping("/hello")
    public String helloWorld() {
        return helloWorldService.sayHello();
    }

}
