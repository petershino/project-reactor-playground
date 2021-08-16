package com.petershino.webtestclientannotation;

import com.petershino.functionalendpoints.model.Product;
import com.petershino.functionalendpoints.reporitory.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Configuration
public class InitDatabse {
    @Bean
    CommandLineRunner init(ReactiveMongoOperations operations, ProductRepository repository) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product(null, "Big Latte", new BigDecimal("2.99")),
                    new Product(null, "Big Decaf", new BigDecimal("2.49")),
                    new Product(null, "Green Tea", new BigDecimal("1.99")))
                    .flatMap(repository::save);

            productFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);

            /*operations.collectionExists(Product.class)
                    .flatMap(exists -> exists ? operations.dropCollection(Product.class) : Mono.just(exists)) // Mono.empty()
					.thenMany(v -> operations.createCollection(Product.class))
					.thenMany(productFlux)
					.thenMany(repository.findAll())
					.subscribe(System.out::println);*/
        };
    }
}
