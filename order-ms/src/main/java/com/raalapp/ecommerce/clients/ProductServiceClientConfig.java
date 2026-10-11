package com.raalapp.ecommerce.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {

    @Bean
    public ProductServiceClient productServiceClient(LoadBalancerClient loadBalancerClient) {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://product-service")
                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        (request, response) ->
                    Optional.empty()
                )
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();
        return factory.createClient(ProductServiceClient.class);
    }
}
