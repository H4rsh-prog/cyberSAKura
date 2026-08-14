package com.tools.module.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
public class BaseController {
	@Autowired RequestMappingHandlerMapping mappings;

	@RequestMapping("/routes")
	public List<RouteInfo> getRoutes(){
		List<RouteInfo> routes = this.mappings.getHandlerMethods().entrySet().stream().map(
				entry -> {
					RequestMappingInfo mappingInfo = entry.getKey();
					HandlerMethod handlerMethod = entry.getValue();
					return new RouteInfo(
							mappingInfo.getPathPatternsCondition().toString(),
							mappingInfo.getMethodsCondition().getMethods().stream()
								.map(Enum::toString).collect(Collectors.toList()),
							handlerMethod.getBeanType().getSimpleName()+"."+handlerMethod.getMethod().getName());
				}
			).collect(Collectors.toList());
		routes.sort(new Comparator<RouteInfo>() {

			@Override
			public int compare(RouteInfo o1, RouteInfo o2) {
				return o1.HANDLER().compareTo(o2.HANDLER());
			}
		});
		return routes;
	}
	record RouteInfo(
			String PATH,
			List<String> METHODS,
			String HANDLER
		) {}
}
