package team.combinatorics.shuwashuwa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
常用注解说明
swagger 通过注解接口生成文档，包括接口名，请求方法，参数，返回信息等。

@Api: 修饰整个类，用于controller类上
@ApiOperation: 描述一个接口，用户controller方法上
@ApiParam: 单个参数描述
@ApiModel: 用来对象接收参数,即返回对象
@ApiModelProperty: 对象接收参数时，描述对象的字段
@ApiResponse: Http响应其中的描述，在ApiResonse中
@ApiResponses: Http响应所有的描述，用在
@ApiIgnore: 忽略这个API
@ApiError: 发生错误的返回信息
@ApiImplicitParam: 一个请求参数
@ApiImplicitParam: 多个请求参数
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        // 构造函数传入初始化规范，这是swagger2规范
        return new Docket(DocumentationType.SWAGGER_2)
                // apiInfo： 添加api详情信息，参数为ApiInfo类型的参数
                // 这个参数包含了第二部分的所有信息比如标题、描述、版本之类的，开发中一般都会自定义这些信息
                .apiInfo(apiInfo())
                // 组名
                .groupName("Combinatorics")
                .enable(true)
                .select()
                // 过滤条件
                .apis(RequestHandlerSelectors.any())
                // 控制哪些路径的api会被显示出来
                .paths(PathSelectors.any())
                .build();
    }

    /*
     * 配置多个分组只需要在生成几个bean就行了
     */


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Shuwashuwa-server API Doc")
                .description("Maruyama Aya desu")
                .version("1.0")
                .license("GPL-3.0 License")
                .build();
    }
}
