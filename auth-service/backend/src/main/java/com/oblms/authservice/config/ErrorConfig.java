package com.oblms.authservice.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ErrorConfig {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(
                    WebRequest webRequest,
                    ErrorAttributeOptions options) {

                Map<String, Object> attributes =
                        new LinkedHashMap<>(
                                super.getErrorAttributes(webRequest, options));

                Throwable error = getError(webRequest);

                if (error instanceof ResponseStatusException rse
                        && rse.getReason() != null) {
                    attributes.put("message", rse.getReason());
                } else if (error != null && error.getMessage() != null) {
                    attributes.put("message", error.getMessage());
                }

                return attributes;
            }
        };
    }
}
