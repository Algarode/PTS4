/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.generic;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service THIS IS JUST AN EXAMPLE!!!!!!!!
 *
 * @author Hafid
 */
@Path("generic")
public class Auth {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public Auth() {
    }

    /**
     * Retrieves representation of an instance of com.generic.Auth
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("/login/{param}")
    public String Login(@PathParam("param") String username) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("username", username);

        
        
        return obj.toString();
    }

    /**
     * PUT method for updating or creating an instance of Auth
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}