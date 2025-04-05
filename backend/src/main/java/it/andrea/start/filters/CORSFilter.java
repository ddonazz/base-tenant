package it.andrea.start.filters;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.stereotype.Component;

@Component
public class CORSFilter extends CorsFilter {

    public CORSFilter() {
	super(corsConfigurationSource());
    }

    private static UrlBasedCorsConfigurationSource corsConfigurationSource() {
	CorsConfiguration config = new CorsConfiguration();
	config.setAllowCredentials(true);
	config.addAllowedOrigin("*");
	config.addAllowedHeader("*");
	config.addAllowedMethod("POST");
	config.addAllowedMethod("GET");
	config.addAllowedMethod("PUT");
	config.addAllowedMethod("OPTIONS");
	config.addAllowedMethod("DELETE");
	config.setMaxAge(86400L); // 24 ore

	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", config);
	return source;
    }

}
