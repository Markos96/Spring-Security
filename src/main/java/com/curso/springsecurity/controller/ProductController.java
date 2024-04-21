package com.curso.springsecurity.controller;

import com.curso.springsecurity.data.entity.Product;
import com.curso.springsecurity.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('READ_ALL_PRODUCT')")
    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    @PreAuthorize("hasAuthority('SAVE_ONE_PRODUCT')")
    @PostMapping()
    public ResponseEntity<Product> saveOneProduct(@RequestBody @Valid Product product){
        return ResponseEntity.ok(productService.saveProduct(product));
    }


}
