package com.uottawa.benjaminmacdonald.quicktings.Classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BenjaminMacDonald on 2017-06-09.
 */

public class ProductItem {

    private int alcoholContent;
    private String description;
    private boolean limitedTimeOffer;
    private int id;
    private int inventoryCount;
    private boolean isDead; //marked dead if removed from LCBO
    private boolean discontinued;
    private boolean isKosher;
    private boolean isSeason;
    private boolean isOCB; //craft beer
    private int saleSaving;
    private String name;
    private String origin;
    private String unitType; //can, bottle ect..
    private int volume;
    private int price; //current retail price
    private String category;
    private String producerName;
    private int regularPrice;
    private String imageUrl;
    private String thumbnailUrl;
    private String tags; //string of words related to product
    private String updated; //last updated


    public ProductItem(JSONObject json) {
        try {
            this.alcoholContent = json.getInt("alcohol_content");
            this.description = json.getString("description");
            this.limitedTimeOffer = json.getBoolean("has_limited_time_offer");
            this.id = json.getInt("id");
            this.inventoryCount = json.getInt("inventory_count");
            this.isDead = json.getBoolean("is_dead");
            this.discontinued = json.getBoolean("is_discontinued");
            this.isKosher = json.getBoolean("is_kosher");
            this.isSeason = json.getBoolean("is_seasonal");
            this.isOCB = json.getBoolean("is_ocb");
            this.saleSaving = json.getInt("limited_time_offer_savings_in_cents");
            this.name = json.getString("name");
            this.origin = json.getString("origin");
            this.unitType = json.getString("package_unit_type");
            this.volume = json.getInt("package_unit_volume_in_milliliters");
            this.price = json.getInt("price_in_cents");
            this.category = json.getString("primary_category");
            this.producerName = json.getString("producer_name");
            this.regularPrice = json.getInt("regular_price_in_cents");
            this.imageUrl = json.getString("image_url");
            this.thumbnailUrl = json.getString("image_thumb_url");
            this.tags = json.getString("tags");
            this.updated = json.getString("updated_at");
        } catch (JSONException e) {
            return;
        }
    }


    public int getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(int alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLimitedTimeOffer() {
        return limitedTimeOffer;
    }

    public void setLimitedTimeOffer(boolean limitedTimeOffer) {
        this.limitedTimeOffer = limitedTimeOffer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    public boolean isKosher() {
        return isKosher;
    }

    public void setKosher(boolean kosher) {
        isKosher = kosher;
    }

    public boolean isSeason() {
        return isSeason;
    }

    public void setSeason(boolean season) {
        isSeason = season;
    }

    public boolean isOCB() {
        return isOCB;
    }

    public void setOCB(boolean OCB) {
        isOCB = OCB;
    }

    public int getSaleSaving() {
        return saleSaving;
    }

    public void setSaleSaving(int saleSaving) {
        this.saleSaving = saleSaving;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public int getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(int regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


}
