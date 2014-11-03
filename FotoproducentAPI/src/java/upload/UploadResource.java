/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package upload;

import com.entities.Collection;
import com.entities.Photo;
import com.entities.User;
import com.general.DBM;
import com.generic.Hashids;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
    
    String FOLDER_PATH = "c:/_photos/";
    String ORIGIN = "origin";
    String LOWRES = "lowres";
    int USERID = 0;
    private final DBM dbManager;
    
    
    @Context
    private UriInfo context;

    public UploadResource() {
        dbManager = new DBM();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getunique")
    public String getUniqueId(String album) throws Exception
    {
        JSONObject j = null;
        Hashids hashids = null;
        try{
            j = new JSONObject(album);
            hashids = new Hashids("teamEdwin");
        }catch(Exception ex){
            return new JSONObject().put("result", false).toString();
        }
        
        String prefix = j.getString("type"); 
        
        return new JSONObject().put("id",prefix + hashids.encode(System.currentTimeMillis()).toLowerCase()).put("result", true).toString();
    }
      
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/uploadImage")
    public String uploadimages(@Context HttpServletRequest request) throws JSONException{
        if(ServletFileUpload.isMultipartContent(request)){
            try
            {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);

                if(items.size() > 2)
                {
                    String token = items.get(0).getString();
                    int PhotographId = Integer.valueOf(items.get(1).getString());
                    items.remove(0);//token
                    items.remove(0);//userId
                    
                    CreateFolderIfNotExists(PhotographId);
                    for(FileItem fi : items){ 
                        SaveImages(fi);
                        SaveToDatabase(token, fi.getName(), PhotographId);
                    }
                }
                
                return new JSONObject().put("result", true).toString();
            }catch(Exception e){
                return new JSONObject().put("result", false).toString();
            }
        }
        return new JSONObject().put("result", false).toString();
    }

    private void SaveToDatabase(String token, String name, int PhotographId){
        Photo p = new Photo();
        p.setId(token);
        p.setLocation(name);
        p.setName(name);
        p.setPhotographerId(dbManager.findById(User.class, PhotographId));

        dbManager.save(p);
    }
    
    private void CreateFolderIfNotExists(int userId){
        USERID = userId;
        File userFolder = new File(FOLDER_PATH+USERID);
        File originFolder = new File(FOLDER_PATH+USERID+"/"+ORIGIN);
        File lowresFolder = new File(FOLDER_PATH+USERID+"/"+LOWRES);
        
        if(!userFolder.exists())    {   userFolder.mkdir();     }
        if(!originFolder.exists())  {   originFolder.mkdir();   }
        if(!lowresFolder.exists())  {   lowresFolder.mkdir();   }
    }
    
    private boolean SaveImages(FileItem fitem)
    {
        try{
            String extension = fitem.getContentType().split("/")[1];

            File file = new File(FOLDER_PATH+USERID+"/"+ORIGIN+"/"+fitem.getName());
            fitem.write(file);

            BufferedImage bufferedImage = ImageIO.read(file);

            BufferedImage resizedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_SMOOTH);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0,bufferedImage.getWidth(), bufferedImage.getHeight(),null);
            g.dispose();

            ImageIO.write(resizedImage, "jpg" , new File(FOLDER_PATH+USERID+"/"+LOWRES+"/"+fitem.getName()));     
            return true;
        }catch(Exception ex){
            Logger.getLogger(UploadResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
    }
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/collection")
    public String getCollection(String json) throws JSONException
    {
        
        JSONObject jObj = new JSONObject(json);
        /*
        int userId = jObj.getInt("userId");
        
        List<Collection> coll = dbManager.getCollectionByUserId(userId);
        
        jObj = new JSONObject();
        
        for(Collection c: coll){
            jObj.put("id", c.getPhotoID().getId());
            jObj.put("location", c.getPhotoID().getLocation());
            jObj.put("name", c.getPhotoID().getName());            
        }
                */
        return jObj.toString();
    }
}
