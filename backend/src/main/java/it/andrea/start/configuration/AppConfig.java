package it.andrea.start.configuration;

import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import it.andrea.start.interceptor.LanguageInterceptor;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig implements WebMvcConfigurer {

    private final LanguageInterceptor languageInterceptor;
    private final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

    public AppConfig(LanguageInterceptor languageInterceptor, PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver) {
        super();
        this.languageInterceptor = languageInterceptor;
        this.pageableHandlerMethodArgumentResolver = pageableHandlerMethodArgumentResolver;
    }

    @Bean
    DocumentBuilderFactory documentBuilderFactory() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            return factory;
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure DocumentBuilderFactory", e);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
        resolvers.add(pageableHandlerMethodArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(languageInterceptor);
    }

}
