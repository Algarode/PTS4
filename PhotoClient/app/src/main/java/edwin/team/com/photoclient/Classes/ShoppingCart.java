package edwin.team.com.photoclient.Classes;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart implements Serializable {

    // Key = photoID
    private HashMap<Integer,OrderLine> orders;
    private Double tax, totalPriceExTax = 0.00;

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

    public void addOrderLine(Integer photoID, Double price, Integer sizeID, Integer amount, Drawable image, String name){
        this.orders.put(photoID,new OrderLine(photoID,price,sizeID,amount, image, name));
        this.totalPriceExTax += (price * amount);
    }

    public void deleteOrderLine(Integer photoID){
        OrderLine order = orders.get(photoID);
        this.totalPriceExTax -= order.getTotalPrice();
        orders.remove(photoID);
    }

    public List<OrderLine> getOrders(){
        return new ArrayList<OrderLine>(orders.values());
    }

}
