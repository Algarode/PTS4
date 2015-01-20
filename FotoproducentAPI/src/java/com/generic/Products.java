package com.generic;

import com.entities.Product;
import com.general.DBM;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Edwin
 */
@Path("products")
public class Products {
    
    private final DBM dbm = new DBM();
    private InetAddress IP = null;
    
    public Products() throws UnknownHostException { IP = InetAddress.getLocalHost(); }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getProducts")
    public String getProducts () throws JSONException {
        JSONObject jObj = null;
        
        try {
            List<Product> collection = dbm.getProductCollection();
            jObj = new JSONObject();
            
            if (!collection.isEmpty()) {
                for (Product p : collection) {
                    JSONObject pObj = new JSONObject();
                    
                    pObj.put("id", p.getId());
                    pObj.put("name", p.getName());
                    //pObj.put("location", "http://" + IP.getHostAddress() + ":8080/FotoproducentAPI/images/products/" + p.getName());
                    pObj.put("location", "http://145.93.81.11:8080/FotoproducentAPI/images/products/" + p.getName());
                    pObj.put("price", p.getPrice());
                    
                    jObj.put(p.getId().toString(), pObj);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return jObj.toString();
    }
    
}
