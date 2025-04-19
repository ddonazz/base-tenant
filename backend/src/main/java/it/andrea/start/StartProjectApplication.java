package it.andrea.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class StartProjectApplication extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(StartProjectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(StartProjectApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        LOG.info("-----------------------------");
        LOG.info("StartApplication->configure");
        LOG.info("-----------------------------");

        final SpringApplicationBuilder applicationBuilder = application.sources(StartProjectApplication.class);
        ClassLoader loader = applicationBuilder.application().getClassLoader();

        LOG.info("loader : {}", loader.getName());

        return application.sources(StartProjectApplication.class);
    }

}