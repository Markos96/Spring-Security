package com.curso.springsecurity.controller;

import com.curso.springsecurity.data.entity.Product;
import com.curso.springsecurity.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    @PostMapping()
    public ResponseEntity<Product> saveOneProduct(@RequestBody @Valid Product product){
        return ResponseEntity.ok(productService.saveProduct(product));
    }


}
