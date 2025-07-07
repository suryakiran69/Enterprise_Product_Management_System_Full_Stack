package com.surya.Ecom_project.Service;

import com.surya.Ecom_project.Controller.ProductController;
import com.surya.Ecom_project.Model.Products;
import com.surya.Ecom_project.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class productService {
    @Autowired
    ProductRepo repo;
    Products product;
    public List<Products> getProducts(){
        return repo.findAll();
    }
    public void postProducts(Products prod){
        repo.save(prod);
    }

    public Products getProductById(int id){
        return repo.findById(id).orElse(null);
    }


    public Products addProduct(Products product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
       return repo.save(product);
    }

    public Products updateProduct(int id, Products product, MultipartFile imageFile) throws IOException {

        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getName());
        product.setImageType(imageFile.getContentType());

        return repo.save(product);
    }

    public void deletProduct(int id) {
         repo.deleteById(id);
    }
    public List<Products> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
