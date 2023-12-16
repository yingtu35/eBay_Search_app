package com.example.ebay_search2.ui;

import java.util.PrimitiveIterator;

public class Product {
    private String itemId;
    private String title;
    private String galleryURL;
    private String viewItemURL;
    private String postalCode;
    private String shippingCost;
    private String currentPrice;
    private String condition;
    private Boolean isWishListed;
    private String allInfo;

    public Product(String itemId, String title, String galleryURL, String viewItemURL,
                   String postalCode, String shippingCost, String currentPrice, String condition, Boolean isWishListed, String allInfo) {
        this.itemId = itemId;
        this.title = title;
        this.galleryURL = galleryURL;
        this.viewItemURL = viewItemURL;
        this.postalCode = postalCode;
        this.shippingCost = shippingCost;
        this.currentPrice = currentPrice;
        this.condition = condition;
        this.isWishListed = isWishListed;
        this.allInfo = allInfo;
    }

    public String getTitle(){ return title; }
    public String getGalleryURL(){return galleryURL; }

    public String getPostalCode(){ return postalCode;}
    public String getShippingCost(){ return shippingCost; }
    public String getCondition(){ return condition; }
    public String getCurrentPrice(){ return currentPrice; }

    public String getViewItemURL(){ return viewItemURL; }
    public String getItemId(){return itemId;}

    public Boolean getIsWishListed() {
        return isWishListed;
    }

    public String getAllInfo() {
        return allInfo;
    }

    public void setIsWishListed(Boolean wishListed) {
        isWishListed = wishListed;
    }

    public String toString() {
        return "Product{" +
                "itemId='" + itemId + '\'' +
                ", title='" + title + '\'' +
                ", galleryURL='" + galleryURL + '\'' +
                ", viewItemURL='" + viewItemURL + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", shippingCost='" + shippingCost + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }


}
