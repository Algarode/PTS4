package edwin.team.com.photoclient.Classes;


import android.graphics.Bitmap;
import java.io.Serializable;

public class OrderLine implements Serializable{
    private Integer  sizeID, amount;
    String photoID, productId;
    private Double price, priceProduct;
    private Bitmap image, product;
    private String name, productName;
    private NeoBitmap nbmp;

    public OrderLine(String photoID, Double price, Integer sizeID, Integer amount, Bitmap image, String name){
        this.photoID = photoID;
        this.price = price;
        this.sizeID = sizeID;
        this.amount = amount;
        this.image = image;
        this.name = name;
        nbmp = null;
        this.priceProduct = 0.0;
    }

    public OrderLine(String photoID, Double price, Integer sizeID, Integer amount, NeoBitmap nbmp, String name){
        this.photoID = photoID;
        this.price = price;
        this.sizeID = sizeID;
        this.amount = amount;
        this.nbmp = nbmp;
        this.name = name;
        this.priceProduct = 0.0;
    }

    public OrderLine(String photoID, String productId, Double price, Double priceProduct, Integer amount, Bitmap photo, Bitmap product, String photoName, String productName) {
        this.photoID = photoID;
        this.productId = productId;
        this.price = price;
        this.priceProduct = priceProduct;
        this.amount = amount;
        this.image = photo;
        this.product = product;
        this.name = photoName;
        this.productName = productName;
    }

    public Double getTotalPrice(){
        if (priceProduct == 0) {
            return (this.price * this.amount);
        } else {
            return ((this.price + priceProduct) * amount);
        }
    }

    public Double getUnitPrice(){
        if (priceProduct == 0) {
            return this.price;
        } else {
            return (this.price + priceProduct);
        }
    }

    public Double getProductPhotoPrice() { return ((this.price + priceProduct) * this.amount); }

    public Integer getAmount(){
        return this.amount;
    }

    public Bitmap getImage() {
        if (nbmp==null) {
            return this.image;
        } else {
            return nbmp.getImageByFilter();
        }
    }

    public NeoBitmap getNeoBitmap() {return  this.nbmp;}

    public Bitmap getProductImage() { return this.product; }

    public String getName() { return this.name; }

    public String getProductName() { return this.productName; }

    public Integer getSizeID() { return this.sizeID; }

    public String getPhotoID() { return this.photoID; }

    public String getProductId() { return this.productId; }

    public void setSize(Integer sizeID){
        this.sizeID = sizeID;
    }

    public void setAmount(Integer amount){
        this.amount = amount;
    }
}
