package com.demo.booking.rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Booking Service API",
        description = "숙박 예약 서비스 API 문서",
        version = "v1.0.0",
        contact = @Contact(
            name = "Booking Service Team",
            email = "booking@demo.com",
            url = "https://demo.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "로컬 개발 서버"),
        @Server(url = "https://api.demo.com", description = "운영 서버")
    }
)
public class SwaggerConfig {
}