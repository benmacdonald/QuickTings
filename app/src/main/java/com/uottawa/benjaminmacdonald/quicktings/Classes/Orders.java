package com.uottawa.benjaminmacdonald.quicktings.Classes;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by BenjaminMacDonald on 2017-07-18.
 */

@IgnoreExtraProperties
public class Orders {

    public String order_date;
    public Double longitude;
    public Double latitude;
    public Double order_cost;
    private List<Integer> products;


    public Orders() {
        //this is required idk why
    }

    public Orders(String order_date, Double longitude, Double latitude, Double order_cost) {
        this.order_date = order_date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.order_cost = order_cost;

    }


    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(Double order_cost) {
        this.order_cost = order_cost;
    }

    public List<Integer> getProducts() {
        return products;
    }

    public void setProducts(List<Integer> products) {
        this.products = products;
    }



}
