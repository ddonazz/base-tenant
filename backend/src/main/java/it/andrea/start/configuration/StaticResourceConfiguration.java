package it.andrea.start.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Value(value = "${app.static.path}")
    private String staticPath;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META_INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS)
                .addResourceLocations(staticPath);
    }
}
