/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.generic;

import com.entities.Account;
import com.entities.Bestelling;
import com.entities.OrderLine;
import com.entities.Size1;
import com.entities.Subscription;
import com.entities.User;
import com.general.DBM;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rob
 */
@Path("orderinfo")
@RequestScoped
public class OrderInfo {
    
    @Context
    private UriInfo context;
    private final DBM dbManager = new DBM();
    InetAddress IP = null;
    
    public OrderInfo() {
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(OrderInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("/getorderinfo")
    public String getOrderInfo(String json) throws JSONException{
        JSONObject jo = new JSONObject();
        List<Bestelling> orders = dbManager.getOrders();
        
        for(Bestelling b : orders){
            JSONObject j = new JSONObject();
            JSONObject order = new JSONObject();
            JSONObject cust = new JSONObject();
            
            order.put("nr",b.getId());
            order.put("sent",b.getSent() == 1 ? true : false);
            int count = 1;
            for(OrderLine ol : b.getOrderLineCollection()){
                JSONObject oline = new JSONObject();
                oline.put("url", "http://"+IP.getHostAddress()+":8080/FotoproducentAPI/images/"+ String.valueOf(ol.getPhotoID().getPhotographerId().getAccountId().getId())+"/lowres/"+ol.getPhotoID().getLocation());
                User photoGrapher = ol.getPhotoID().getPhotographerId();
                String first = photoGrapher.getFirstName();
                String middle = photoGrapher.getMiddleName();
                String last = photoGrapher.getLastName();
                String name = (middle != null && !middle.equals("")) ? first + " " + middle + " " + last : first + " " + last;
                oline.put("photographer",name);
                oline.put("amount", ol.getAmount());
                Size1 s = ol.getSizeID().getSizeID();
                String size = String.valueOf(s.getHeight()) + " x " + String.valueOf(s.getWidth());
                oline.put("measure", size);
                oline.put("piece_price",ol.getSizeID().getPrice());
                order.put("line"+count, oline);
                count++;
            }
            
            User user = b.getUserID();
            cust.put("first_name", user.getFirstName());
            cust.put("middle_name",user.getMiddleName());
            cust.put("last_name",user.getLastName());
            cust.put("street",user.getStreet());
            cust.put("house_number",user.getHouseNumber());
            cust.put("city",user.getCity());
            cust.put("postal_code",user.getPostalCode());
            cust.put("country",user.getCountry());
            Account account = user.getAccountId();
            cust.put("email", account.getUserName());
            cust.put("gender", user.getGender());
            
            j.put("order",order);
            j.put("customer",cust);
            jo.put(b.getId().toString(),j);
        }
        jo.put("result",true);
        return jo.toString();
    }
}
