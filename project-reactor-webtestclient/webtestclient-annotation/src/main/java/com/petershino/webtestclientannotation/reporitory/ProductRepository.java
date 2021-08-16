package com.petershino.webtestclientannotation.reporitory;

import com.petershino.webtestclientannotation.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
}
