/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic;

import com.entities.*;
import com.general.DBM;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author stefan
 */
@Path("generic")
public class CodeClaim {
    private final DBM dbManager = new DBM();
    InetAddress IP = null;
    
    public CodeClaim() throws UnknownHostException {
        IP = InetAddress.getLocalHost();
    }

    @POST
    @Consumes("application/json")
    @Path("/claimCode")
    public String claim(String json) throws JSONException {
        JSONObject result = new JSONObject(json);
        String code = result.optString("code").toLowerCase();
        String uid = result.optString("uid");
        //Account ac = dbManager.authenicateUser(username, password);
       
        result = new JSONObject();
        if (code.startsWith("p")){ //Single Picture
            Collection ps = dbManager.verifyCode(uid, code);
            if (ps == null){
                result.put("result", "false");
                result.put("errormsg", "code failure");
            }
            else  {result.put("result", "true"); }
        }// Album
        else if(code.startsWith("a")){
            List<Collection> ps = dbManager.verifyCodeAlbum(uid, code);
            if (ps == null){
                result.put("result", "false");
                result.put("errormsg", "code Failure");
            } else  {result.put("result", "true"); }
        }//geen valide code
        else {
            result.put("result", "false");
            result.put("errormsg", "no valid code");
        }

        return result.toString();
    }
    
    
    @POST
    @Consumes("application/json")
    @Path("/getCollection")
    public String Collection(String json){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            
            
            int accountID = obj.getInt("uid");
            
            List<Collection> coll = dbManager.getCollectionByUserId(accountID);
            JSONArray jArray = new JSONArray();
            
            obj = new JSONObject();
            
            int length = 0;
            for(Collection c: coll){
                Photo p = c.getPhotoID();
                JSONObject jObj = new JSONObject();
                if(p != null){
                    //jObj.put("location", "http://"+IP.getHostAddress()+":8080/FotoproducentAPI/images/"+ String.valueOf(p.getPhotographerId().getAccountId().getId())
                            //+"/lowres/"+p.getLocation());
                    jObj.put("location", "http://145.93.81.11:8080/FotoproducentAPI/images/" + String.valueOf(p.getPhotographerId().getAccountId().getId()) + "/lowres/" + p.getLocation());
                    jObj.put("name", p.getName());
                    jObj.put("id",p.getId());
                    
                    JSONObject sizeObject = new JSONObject();
                    User photographer = p.getPhotographerId();
                    for(SizePrize sp : photographer.getSizePrizeCollection()){
                        JSONObject sizePriceObject = new JSONObject();
                        Size1 sObj = sp.getSizeID();
                        sizePriceObject.put("height", sObj.getHeight());
                        sizePriceObject.put("width", sObj.getWidth());
                        sizePriceObject.put("price", sp.getPrice());
                        sizePriceObject.put("sizePriceID", sp.getId());
                        sizeObject.put("price"+sObj.getId().toString(), sizePriceObject);
                    }
                    jObj.put("price",sizeObject);
                    obj.put("obj_"+String.valueOf(length), jObj);
                    
                    length++;
                } else{
                    for(PhotoAlbum pa: c.getAlbumID().getPhotoAlbumCollection()){
                        Photo paPhoto = pa.getPhotoId();
                        
                        
                        jObj.put("location", paPhoto.getLocation());
                        jObj.put("name", paPhoto.getName());
                        jObj.put("id",pa.getId());
                        obj.put("obj_"+String.valueOf(length), jObj);
                        
                      //  Size1 size = sp.getSizeID();
                    
//                        jObj.put("width", size.getWidth());
//                        jObj.put("height", size.getHeight());
//                        jObj.put("price", size.getPrice());
                        
                        length++;
                    }
                }
                
            }
        } catch (JSONException ex) {
            Logger.getLogger(CodeClaim.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj.toString();
    }  
}
