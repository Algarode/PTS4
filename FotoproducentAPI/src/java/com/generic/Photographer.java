/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.generic;

import com.entities.Album;
import com.entities.Collection;
import com.entities.OrderLine;
import com.entities.Photo;
import com.entities.PhotoAlbum;
import com.entities.Subscription;
import com.entities.User;
import com.general.DBM;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Hafid
 */
@Path("photographer")
@RequestScoped
public class Photographer {
 
    @Context
    private UriInfo context;
    private final DBM dbManager = new DBM();
    JSONObject result = null;
    InetAddress IP = null;
    
    /**
     * Creates a new instance of Photographer
     */
    public Photographer() throws UnknownHostException {
        IP = InetAddress.getLocalHost();
    }


    @POST
    @Produces("application/json")
    @Path("/createalbum")
    public String createAlbum(String json) throws JSONException {
        result = new JSONObject(json);
        
        int uid = result.optInt("uid",-1);
        String name = result.optString("name");
        result = new JSONObject();
        if(uid != -1 && !name.isEmpty()){
            Album newAlbum = dbManager.createAlbum(uid, name);
            if(newAlbum != null){
                result.put("result", true);
                result.put("name", newAlbum.getNaam());
                result.put("token", newAlbum.getId());
                return result.toString();
            }
        }
        
        result.put("result", false);
        return result.toString();
    }
    
    @POST
    @Produces("application/json")
    @Path("/getalbum")
    public String getAlbum(String json)throws JSONException{
        result = new JSONObject(json);
        
        int uid = result.optInt("uid", -1);
        if(uid != -1){
            result = new JSONObject();
            JSONArray jarray = new JSONArray();
            List<Album> data = dbManager.getAllAlbumsByPhotographId(uid);
            
            for(Album a: data){
                HashMap mp = new HashMap();
                mp.put("name", a.getNaam());
                mp.put("amount", dbManager.getAlbumCount(a));
                mp.put("token",a.getId());
                
                jarray.put(mp);
            }
            result.put("array", jarray);
            return result.toString();
        }
        
        
        return JSONReturn.returnfalse();
    }
    
    @POST
    @Produces("application/json")
    @Path("/editalbumname")
    public String editAlbumName(String json)throws JSONException{
        result = new JSONObject(json);
        
        String token = result.optString("token");
        String name = result.optString("name");
        
        if(!token.isEmpty() && !name.isEmpty()){
            if(dbManager.changeAlbumName(token, name))
                return JSONReturn.returntrue();
            else
                return JSONReturn.returnfalse();
        }

        return JSONReturn.returnfalse();
    }
    
    @POST
    @Produces("application/json")
    @Path("/deletealbum")
    public String deleteAlbum(String json)throws JSONException{
        result = new JSONObject(json);
        
        String token = result.optString("token");
        
        if(!token.isEmpty()){
            if(dbManager.deleteAlbum(token))
                return JSONReturn.returntrue();
            else
                return JSONReturn.returnfalse();
        }
        
        return JSONReturn.returnfalse();
    }
    
    @POST
    @Produces("application/json")
    @Path("/getimages")
    public String getImages(String json)throws JSONException{
        result = new JSONObject(json);
        
        int uid = result.optInt("uid", -1);
        String albumid = result.optString("albumid", "");
        
        if(uid != -1){
            List<Photo> colllection;
            if(!albumid.equals("")){
                colllection = dbManager.getAllPhotosFromAlbum(albumid);
            } else
                colllection = dbManager.getAllPhotosFromPhotographer(uid);
            
            
            
            JSONArray jArray = new JSONArray();
            
            for(Photo p: colllection){
                HashMap mp = new HashMap();
                mp.put("id", p.getId());
                mp.put("location", "http://"+IP.getHostAddress()+":8080/FotoproducentAPI/images/"+ String.valueOf(p.getPhotographerId().getAccountId().getId())+"/lowres/"+p.getLocation());
                int amountSold = 0;
                double profit = 0d;
                for(OrderLine ol : p.getOrderLineCollection()){
                    amountSold += ol.getAmount();
                    profit += (ol.getSizeID().getPrice() * ol.getAmount());
                }
                mp.put("sold",amountSold);
                mp.put("profit",profit);
                jArray.put(mp);
            }
            
            return new JSONObject().put("array", jArray).toString();
            
        }
        
        return JSONReturn.returnfalse();
    }
    
    @POST
    @Produces("application/json")
    @Path("/photostoalbum")
    public String putPhotosInAlbum(String json) throws JSONException{
        
        if(!json.isEmpty()){
            JSONObject jsonObject = new JSONObject(json);
            String albumId = jsonObject.optString("albumid");
            int uid = jsonObject.optInt("uid", -1);
            
            if(uid != -1){
                JSONArray jArray = jsonObject.getJSONArray("photos");

                for (int i = 0; i < jArray.length(); i++) {
                    Boolean breturn = dbManager.moveToAlbum(jArray.getString(i), albumId);
                    if(!breturn) return JSONReturn.returnfalse();
                }
                return JSONReturn.returntrue();
            }

        }
        
        
        return JSONReturn.returnfalse();
    }
    
    
    @POST
    @Produces("application/json")
    @Path("/deletephoto")
    public String deletePhoto(String json) throws JSONException{
        
        if(!json.isEmpty()){
            JSONObject obj = new JSONObject(json);
            JSONArray jArray = obj.getJSONArray("array");

            for (int i = 0; i < jArray.length(); i++) {
                Boolean breturn = dbManager.deleteById(Photo.class, jArray.getString(i));
                if(!breturn) return JSONReturn.returnfalse();
            }
            return JSONReturn.returntrue();
        }

        return JSONReturn.returntrue();
    }
    
    @POST
    @Produces("application/json")
    @Path("/getsubscriptions")
    public String getSubscriptions() throws JSONException{
        List<Subscription> subs = dbManager.getSubscriptions();
        JSONObject jo = new JSONObject();
        int count = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        for(Subscription sub : subs){
            JSONObject js = new JSONObject();
            User user = sub.getUserId();
            js.put("id", user.getId());
            String first = user.getFirstName();
            String middle = user.getMiddleName();
            String last = user.getLastName();
            String name = (middle != null && !middle.equals("")) ? first + " " + middle + " " + last : first + " " + last;
            js.put("photographer",name);
            js.put("from_date",format.format(sub.getFromDate()));
            js.put("to_date",format.format(sub.getToDate()));
            js.put("is_active", (sub.getIsActive() == 1) ? true : false);
            jo.put("sub"+String.valueOf(count), js);
            count++;
        }
        
        if(subs.size() > 0)
            jo.put("result", true);
        else
            jo.put("result", false);
        return jo.toString();
    }
    
    @POST
    @Produces("application/json")
    @Path("/setsubscription")
    public String setSubscription(String json) throws JSONException{
        JSONObject jo = new JSONObject(json);
        boolean result = jo.optBoolean("activate");
        int userID = jo.optInt("id");
            User user = dbManager.findById(User.class, userID);
            if(user != null){
                Iterator<Subscription> it = user.getSubscriptionCollection().iterator();
                while(it.hasNext())
                {
                    Subscription sub = it.next();
                    int res = result ? 1 : 0;
                    sub.setIsActive(res);
                    dbManager.save(sub);
                }
                jo.put("result", true);
            }
            else
                jo.put("result", false);
        return jo.toString();
    }
    
}
