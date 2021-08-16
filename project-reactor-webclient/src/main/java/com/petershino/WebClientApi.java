package com.petershino;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class WebClientApi {

    private WebClient webClient;

    WebClientApi() {
        this.webClient = WebClient.create("http://localhost:8080/products");
//        this.webClient = WebClient.builder()
//                .baseUrl("http://localhost:8080/products")
//                .build();
    }

    public static void main(String args[]) {
        WebClientApi api = new WebClientApi();

        api.saveProduct()
                .thenMany(api.getAllProducts())
                .take(1)
                .flatMap(p -> api.updateProduct(p.getId(), "White Tea", new BigDecimal("0.99")))
                .flatMap(p -> api.deleteProduct(p.getId()))
                .thenMany(api.getAllProducts())
                .thenMany(api.getAllEvents())
                .subscribe(System.out::println);
    }

    private Mono<ResponseEntity<Product>> saveProduct() {
        return webClient.post()
                .body(Mono.just(new Product(null, "Blue Tea", new BigDecimal("1.99"))), Product.class)
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Product.class))
                .doOnSuccess(o -> System.out.println("**********POST: " + o));
    }


    private Flux<Product> getAllProducts() {
        return webClient.get().retrieve().bodyToFlux(Product.class)
                .doOnNext(o -> System.out.println("**********GET: " + o));
    }

    private Mono<Product> updateProduct(String id, String name, BigDecimal price) {
        return webClient.put()
                .uri("/" + id) // .uri("/{id}")
                .body(Mono.just(new Product(null, name, price)), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(o -> System.out.println("**********UPDATE " + o));
    }

    private Mono<Void> deleteProduct(String id) {
        return webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(o -> System.out.println("**********DELETE " + o));
    }

    private Flux<ProductEvent> getAllEvents() {
        return webClient.get()
                .uri("/events")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(ProductEvent.class);
    }
}
