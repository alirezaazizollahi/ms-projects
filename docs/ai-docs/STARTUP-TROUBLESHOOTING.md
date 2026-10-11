# Order Service Startup: Eureka Client Warnings

## Observed behavior

The supplied log contains `BeanCurrentlyInCreationException` for
`scopedTarget.eurekaClient`, followed by `No instances available for localhost`
and `TransportException: Cannot execute request on any known server` during
Eureka registry fetch and registration. Later, Tomcat starts on port `8084` and
`OrderApplication` reports a successful start. In this run the warning is not
a fatal application-context startup failure, but Eureka registration and
registry refresh are failing.

## Source evidence

- `order-ms/src/main/java/com/raalapp/ecommerce/clients/ProductServiceClientConfig.java`
  declares a `RestClient.Builder` bean annotated `@LoadBalanced`.
- That builder creates the client targeting `http://product-service`, where
  load balancing is intended.
- The Eureka URL is `http://localhost:8761/eureka` in
  `configserver/src/main/resources/config/order-service.yaml`.
- The observed load-balancer interceptor also handles Eureka's fixed registry
  URL, interpreting `localhost` as a service ID. Since Eureka is the registry
  being contacted, this leads to recursive discovery and the reported
  `eurekaClient` in-creation failure.

## Recommended correction

Keep the normal `RestClient.Builder` unannotated. Define a separate builder for
the product client with `@LoadBalanced`, and inject that specifically qualified
builder into the product client factory. Eureka's HTTP client can then use a
plain direct connection, while `http://product-service` still resolves through
Eureka.

Do not change Eureka's URL to a service name: the client needs a direct address
for the Eureka server. This log uses localhost URLs, so the diagnosis assumes
all processes run on one host. In containers, `localhost` points to the
container itself and must be replaced by reachable service DNS names.

## Applied change

`ProductServiceClientConfig` applies `LoadBalancerInterceptor` directly to the
product-service `RestClient`; it does not expose a `@LoadBalanced
RestClient.Builder` bean. This keeps load balancing scoped to product calls and
leaves Eureka's own registry HTTP client on its direct transport.

## Verify after correcting

Start Eureka, then Config Server and order service. Check the Eureka dashboard
at `http://localhost:8761` and confirm `ORDER-SERVICE` is registered. The log
should no longer show `No instances available for localhost` during Eureka's
registry fetch or registration. A successful process start alone does not
prove registration worked. If direct connection errors persist, verify Eureka
is listening on port `8761`.
