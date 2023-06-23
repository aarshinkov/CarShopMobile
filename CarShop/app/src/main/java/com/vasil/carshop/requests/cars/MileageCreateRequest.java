package com.vasil.carshop.requests.cars;

import java.io.Serializable;

public class MileageCreateRequest implements Serializable {

    private Double mileage;
    private String unit;

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
