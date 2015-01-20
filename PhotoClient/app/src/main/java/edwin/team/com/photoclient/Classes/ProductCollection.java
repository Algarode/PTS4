package edwin.team.com.photoclient.Classes;

import java.io.Serializable;

/**
 * Created by Edwin on 6-1-2015.
 */
public class ProductCollection implements Serializable {

    private String imageURL;
    private String id;
    private String name;
    private double price;
    private byte[] imageByte;
    private boolean checked;

    public ProductCollection(String imageURL, String id, String name, double price) {
        this.imageURL = imageURL;
        this.id = id;
        this.name = name;
        this.price = price;
        this.checked = false;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public byte[] getImageByte() {
        return this.imageByte;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public String getId() { return this.id; }

    public Double getPrice() { return this.price; }

    public String getName() { return "1000 x 1000"; }

    public void setChecked(boolean check){ this.checked = check;}

    public boolean getChecked(){ return this.checked;}

}
