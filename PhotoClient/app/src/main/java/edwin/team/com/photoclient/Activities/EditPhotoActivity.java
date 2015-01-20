package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.CropRect;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.Classes.NeoBitmap;
import edwin.team.com.photoclient.Classes.ShoppingCart;
import edwin.team.com.photoclient.Classes.filter;
import edwin.team.com.photoclient.R;

/**
 * Created by stefan on 12-Jan-15.
 */
public class EditPhotoActivity extends Activity {

    private ImageView imv;
    private  ImageView imr;
    private NeoBitmap nbmp;
    private ImageCollection currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        Intent intent = getIntent();
        //setting all fields
        currentItem = (ImageCollection) intent.getSerializableExtra("CurrentItem");
        imv = (ImageView) findViewById(R.id.imgView);
        imr = (ImageView) findViewById(R.id.imgPreview);
        final CropRect view = (CropRect) findViewById(R.id.dragRect);
        nbmp = new NeoBitmap(General.bmp);
        General.bmp = null;
        imv.setImageBitmap(nbmp.image);
        imr.setImageBitmap(nbmp.image);
        imv.buildDrawingCache();
        //initializing the croprect and editing it's eventhandler.
        if (null != view) {
            view.setOnUpCallback(new CropRect.OnUpCallback() {
                @Override
                public void onRectFinished(final Rect rect) {
                  //  Toast.makeText(getApplicationContext(), "geselecteerde range (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                //            Toast.LENGTH_LONG).show();
                    imv.buildDrawingCache(true);
                    try {
                        //new values
                        Bitmap newBitmap = Bitmap.createBitmap(filter.getRightFilteredMap(imv.getDrawingCache(true), nbmp.filterID), rect.left, rect.top, rect.width(), rect.height(), null, false);
                        nbmp.changeSize(rect.left,rect.top,rect.width(), rect.height());
                        imr.setImageBitmap(newBitmap);
                    } catch (Exception ex) {
                       // Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e("Filter Error: ", ex.getMessage());
                    }

                }
            });
        }
    }

    //turn bitmap to black and white
    public void BlacknWhite(View view){
        try {
            Bitmap bnw = filter.BlackNWhite(nbmp.image);
            nbmp.filterID = 1;
            imv.setImageBitmap(bnw);
            if (nbmp.x != -1) {
                imr.setImageBitmap(Bitmap.createBitmap(filter.getRightFilteredMap(imv.getDrawingCache(true), nbmp.filterID), nbmp.x, nbmp.y, nbmp.width, nbmp.height, null, false));
            }
            else{
                imr.setImageBitmap(bnw);
            }
        } catch (Exception ex){
            Log.e("Filter Error: ", ex.getMessage());
        }
    }

    //sepia without Blue
    public  void SepianoBlue(View view) {
        try {
            Bitmap bnw = filter.SepiaNoBlue(nbmp.image);
            nbmp.filterID = 2;
            imv.setImageBitmap(bnw);
            if (nbmp.x != -1) {
                imr.setImageBitmap(Bitmap.createBitmap(filter.getRightFilteredMap(imv.getDrawingCache(true), nbmp.filterID), nbmp.x, nbmp.y, nbmp.width, nbmp.height, null, false));
            }
            else{
                imr.setImageBitmap(bnw);
            }

        } catch (Exception ex){
            Log.e("Filter Error: ", ex.getMessage());
        }
    }

    //sepia brown (beautyfull
    public void  SepiaExperiment(View view){
        try {
            Bitmap bnw = filter.SepiaBrown(nbmp.image);
            nbmp.filterID = 3;
            imv.setImageBitmap(bnw);
            if (nbmp.x != -1) {
                imr.setImageBitmap(Bitmap.createBitmap(filter.getRightFilteredMap(imv.getDrawingCache(true), nbmp.filterID), nbmp.x, nbmp.y, nbmp.width, nbmp.height, null, false));
            }
            else{
                imr.setImageBitmap(bnw);
            }
        } catch (Exception ex){
            Log.e("Filter Error: ", ex.getMessage());
        }
    }

    //get the original image without any weird effects
    public void Revert(View view){
        try {
            Bitmap bnw = nbmp.image;
            nbmp.filterID = 0;
            imv.setImageBitmap(bnw);
            if (nbmp.x != -1) {
                imr.setImageBitmap(Bitmap.createBitmap(filter.getRightFilteredMap(imv.getDrawingCache(true), nbmp.filterID), nbmp.x, nbmp.y, nbmp.width, nbmp.height, null, false));
            }
            else{
                imr.setImageBitmap(bnw);
            }
        } catch (Exception ex){
            Log.e("Filter Error: ", ex.getMessage());
        }
    }
    public void btnUncrop(View view){
        imr.setImageBitmap(filter.getRightFilteredMap(nbmp.image, nbmp.filterID));
    }

    //adds edited pic to cart
    public void AddToCart(View view){
        AppController.getShoppingCart().addOrderLine(currentItem.getId() ,currentItem.getPrice() ,currentItem.getSizeID() ,1 , nbmp , "Edited: " + currentItem.getSizeName());
        //General.getShoppingCart().addOrderLine(currentItem.getId() ,currentItem.getPrice() ,currentItem.getSizeID() ,1 , nbmp , "Edited: " + currentItem.getSizeName());
        Toast.makeText(getApplicationContext(), getString(R.string.item_added_to_shoppingcart), Toast.LENGTH_LONG).show();
    }

    //return to the select page
    public void ReturnToSelect(View view){
        Intent intent = new Intent(this, PickPhotoActivity.class);
        this.startActivity(intent);
    }
}
