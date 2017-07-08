package com.uottawa.benjaminmacdonald.quicktings.Classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BenjaminMacDonald on 2017-07-07.
 */

public class Store {

    private int id;
    private String address_line_1;
    private Double latitude;
    private Double longitude;
    private String name;
    private String postalCode;
    private String telephone;




    public Store(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.address_line_1 = json.getString("address_line_1");
            this.latitude = json.getDouble("latitude");
            this.longitude = json.getDouble("longitude");
            this.name = json.getString("name");
            this.postalCode = json.getString("postal_code");
            this.telephone = json.getString("telephone");
        } catch (JSONException e) {
            throw new NullPointerException();
        }
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
