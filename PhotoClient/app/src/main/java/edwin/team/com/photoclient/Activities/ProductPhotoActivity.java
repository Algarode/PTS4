package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.Classes.ProductCollection;
import edwin.team.com.photoclient.Classes.ShoppingCart;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

/**
 * Created by Edwin on 1-12-2014.
 */
public class ProductPhotoActivity extends Activity {

    private ImageCollection imageCollection;
    private ProductCollection productCollection;
    private VolleyHelper helper = AppController.getVolleyHelper();
    private ImageLoader imageLoader;
    private BitmapByteArrayConversion bbac = new BitmapByteArrayConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_productphoto);
        Intent intent = getIntent();
        this.imageCollection = (ImageCollection) intent.getSerializableExtra("collection");
        this.productCollection = (ProductCollection) intent.getSerializableExtra("pcollection");
        this.imageLoader = helper.getImageLoader();

        NetworkImageView background = (NetworkImageView) findViewById(R.id.nivBackground);
        NetworkImageView foreground = (NetworkImageView) findViewById(R.id.nivForeground);

        background.setImageUrl(productCollection.getImageURL(), imageLoader);
        background.setDefaultImageResId(R.drawable.loading);
        background.setErrorImageResId(R.drawable.error);
        background.setId(0);

        foreground.setImageUrl(imageCollection.getImageUrl(), imageLoader);
        foreground.setDefaultImageResId(R.drawable.loading);
        foreground.setErrorImageResId(R.drawable.error);
        foreground.setId(0);
    }

    public void btnClick(View view) {
        ShoppingCart cart = AppController.getShoppingCart();
        cart.addOrderLine(
                imageCollection.getId(), productCollection.getId(),
                imageCollection.getPrice(), productCollection.getPrice(), 1,
                bbac.toBitmap(imageCollection.getImageByte()), bbac.toBitmap(productCollection.getImageByte()),
                imageCollection.getSizeName(), productCollection.getName());

        Intent intent = new Intent(this, MainDashboardActivity.class);
        this.startActivity(intent);
        finish();
    }

}
