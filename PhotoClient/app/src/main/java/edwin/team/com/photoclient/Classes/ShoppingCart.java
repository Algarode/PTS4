package edwin.team.com.photoclient.Classes;

import java.io.Serializable;
import java.util.HashMap;

public class ShoppingCart implements Serializable {

    // Key = photoID
    private HashMap<Integer,OrderLine> orders;
    private Double tax, totalPriceExTax;

    public ShoppingCart(){
        this.orders = new HashMap<Integer, OrderLine>();
    }

    public Double getTotalPriceExTax(){
        return this.totalPriceExTax;
    }

    public Double getTax(){
        calculateTax();
        return this.tax;
    }

    public Double getTotalPriceIncTax(){
        return this.totalPriceExTax + this.tax;
    }

    private void calculateTax(){
        this.tax = (totalPriceExTax * 1.19) - totalPriceExTax; //Btw moet van server worden opgehaald
    }

    public void changeAmount(Integer photoID, Integer amount){
        OrderLine order = orders.get(photoID);
        this.totalPriceExTax -= order.getTotalPrice();
        order.setAmount(amount);
        this.totalPriceExTax += order.getTotalPrice();
    }

    public void changeSize(Integer photoID, Integer sizeID){
        //hier moeten we nog even overleggen hoe we de kosten van een size op gaan halen. a.s. dinsdag zullen we dit doen
    }

    public void addOrderLine(Integer photoID, Double price, Integer sizeID, Integer amount){
        OrderLine order = new OrderLine(photoID,price,sizeID,amount);
        this.totalPriceExTax += (price * amount);
    }

    public void deleteOrderLine(Integer photoID){
        OrderLine order = orders.get(photoID);
        this.totalPriceExTax -= order.getTotalPrice();
        orders.remove(photoID);
    }

}
