package edwin.team.com.photoclient.Classes;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Hafid on 28-10-2014.
 */
public class ImageCollection implements Serializable {

    private String imageUrl;
    private byte[] imageByte;
    private String id;
    private ArrayList<Price> prices;
    private Integer sizePos;
    private Integer amount;
    NumberFormat format = NumberFormat.getCurrencyInstance();

    public ImageCollection (String imageUrl, String id,ArrayList<Price> prices) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.prices = prices;
        this.amount = 1;
        this.sizePos = 0;
    }

    public void setImageByte(byte[] imageByte){
        this.imageByte = imageByte;
    }

    public byte[] getImageByte(){ return this.imageByte; }

    public String getImageUrl(){    return  this.imageUrl;}

    public String getId(){
        return  this.id;
    }

    public double getStartPrice(){ return (this.prices.get(sizePos).getPrice() * amount); }

    public ArrayList<String> getSizeAdapter(){
        ArrayList<String> priceAdapter = new ArrayList<String>();
        for(Price p : this.prices){
            priceAdapter.add(String.valueOf(p.getHeight()) + " x " + String.valueOf(p.getWidth()) + " " + format.format(p.getPrice()) + "p/stuk");
        }
        return priceAdapter;
    }

    public Integer getAmount(){ return this.amount;}

    public void setAmount(int amount){
        this.amount = amount;
    }

    public void setSize(int size){
        this.sizePos = size;
    }

    public Integer getSizeID(){
        return this.sizePos;
    }

    public Integer getSizePriceID(){return this.prices.get(sizePos).getSizepriceId();}

    public Double getPrice(){ return this.prices.get(sizePos).getPrice();}

    public String getSizeName(){
        Price price = this.prices.get(sizePos);
        return String.valueOf(price.getHeight()) + " x " + String.valueOf(price.getWidth());
    }
}
