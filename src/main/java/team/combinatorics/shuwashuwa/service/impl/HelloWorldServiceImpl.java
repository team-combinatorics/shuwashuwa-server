package team.combinatorics.shuwashuwa.service.impl;

import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.service.HelloWorldService;

@Component
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String sayHello() {
        return "Hello, world!\n";
    }

    @Override
    public String leesou() {
        return "Test for leesou!\n";
    }
}
