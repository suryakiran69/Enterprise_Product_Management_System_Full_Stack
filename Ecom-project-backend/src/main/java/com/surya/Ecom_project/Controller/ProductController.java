package com.surya.Ecom_project.Controller;

import com.surya.Ecom_project.Model.Products;
import com.surya.Ecom_project.Service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")

public class ProductController {

    @Autowired
    productService service;
    @RequestMapping("/")
    public String greet(){
        return "Welcome to Surya Kiran Enterprises";
    }

    /*@GetMapping("/products")
    public List<Products> getProducts(){
        return service.getProducts();
    }*/
    @GetMapping("/products")
    public ResponseEntity<List<Products>> getProducts(){
        return new ResponseEntity<>(service.getProducts(), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable int id){
        Products prod1 = service.getProductById(id);
        if (prod1 != null)
             return new ResponseEntity<>( service.getProductById(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/product")
    public ResponseEntity<?> postProducts(@RequestPart Products product, @RequestPart MultipartFile imageFile){

        try {
            Products product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("product/{id}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id ){
        Products product = service.getProductById(id);
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }


    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestPart("product") Products product,
            @RequestPart("imageFile") MultipartFile imageFile) {

        try {
            Products updatedProduct = service.updateProduct(id, product, imageFile);
            if (updatedProduct != null) {
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update product", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deletProduct(@PathVariable int id){

        Products prod1 = service.getProductById(id);
        if (prod1 != null) {
            service.deletProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Products>> searchProducts(@RequestParam String keyword){
        List<Products> products = service.searchProducts(keyword);
        System.out.println("searching with " + keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
