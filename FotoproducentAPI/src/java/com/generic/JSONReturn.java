/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.generic;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Hafid
 */
public class JSONReturn {
    
    static JSONObject jsonObject;
    
    public static String returnfalse() 
    {
        jsonObject = new JSONObject();
        try {  
            jsonObject.put("result", false);

        } catch (JSONException ex) {
            Logger.getLogger(JSONReturn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject.toString();
    }
    
    public static String returntrue() 
    {
        jsonObject = new JSONObject();
        try {  
            jsonObject.put("result", true);

        } catch (JSONException ex) {
            Logger.getLogger(JSONReturn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonObject.toString();
    }
}
