package com.example.ebay_search2.ui;

public class SimilarProduct {
    private String title;
    private String galleryURL;
    private String shippingCost;
    private String daysLeft;
    private String price;
    private String itemURL;

    public SimilarProduct(String title, String imageUrl, String shippingCost, String daysLeft, String price, String itemUrl) {
        this.title = title;
        this.galleryURL = imageUrl;
        this.shippingCost = shippingCost;
        this.daysLeft = daysLeft;
        this.price = price;
        this.itemURL = itemUrl;
    }

    public String getTitle() {
        return title;
    }

public String getGalleryURL() {
        return galleryURL;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public String getPrice() {
        return price;
    }

    public String getItemURL() {
        return itemURL;
    }
}
