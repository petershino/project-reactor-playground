package com.petershino.webtestclientfunctional.reporitory;

import com.petershino.webtestclientfunctional.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
}
