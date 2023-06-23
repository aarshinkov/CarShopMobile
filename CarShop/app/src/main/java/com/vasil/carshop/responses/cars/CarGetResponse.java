package com.vasil.carshop.responses.cars;

import com.vasil.carshop.responses.users.UserGetResponse;

import java.io.Serializable;
import java.sql.Timestamp;

public class CarGetResponse implements Serializable {

    private String carId;
    private String brand;
    private String model;
    private Integer year;
    private Integer horsePowers;
    private Double price;
    private Integer category;
    private Integer engineType;
    private Integer gearbox;
    private MileageGetResponse mileage;
    private String color;
    private Timestamp addedOn;
    private UserGetResponse owner;

    public boolean isUpdated(CarGetResponse currentData) {

        if (this == currentData) return false;

        if (!this.brand.equals(currentData.getBrand())) return true;
        if (!this.model.equals(currentData.getModel())) return true;
        if (!this.year.equals(currentData.getYear())) return true;
        if (!this.horsePowers.equals(currentData.getHorsePowers())) return true;
        if (!this.price.equals(currentData.getPrice())) return true;
        if (!this.category.equals(currentData.getCategory())) return true;
        if (!this.engineType.equals(currentData.getEngineType())) return true;
        if (!this.gearbox.equals(currentData.getGearbox())) return true;
        if (!this.mileage.getMileage().equals(currentData.getMileage().getMileage())) return true;
        if (!this.mileage.getUnit().equals(currentData.getMileage().getUnit())) return true;
        if (!this.color.equals(currentData.getColor())) return true;

        return !this.owner.getUserId().equals(currentData.getOwner().getUserId());
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getHorsePowers() {
        return horsePowers;
    }

    public void setHorsePowers(Integer horsePowers) {
        this.horsePowers = horsePowers;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getEngineType() {
        return engineType;
    }

    public void setEngineType(Integer engineType) {
        this.engineType = engineType;
    }

    public Integer getGearbox() {
        return gearbox;
    }

    public void setGearbox(Integer gearbox) {
        this.gearbox = gearbox;
    }

    public MileageGetResponse getMileage() {
        return mileage;
    }

    public void setMileage(MileageGetResponse mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Timestamp getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Timestamp addedOn) {
        this.addedOn = addedOn;
    }

    public UserGetResponse getOwner() {
        return owner;
    }

    public void setOwner(UserGetResponse owner) {
        this.owner = owner;
    }
}
