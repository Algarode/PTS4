package edwin.team.com.photoclient.Classes;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.Serializable;

/**
 * Created by Hafid on 28-10-2014.
 */
public class ImageCollection implements Serializable {

    private transient Bitmap image;
    private byte[] imageByte;
    private int id;


    public ImageCollection (Bitmap image, int id) {
        this.image = image;
        this.id = id;
    }

    public void setImageByte(byte[] imageByte){
        this.imageByte = imageByte;
    }

    public byte[] getImageByte(){ return this.imageByte; }

    public Bitmap getImage(){
        return this.image;
    }

    public int getId(){
        return  this.id;
    }
}
