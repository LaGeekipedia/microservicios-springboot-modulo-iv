package com.miempresa.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(0)
public class AuthFilter implements GlobalFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_VALIDO = "Bearer miclave123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (!exchange.getRequest().getHeaders().containsKey(AUTH_HEADER)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);

        if (!TOKEN_VALIDO.equals(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);

    }
}
