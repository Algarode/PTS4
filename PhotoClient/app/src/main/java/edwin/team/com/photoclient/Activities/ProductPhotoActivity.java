package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.R;

/**
 * Created by Edwin on 1-12-2014.
 */
public class ProductPhotoActivity extends Activity {

    ImageView imageView = (ImageView) findViewById(R.id.imageViewProductPhoto);
    private ArrayList<ImageCollection> imageCollectionList;
    BitmapByteArrayConversion bbac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.imageCollectionList = (ArrayList<ImageCollection>) intent.getSerializableExtra("collection");

        if (imageCollectionList.size() >= 2) {
            Bitmap background = bbac.toBitmap(imageCollectionList.get(0).getImageByte());
            Bitmap photo = bbac.toBitmap(imageCollectionList.get(1).getImageByte());
            combineProductPhoto(background, photo);
        }
    }

    private boolean checkDimensions (Bitmap photo) {
        int w = photo.getWidth();
        int h = photo.getHeight();

        if (w <= 50 || h <= 50) {
            return true;
        }

        return false;
    }

    public void combineProductPhoto (Bitmap background, Bitmap photo) {
        Drawable bg = new BitmapDrawable(getResources(), background);
        Drawable p = new BitmapDrawable(getResources(), photo);

        imageView.setBackground(bg);
        if (this.checkDimensions(photo)) {
            imageView.setImageDrawable(p);
        } else {
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(photo, 50, 50, true));
            imageView.setImageDrawable(d);
        }

    }

}
