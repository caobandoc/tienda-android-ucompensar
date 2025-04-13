package com.example.compensarshop.core.dto;

public class Product {
    private Long id;
    private String name;
    private double price;
    private String urlImage;

    public Product(Long id, String name, double price, String urlImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
