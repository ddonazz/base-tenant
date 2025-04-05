package it.andrea.start.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
	description = "", 
	method = "", 
	summary = "", 
	responses = { 
		@ApiResponse(responseCode = "200", description = "OK"), 
		@ApiResponse(responseCode = "400", description = "Bad request"), 
		@ApiResponse(responseCode = "401", description = "Not authorized"), 
		@ApiResponse(responseCode = "403", description = "Forbidden"), 
		@ApiResponse(responseCode = "500", description = "Internal server error") 
		}
	)
public @interface CustomApiOperation {

    String description() default "No description provided";

    String method() default "POST";

    String summary() default "No summary provided";
}