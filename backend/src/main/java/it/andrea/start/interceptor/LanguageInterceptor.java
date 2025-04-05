package it.andrea.start.interceptor;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LanguageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
	String language = request.getHeader("Accept-Language");
	if (language == null || language.isEmpty()) {
	    language = "it-IT";
	}
	LocaleContextHolder.setLocale(Locale.forLanguageTag(language));
	return true;
    }

}
