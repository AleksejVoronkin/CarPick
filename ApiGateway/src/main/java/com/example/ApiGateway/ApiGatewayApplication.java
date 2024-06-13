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
				.route("CarPick1", r -> r.path("/CarPick1/**")
						.filters(f -> f.rewritePath("/CarPick1/(?<segment>.*)", "/${segment}"))
						.uri("lb://CarPick1"))
				.route("CarPickKia1", r -> r.path("/CarPickKia1/**")
						.filters(f -> f.rewritePath("/CarPickKia1/(?<segment>.*)", "/${segment}"))
						.uri("lb://CarPickKia1"))
				.build();
	}
}