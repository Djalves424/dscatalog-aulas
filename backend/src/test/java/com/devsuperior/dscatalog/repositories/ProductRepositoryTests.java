package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private Long exitingId;
    private Long countTotalProducts;

    @BeforeEach
    void setup() throws Exception {
        exitingId = 1L;
        countTotalProducts = 25L;

    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdInsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(exitingId);

        Optional<Product> result = repository.findById(exitingId);
        Assertions.assertFalse(result.isPresent());
    }
}
