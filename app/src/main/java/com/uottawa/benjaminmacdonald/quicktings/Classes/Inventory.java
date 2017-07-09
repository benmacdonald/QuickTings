package com.uottawa.benjaminmacdonald.quicktings.Classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BenjaminMacDonald on 2017-07-08.
 */

public class Inventory {

    private int productId;
    private int storeId;
    private int quantity;
    private String updated_on;



    public Inventory(JSONObject json) {
        try {
            this.productId = json.getInt("product_id");
            this.storeId = json.getInt("store_id");
            this.quantity = json.getInt("quantity");
            this.updated_on = json.getString("updated_on");
        } catch (JSONException e) {
            throw new NullPointerException();
        }
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

}
