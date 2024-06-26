package com.noman.product_service.config;

import com.noman.product_service.model.Product;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ChangeUnit(id="pre_seed_data2", order = "003", author = "noman")
public class DatabasePreSeed {
    private final MongoTemplate mongoTemplate;
    public DatabasePreSeed(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    /** This is the method with the migration code **/
    @Execution
    public void changeSet() {
        List<Product> productList = new ArrayList<>();
        productList.add(createProduct("Iphone 6", "Apple product Iphone 6", BigDecimal.valueOf(100)));
        productList.add(createProduct("Iphone 7", "Apple product Iphone 7", BigDecimal.valueOf(200)));
        productList.add(createProduct("Iphone 8", "Apple product Iphone 8", BigDecimal.valueOf(250)));
        mongoTemplate.insert(productList, "product");
    }
    /**
     This method is mandatory even when transactions are enabled.
     They are used in the undo operation and any other scenario where transactions are not an option.
     However, note that when transactions are avialble and Mongock need to rollback, this method is ignored.
     **/
    @RollbackExecution
    public void rollback() {
    }

    private Product createProduct(String name, String desc, BigDecimal price) {
        return Product.builder().name(name).description(desc).price(price).build();
    }
}
