/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package upload;

import com.generic.Hashids;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UpploadResource
     */
    public UploadResource() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getunique")
    public String getUniqueId() throws Exception
    {
        Hashids hashids = null;
        try{
             hashids = new Hashids("teamEdwin");
        }catch(Exception ex){
            return new JSONObject().put("result", false).toString();
        }
        return new JSONObject().put("id",hashids.encode(System.currentTimeMillis()).toLowerCase()).put("result", true).toString();
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
                    String userId = items.get(1).getString();
                    items.remove(0);//token
                    items.remove(0);//userId
                    
                    CreateFolderIfNotExists(Integer.valueOf(userId));
                    for(FileItem fi : items){ 
                        SaveImages(fi);
                    }
                }
                
                return new JSONObject().put("result", true).toString();
            }catch(Exception e){
                return new JSONObject().put("result", false).toString();
            }
        }
        return new JSONObject().put("result", false).toString();
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
    
    private void SaveImages(FileItem fitem) throws IOException, Exception
    {
        String extension = fitem.getContentType().split("/")[1];
                
        File file = new File(FOLDER_PATH+USERID+"/"+ORIGIN+"/"+fitem.getName());
        fitem.write(file);
        
        BufferedImage bufferedImage = ImageIO.read(file);
        
        //Lowres 500 x 500 px version
        BufferedImage resizedImage = new BufferedImage(500, 500, Image.SCALE_DEFAULT);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0,500,500,null);
        g.dispose();
        
        ImageIO.write(resizedImage, extension , new File(FOLDER_PATH+USERID+"/"+LOWRES+"/"+fitem.getName()));   
    }
}
