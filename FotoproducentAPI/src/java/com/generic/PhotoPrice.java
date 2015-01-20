/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.generic;
import com.entities.Size1;
import com.entities.SizePrize;
import com.entities.User;
import com.general.DBM;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rob
 */

@Path("photoprice")
public class PhotoPrice {
    
    private final DBM dbManager = new DBM();
    
    @POST
    @Consumes("application/json")
    @Path("/getprices")
    public String getPrices(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        int pid = jo.optInt("pid");
        User user = dbManager.getUserbyAccountId(pid);
        Collection<SizePrize> prices = user.getSizePrizeCollection();
        if(prices.size() == 4){
            JSONObject jobj = new JSONObject();
            for(SizePrize sp : prices){
                JSONObject sizePrice = new JSONObject();
                Size1 s = sp.getSizeID();
                sizePrice.put("height", s.getHeight());
                sizePrice.put("width", s.getWidth());
                sizePrice.put("price", sp.getPrice());
                jobj.put(s.getId().toString(),sizePrice);
            }
            return jobj.toString();
        }
        else{
            JSONObject jobj = new JSONObject();
            for(int i = 1; i <= 4; i++){
                JSONObject sizePrice = new JSONObject();
                Size1 s = dbManager.findById(Size1.class, i);
                sizePrice.put("height", s.getHeight());
                sizePrice.put("width", s.getWidth());
                sizePrice.put("price", s.getPrice());
                jobj.put(s.getId().toString(),sizePrice);
            }
            return jobj.toString();
        }
    }
    
    @POST
    @Consumes("application/json")
    @Path("/setprices")
    public String setPrices(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        int pid = jo.optInt("pid");
        User user = dbManager.getUserbyAccountId(pid);
        Collection<SizePrize> prices = user.getSizePrizeCollection();
        if(prices.size() == 4){
            for(SizePrize sp : prices){
                Double price = jo.optDouble(sp.getSizeID().getId().toString());
                sp.setPrice(price);
                dbManager.save(sp);
            }
            jo.put("result", true);
            return jo.toString();
        }
        else{
            for(int i = 1; i <= 4; i++){
                Double price = jo.optDouble(String.valueOf(i));
                SizePrize sp = new SizePrize();
                sp.setPhotographerID(user);
                sp.setPrice(price);
                sp.setSizeID(dbManager.findById(Size1.class, i));
                dbManager.save(sp);
            }
            jo.put("result", true);
            return jo.toString();
        }
    }
}
