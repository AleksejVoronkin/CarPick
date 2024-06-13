package com.example.ApiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("CarPick1_root", r -> r.path("/")
						.uri("http://localhost:8081/"))
				.route("CarPick1_decode", r -> r.path("/decode")
						.uri("http://localhost:8081/decode"))
				.route("CarPick1_toPart", r -> r.path("/toPart")
						.uri("http://localhost:8081/toPart"))
				.route("CarPickKia1_root", r -> r.path("/")
						.uri("http://localhost:8082/"))
				.route("CarPickKia1_decode", r -> r.path("/decode")
						.uri("http://localhost:8082/decode"))
				.route("CarPickKia1_toPart", r -> r.path("/toPart")
						.uri("http://localhost:8082/toPart"))
				.build();
	}
}
