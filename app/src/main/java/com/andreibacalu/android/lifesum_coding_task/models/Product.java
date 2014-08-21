package com.andreibacalu.android.lifesum_coding_task.models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrei on 18-08-14.
 */
public class Product extends SugarRecord<Product> {
    private int categoryid;
    private double fiber;
    private String headimage;
    private String pcsingram;
    private String brand;
    private double unsaturatedfat;
    private double fat;
    private int servingcategory;
    private int typeofmeasurement;
    private double protein;
    private int defaultserving;
    private double mlingram;
    private int productid;
    private double saturatedfat;
    private String category;
    private boolean verified;
    private String title;
    private String pcstext;
    private double sodium;
    private double carbohydrates;
    private int showonlysametype;
    private int calories;
    private int serving_version;
    private double sugar;
    private int measurementid;
    private double cholesterol;
    private double gramsperserving;
    private int showmeasurement;
    private double potassium;

    public Product() {}

    public Product(int serving_version, int categoryid, double fiber, String headimage, String pcsingram,
                   String brand, double unsaturatedfat, double fat, int servingcategory, int typeofmeasurement,
                   double protein, int defaultserving, double mlingram, int id, double saturatedfat,
                   String category, boolean verified, String title, String pcstext, double sodium,
                   double carbohydrates, int showonlysametype, int calories, double sugar, int measurementid,
                   double cholesterol, double gramsperserving, int showmeasurement, double potassium) {
        this.serving_version = serving_version;
        this.categoryid = categoryid;
        this.fiber = fiber;
        this.headimage = headimage;
        this.pcsingram = pcsingram;
        this.brand = brand;
        this.unsaturatedfat = unsaturatedfat;
        this.fat = fat;
        this.servingcategory = servingcategory;
        this.typeofmeasurement = typeofmeasurement;
        this.protein = protein;
        this.defaultserving = defaultserving;
        this.mlingram = mlingram;
        this.productid = id;
        this.saturatedfat = saturatedfat;
        this.category = category;
        this.verified = verified;
        this.title = title;
        this.pcstext = pcstext;
        this.sodium = sodium;
        this.carbohydrates = carbohydrates;
        this.showonlysametype = showonlysametype;
        this.calories = calories;
        this.sugar = sugar;
        this.measurementid = measurementid;
        this.cholesterol = cholesterol;
        this.gramsperserving = gramsperserving;
        this.showmeasurement = showmeasurement;
        this.potassium = potassium;
    }

    public Product(JSONObject jsonObject) throws JSONException {
        this(jsonObject.getInt("serving_version"), jsonObject.getInt("categoryid"), jsonObject.getDouble("fiber"), jsonObject.getString("headimage"), jsonObject.getString("pcsingram"),
                jsonObject.getString("brand"), jsonObject.getDouble("unsaturatedfat"), jsonObject.getDouble("fat"), jsonObject.getInt("servingcategory"), jsonObject.getInt("typeofmeasurement"),
                jsonObject.getDouble("protein"), jsonObject.getInt("defaultserving"), jsonObject.getDouble("mlingram"), jsonObject.getInt("id"), jsonObject.getDouble("saturatedfat"),
                jsonObject.getString("category"), jsonObject.getBoolean("verified"), jsonObject.getString("title"), jsonObject.getString("pcstext"), jsonObject.getDouble("sodium"),
                jsonObject.getDouble("carbohydrates"), jsonObject.getInt("showonlysametype"), jsonObject.getInt("calories"), jsonObject.getDouble("sugar"), jsonObject.getInt("measurementid"),
                jsonObject.getDouble("cholesterol"), jsonObject.getDouble("gramsperserving"), jsonObject.getInt("showmeasurement"), jsonObject.getDouble("potassium"));
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getPcsingram() {
        return pcsingram;
    }

    public void setPcsingram(String pcsingram) {
        this.pcsingram = pcsingram;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getUnsaturatedfat() {
        return unsaturatedfat;
    }

    public void setUnsaturatedfat(double unsaturatedfat) {
        this.unsaturatedfat = unsaturatedfat;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public int getServingcategory() {
        return servingcategory;
    }

    public void setServingcategory(int servingcategory) {
        this.servingcategory = servingcategory;
    }

    public int getTypeofmeasurement() {
        return typeofmeasurement;
    }

    public void setTypeofmeasurement(int typeofmeasurement) {
        this.typeofmeasurement = typeofmeasurement;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public int getDefaultserving() {
        return defaultserving;
    }

    public void setDefaultserving(int defaultserving) {
        this.defaultserving = defaultserving;
    }

    public double getMlingram() {
        return mlingram;
    }

    public void setMlingram(double mlingram) {
        this.mlingram = mlingram;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int id) {
        this.productid = id;
    }

    public double getSaturatedfat() {
        return saturatedfat;
    }

    public void setSaturatedfat(double saturatedfat) {
        this.saturatedfat = saturatedfat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPcstext() {
        return pcstext;
    }

    public void setPcstext(String pcstext) {
        this.pcstext = pcstext;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getShowonlysametype() {
        return showonlysametype;
    }

    public void setShowonlysametype(int showonlysametype) {
        this.showonlysametype = showonlysametype;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getServing_version() {
        return serving_version;
    }

    public void setServing_version(int serving_version) {
        this.serving_version = serving_version;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public int getMeasurementid() {
        return measurementid;
    }

    public void setMeasurementid(int measurementid) {
        this.measurementid = measurementid;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getGramsperserving() {
        return gramsperserving;
    }

    public void setGramsperserving(double gramsperserving) {
        this.gramsperserving = gramsperserving;
    }

    public int getShowmeasurement() {
        return showmeasurement;
    }

    public void setShowmeasurement(int showmeasurement) {
        this.showmeasurement = showmeasurement;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return categoryid == product.getCategoryid() && productid == product.getProductid();
    }

    public String getHumanReadableString() {
        // TODO: maybe add new info
        return new StringBuilder().append("Product: ").append(title).append("\n").append("Category: ").append(category).toString();
    }
}
