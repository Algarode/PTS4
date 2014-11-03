/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.generic;

import javax.ws.rs.Path;
import com.entities.*;
import com.general.DBM;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author stefan
 */
@Path("generic")
public class CodeClaim {
    private final DBM dbManager = new DBM();

    public CodeClaim() {
    }

    @POST
    @Consumes("application/json")
    @Path("/claimCode")
    public String claim(String json) throws JSONException {
        JSONObject result = new JSONObject(json);
        String username = result.optString("username");
        String password = result.optString("password");
        Account ac = dbManager.authenicateUser(username, password);
       
        result = new JSONObject();
        if (ac != null){
            result.put("result", true);
            result.put("userID", ac.getId());
            result.put("role",ac.getRoleID());
        } else {
            result.put("result", "false"); 
        }

        return result.toString();
    }
}
