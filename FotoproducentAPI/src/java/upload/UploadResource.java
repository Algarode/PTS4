/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package upload;

import com.entities.Account;
import com.entities.Album;
import com.entities.Bestelling;
import com.entities.Collection;
import com.entities.OrderLine;
import com.entities.Photo;
import com.entities.Size1;
import com.entities.SizePrize;
import com.entities.User;
import com.general.DBM;
import com.generic.Hashids;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
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
    
    String FOLDER_PATH = "";
    String ORIGIN = "origin";
    String LOWRES = "lowres";
    int USERID = 0;
    private final DBM dbManager;
    InetAddress IP = null;
    
    @Context
    private UriInfo context;

    public UploadResource() throws UnknownHostException {
        dbManager = new DBM();
        IP = InetAddress.getLocalHost();
        //FOLDER_PATH = "http://"+IP.getHostAddress()+":8080/FotoproducentAPI/images/";
        FOLDER_PATH = "D:\\svn_repo\\Photoproject\\FotoproducentAPI\\web\\images\\";
    }

    
    private String getUniqueId(String prefix) throws Exception
    {
        Hashids hashids = null;
        String encoded = "";
        
            hashids = new Hashids("teamEdwin");
            encoded = hashids.encode(System.currentTimeMillis()).toLowerCase();
            
            if(prefix.equals("p")){
                
                if (dbManager.findById(Photo.class, encoded) != null)
                {
                    getUniqueId(prefix);
                }
            } else {
                if (dbManager.findById(Album.class, encoded) != null)
                {
                    getUniqueId(prefix);
                }
            }
        return prefix +"_" + encoded;
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

                if(items.size() > 1)
                {
                    int PhotographId = Integer.valueOf(items.get(0).getString());
                    items.remove(0);//userId
                    
                    CreateFolderIfNotExists(PhotographId);
                    for(FileItem fi : items){ 
                        String token = this.getUniqueId("p");
                        SaveImages(fi);
                        SaveToDatabase(token, fi.getName(), PhotographId);
                    }
                }
                return new JSONObject().put("result", true).toString();
            }catch(Exception e){
                e.printStackTrace();
                return new JSONObject().put("result", false).toString();
            }
        }
        return new JSONObject().put("result", false).toString();
    }

    private void SaveToDatabase(String token, String name, int photographId){
        Photo p = new Photo();
        p.setId(token);
        p.setLocation(name);
        p.setName(name);
        Account account = dbManager.findById(Account.class,photographId);
        p.setPhotographerId(account.getUser());

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
            jObj.put("id", c.getPhotoID().getId())
            jObj.put("location", c.getPhotoID().getLocation());
            jObj.put("name", c.getPhotoID().getName());            
        }
                */
        return jObj.toString();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products")
    public String getProducts (String json) throws JSONException {
        JSONObject jObj = new JSONObject(json);
        return jObj.toString();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/order")
    public String saveOrder(String orders){
        try {
            JSONObject jObj = new JSONObject(orders);
            User user = null;
            if(jObj.optInt("uid") != 0)
                user = dbManager.getUser(jObj.optInt("uid"));
            if(user != null){
                jObj.remove("uid");
                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                Bestelling best = new Bestelling();
                Integer id = null;
                if(jObj.length() > 0){
                    
                    Date date = new Date();
                    best.setDate(date);
                    best.setUserID(user);
                    dbManager.save(best);
                    id = best.getId();
                }
                for(int i = 1; i <= jObj.length(); i++){
                    
                    JSONObject order = new JSONObject(jObj.optString("order"+i));
                    if(order != null){
                        int orderID = id;
                        String photoID = order.optString("photoID");
                        int sizeID = order.optInt("sizeID");
                        int amount = order.optInt("amount");
                        OrderLine ol = new OrderLine();
                        ol.setAmount(amount);
                        ol.setOrderID(best);
                        ol.setPhotoID(dbManager.findById(Photo.class, photoID));
                        ol.setSizeID(dbManager.findById(SizePrize.class, sizeID));
                        dbManager.save(ol);
                    }
                }
                return new JSONObject().put("result", true).toString();
            }
            else
                return new JSONObject().put("result", false).toString();
        } 
        catch (JSONException ex) {
            Logger.getLogger(UploadResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
