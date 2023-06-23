package com.vasil.carshop.requests.cars;

import java.io.Serializable;

public class CarCreateRequest implements Serializable {

    private String brand;
    private String model;
    private Double price;
    private Integer year;
    private Integer engineType;
    private Integer horsePowers;
    private Integer gearbox;
    private Integer category;
    private MileageCreateRequest mileage;
    private String color;
    private String userId;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getEngineType() {
        return engineType;
    }

    public void setEngineType(Integer engineType) {
        this.engineType = engineType;
    }

    public Integer getHorsePowers() {
        return horsePowers;
    }

    public void setHorsePowers(Integer horsePowers) {
        this.horsePowers = horsePowers;
    }

    public Integer getGearbox() {
        return gearbox;
    }

    public void setGearbox(Integer gearbox) {
        this.gearbox = gearbox;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public MileageCreateRequest getMileage() {
        return mileage;
    }

    public void setMileage(MileageCreateRequest mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
