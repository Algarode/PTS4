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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("FotoproducentAPIPU2");
    private final EntityManager em = factory.createEntityManager();
    
    
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
    @Consumes(MediaType.APPLICATION_JSON)
    public void uploadImage(String Json) throws JSONException, IOException {
        
        JSONObject jObj = new JSONObject(Json);
        
        String hexString = jObj.getString("image");
        String extension = jObj.getString("extension");
        String imageName = jObj.getString("name");
        Integer userId = jObj.getInt("userID");
        
        String location = SaveHexImage(hexString, extension, imageName, userId);
        
        
        
    
    }
    
    
    private String SaveHexImage(String hex, String extension,
            String name, Integer userId) throws IOException{
        if(hex.isEmpty())
            return null;
        
        String location = "c:/"+userId+"/" +name+"."+extension;
        
        byte[] barr = DatatypeConverter.parseHexBinary(hex);
        
        InputStream in = new ByteArrayInputStream(barr);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        //Original sized
        ImageIO.write(bImageFromConvert, extension , new File(location));    
        //Lowres 500 x 500 px version
        bImageFromConvert.getGraphics().drawImage(bImageFromConvert, 500, 500, null);
        
        return location;
    }
}
