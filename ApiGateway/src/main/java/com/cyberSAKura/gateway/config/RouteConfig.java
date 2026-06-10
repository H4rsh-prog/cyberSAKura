package com.cyberSAKura.gateway.config;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


@Configuration
public class RouteConfig {
    
	private static final Logger log = LoggerFactory.getLogger(RouteConfig.class);
	
	@Bean
	public RouterFunction<ServerResponse> dynamicRouter(DiscoveryClient discoveryClient) {
		return GatewayRouterFunctions.route("dynamic_route")
				.route(RequestPredicates.path("/{serviceName}/**"), HandlerFunctions.http())
				.before(request -> {
					String serviceName = request.pathVariable("serviceName");
					List<String> services = discoveryClient.getServices();
					log.debug("Available services: {} (Looking for service: {})", services, serviceName);
					if(services != null && services.contains(serviceName)) {
						List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
						if(instances != null && !instances.isEmpty()) {
							URI targetUri = instances.get(0).getUri();
							log.debug("Routing to {}", targetUri);
							return BeforeFilterFunctions.uri(targetUri).apply(request);
						}
					}
					log.warn("Service '{}' not found in registry. Returning original request.", serviceName);
					return request;
				})
				.after(AfterFilterFunctions.addResponseHeader("X-Dynamic-Route", "true"))
				.build();
	}
}
