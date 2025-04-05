package it.andrea.start.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticResourceConfiguration implements WebMvcConfigurer {

    private final String staticPath;

    public StaticResourceConfiguration(Environment environment) {
	staticPath = environment.getProperty("app.static.path");
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META_INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS).addResourceLocations(staticPath);
    }
}
