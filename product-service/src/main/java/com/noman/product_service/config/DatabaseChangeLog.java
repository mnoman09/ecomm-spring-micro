package com.noman.product_service.config;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.noman.product_service.model.Product;
import com.noman.product_service.repository.ProductRepository;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class DatabaseChangeLog {
    /** This is the method with the migration code **/
    @ChangeSet(order = "002", id = "seedDatabaseProducts", author = "noman")
    public void changeSet(ProductRepository productRepository) {
        List<Product> productList = new ArrayList<>();
        productList.add(createProduct("Iphone 14", "Apple product Iphone 14", BigDecimal.valueOf(400)));
        productList.add(createProduct("Iphone 15", "Apple product Iphone 15", BigDecimal.valueOf(600)));
        productList.add(createProduct("Iphone 13", "Apple product Iphone 13", BigDecimal.valueOf(300)));
        productRepository.insert(productList);
    }

    private Product createProduct(String name, String desc, BigDecimal price) {
        return Product.builder().name(name).description(desc).price(price).build();
    }
}
