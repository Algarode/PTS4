package edwin.team.com.photoclient.Classes;


import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class OrderLine implements Serializable{
    private Integer photoID, sizeID, amount;
    private Double price;
    private Drawable image;
    private String name;

    public OrderLine(Integer photoID, Double price, Integer sizeID, Integer amount, Drawable image, String name){
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

    public Drawable getImage() { return this.image; }

    public String getName() { return this.name; }

    public void setSize(Integer sizeID){
        this.sizeID = sizeID;
    }

    public void setAmount(Integer amount){
        this.amount = amount;
    }
}
