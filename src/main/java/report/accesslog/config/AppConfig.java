package report.accesslog.config;

import springfox.documentation.service.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import report.accesslog.processor.HttpServerAccessLogProcessor;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.File;

import static springfox.documentation.builders.PathSelectors.any;

@Configuration
public class AppConfig {

    @Value("${spruce.server.access.log.name}")
    String fileName;

    @Bean
    public HttpServerAccessLogProcessor logProcessor() {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("File %s does not exist", fileName  ));
        }
        return new HttpServerAccessLogProcessor(fileName);
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("Room").select()
                .apis(RequestHandlerSelectors.basePackage("report.accesslog"))
                .paths(any()).build().apiInfo(new ApiInfo("Server Access Log Report",
                        "A set of services to provide report on server access logs", "1.0.0", null,
                        new Contact("Simon Nektalov", "https://twitter.com/snektal", null),null, null));
    }
}
