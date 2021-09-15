package com.jc.system.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 * @author Administrator
 * @date 2020-07-01
 */
@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConf {
 
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jc.test")) // 注意修改此处的包名
                .paths(PathSelectors.any())
                .build();
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("接口列表 jcap v3.1") // 任意，建议规范：接口列表_项目缩写_版本号
                .description("接口测试") // 任意，建议规范：**接口测试
                .termsOfServiceUrl("http://127.0.0.1:8080/basic/doc.html") // 将“url”换成自己的ip:port/项目名/doc.html
                .contact("jcap") // 无所谓（这里是作者的别称）
                .version("3.1")
                .build();
    }
}
