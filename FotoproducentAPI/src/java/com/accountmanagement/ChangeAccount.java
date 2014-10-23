/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accountmanagement;

import com.entities.Account;
import com.entities.User;
import com.entitycontrol.exceptions.NonexistentEntityException;
import com.entitycontrol.exceptions.RollbackFailureException;
import com.general.DBM;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author rob
 */
@Path("account")
public class ChangeAccount {

    @Context
    private UriInfo context;
    private DBM dbManager;
    
    
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("FotoproducentAPIPU");
    EntityManager em = factory.createEntityManager();
        
    
    /**
     * Creates a new instance of ChangeAccount
     */
    public ChangeAccount() {
        dbManager = new DBM();
    }
    
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateUserData(String json) throws JSONException, IOException, NonexistentEntityException, RollbackFailureException, Exception {
        JSONObject jObj = new JSONObject(json);
        try {
            int userId = jObj.getInt("userID");
            String email = jObj.getString("email");
            String password = jObj.getString("password");

            String middleName = jObj.optString("middle_name");
            String lastName = jObj.optString("last_name");
            String street = jObj.optString("street");
            String houseNumber = jObj.optString("house_number");
            String postalCode = jObj.optString("postal_code");
            String city = jObj.optString("city");
            String country = jObj.optString("country");
            
            
            Account account = dbManager.findById(Account.class, userId);
            account.setUserName(email);
            account.setPassword(password);
            User user = account.getUser();
            user.setCity(city);
            user.setCountry(country);
            user.setHouseNumber(houseNumber);
            user.setLastName(lastName);
            user.setPostalCode(postalCode);
            user.setStreet(street);

            dbManager.save(account);

        } catch (Exception ex) {
            Logger.getLogger(ChangeAccount.class.getName()).log(Level.SEVERE, null, ex);
            return jObj.put("result", true).toString();
        }
        jObj = new JSONObject();
        
        return jObj.put("result", true).toString();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("select")
    public String getUserData(String json) { 
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(json);
            Integer userID = Integer.parseInt(jObj.getString("userID"));
            User user = dbManager.findById(User.class, userID);
            
            jObj.put("middle_name",user.getMiddleName());
            jObj.put("last_name",user.getLastName());
            jObj.put("street",user.getStreet());
            jObj.put("house_number",user.getHouseNumber());
            jObj.put("city",user.getCity());
            jObj.put("postal_code",user.getPostalCode());
            jObj.put("country",user.getCountry());
            Account account = user.getAccountId();
            jObj.put("email", account.getUserName());
            
        } catch (JSONException ex) {
            Logger.getLogger(ChangeAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return jObj.toString();
        }
    }
}
