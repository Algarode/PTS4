package edwin.team.com.photoclient.Classes;


import android.graphics.Bitmap;

import java.io.Serializable;

public class OrderLine implements Serializable{
    private Integer  sizeID, amount;
    String photoID;
    private Double price;
    private Bitmap image;
    private String name;

    public OrderLine(String photoID, Double price, Integer sizeID, Integer amount, Bitmap image, String name){
        this.photoID = photoID;
        this.price = price;
        this.sizeID = sizeID;
        this.amount = amount;
        this.image = image;
        this.name = name;
    }

    public Double getTotalPrice(){
        return (this.price * this.amount);
    }

    public Double getUnitPrice(){
        return this.price;
    }

    public Integer getAmount(){
        return this.amount;
    }

    public Bitmap getImage() { return this.image; }

    public String getName() { return this.name; }

    public Integer getSizeID() { return this.sizeID; }

    public String getPhotoID() { return this.photoID; }

    public void setSize(Integer sizeID){
        this.sizeID = sizeID;
    }

    public void setAmount(Integer amount){
        this.amount = amount;
    }
}
