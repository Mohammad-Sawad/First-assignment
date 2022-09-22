package com.demo.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.demo.spring.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("UPDATE Product e set e.price=:price where productId=:productId ")  
    @Modifying 
    @Transactional 
    public int updatePrice(int productId,double price);
}
