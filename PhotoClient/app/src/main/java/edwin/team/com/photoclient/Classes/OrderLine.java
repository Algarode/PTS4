package edwin.team.com.photoclient.Classes;


import java.io.Serializable;

public class OrderLine implements Serializable{
    private Integer photoID, sizeID, amount;
    private Double price;

    public OrderLine(Integer photoID, Double price, Integer sizeID, Integer amount){
        this.photoID = photoID;
        this.price = price;
        this.sizeID = sizeID;
        this.amount = amount;
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

    public void setSize(Integer sizeID){
        this.sizeID = sizeID;
    }

    public void setAmount(Integer amount){
        this.amount = amount;
    }
}
