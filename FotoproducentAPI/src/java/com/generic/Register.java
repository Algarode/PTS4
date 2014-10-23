package com.generic;

import com.entities.Account;
import com.entities.Roles;
import com.entities.User;
import com.general.DBM;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Edwin
 */
@Path("register")
public class Register {
    private final DBM dbManager = new DBM();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Register
     */
    public Register() {
    }


    @POST
    @Path("user")
    @Consumes("application/json")
    @Produces("application/json")
    public String user(String content) throws JSONException {
        JSONObject jReturn = new JSONObject();
        jReturn.put("result", false);
        
        
        JSONObject jObj = new JSONObject(content);
        
        String email = jObj.getString("email");
        String password = jObj.getString("password");
        
        String firstName = jObj.getString("naam");
        String middleName = jObj.getString("tussenvoegsel");
        String lastName = jObj.getString("achternaam");
        String gender = jObj.getString("geslacht");
        String street = jObj.getString("straat");
        String houseNumber = jObj.getString("huisnummer");
        String postalCode = jObj.getString("postcode");
        String city = jObj.getString("stad");
        String country = jObj.getString("land");
        Boolean isPhotographer = jObj.getBoolean("fotograaf");
        
        if (email != "" && password != "" && firstName != "" && lastName != "" && gender != "" && street != "" && houseNumber != "" && postalCode != "" && city != "" && country!= "") {
            if (!email.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
                return jReturn.put("error", "E-mail is wrong").toString();
            } else if (!firstName.matches("[a-zA-Z]*")) {
                return jReturn.put("error", "Firstname is wrong").toString();
            } else if (!middleName.matches("")) {
                if (!middleName.matches("^[a-zA-Z ]+(([\\'\\,\\.\\s-][a-zA-Z\\s])?[a-zA-Z])+$")) {
                    return jReturn.put("error", "Middlename is wrong").toString();
                }
            } else if (!lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
                return jReturn.put("error", "lastname is wrong").toString();
            } else if (!street.matches("^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?)$")) {
                return jReturn.put("error", "Street is wrong").toString();
            } else if (!houseNumber.matches("^[0-9]+[a-zA-Z]+|[0-9]+$")) {
                return jReturn.put("error", "Housenumber is wrong").toString();
            } else if (!postalCode.matches("^[0-9]{4}[ ]?[a-z|A-Z]{2}$")) {
                return jReturn.put("error", "Zipcode is wrong").toString();
            } else if (!city.matches("^(([2][e][[:space:]]|['][ts][-[:space:]]))?[ëéÉËa-zA-Z]{2,}((\\s|[-](\\s)?)[ëéÉËa-zA-Z]{2,})*$")) {
                return jReturn.put("error", "City is wrong").toString();
            } else {
                // Account object maken
                Account account = new Account();
                // Velden setten
                account.setUserName(email);
                account.setPassword(password);
               // dbManager.save(account);
                
                
                
                // User object maken
                User user = new User();
                // Velden setten
                user.setFirstName(firstName);
                user.setMiddleName(middleName);
                user.setLastName(lastName);
                user.setGender(gender);
                user.setStreet(street);
                user.setHouseNumber(houseNumber);
                user.setPostalCode(postalCode);
                user.setCity(city);
                user.setCountry(country);
                if(isPhotographer){ account.setRoleID(new Roles(2));} else { account.setRoleID(new Roles(3)); }
                user.setAccountId(account);
                account.setUser(user);
                
                dbManager.save(account);
            }
        }
        return jReturn.put("result", true).toString();
    }
}