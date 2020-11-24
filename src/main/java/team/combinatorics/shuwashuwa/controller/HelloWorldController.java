package team.combinatorics.shuwashuwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.combinatorics.shuwashuwa.service.HelloWorldService;

@Api(value = "Hello模块说明")
@RestController
@AllArgsConstructor
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    @ApiOperation(value = "sdfsdf", notes = "hello world")
    @GetMapping("/hello")
    public String helloWorld() {
        return helloWorldService.sayHello();
    }

    @ApiOperation(value = "sayHello接口说明", notes = "李保霖大帅哥")
    @GetMapping("/sayHello")
    public String sayHello(@ApiParam("参数：要sayhello的名字")
                           @RequestParam String name) {
        return "Hello " + name + "!\n";
    }

    @ApiOperation(value = "leesou test", notes = "leesou test for swagger")
    @GetMapping("/leesouTest")
    public String leesouTest(@ApiParam("参数：输入的字符串")
                             @RequestParam(defaultValue = "leesou") String name)
    {
        return name + " love HoneyWorks!\n";
    }
}
