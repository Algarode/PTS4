/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package upload;

import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.mail.Multipart;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Hafid
 */
@MultipartConfig
@Path("upload")
public class UploadResource extends HttpServlet{ 

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UpploadResource
     */
    public UploadResource() {
    }

    /**
     * Retrieves representation of an instance of upload.UpploadResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/gett")
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        
        return "text";
    }

    @POST
    @Path("image")
    @Consumes("multipart/form-data")
    public String uploadImage(@FormDataParam("file") InputStream file1) throws FileNotFoundException, IOException, JSONException {
        
        FileInputStream fis = new FileInputStream("C:\\Users\\Hafid\\Pictures\\7PoqEkf.jpg");
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        IOUtils.copy(fis, buffer);
        byte [] data = buffer.toByteArray();
        String s = Base64.encode(data);
        String hex = Hex.encodeHex(data).toString();
        JSONObject ob = new JSONObject();
        ob.put("image", s);
        ob.put("name", "hafid");
        
        
    return ob.toString();
    }
    
       
    @POST
    @Path("/testing") 
    @Consumes(MediaType.APPLICATION_JSON)
    public String testing(String json) throws JSONException, FileNotFoundException, IOException {
                
        JSONObject obj = new JSONObject(json);
        

        return obj.toString();
    }
    
    
} 
