package com.generic;

import com.entities.*;
import com.general.DBM;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
/**
 * REST Web Service
 *
 * @author Hafid
 */
@Path("generic")
public class Auth {
    private final DBM dbManager = new DBM();

    public Auth() {
    }

    @POST
    @Consumes("application/json")
    @Path("/login")
    public String Login(String json) throws JSONException {
        JSONObject result = new JSONObject(json);
        String username = result.optString("username");
        String password = result.optString("password");
        Account ac = dbManager.authenicateUser(username, password);
       
        result = new JSONObject();
        if (ac != null){
            result.put("result", true);
            result.put("userID", ac.getId());
            Roles role = ac.getRoleID();
            result.put("role",role.getId());
        } else {
            result.put("result", "false"); 
            Product pr = new Product();
        }

        return result.toString();
    }
}
