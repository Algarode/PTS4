package edwin.team.com.photoclient.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Hafid on 29-10-2014.
 */
public class BitmapByteArrayConversion {

    public byte[] toByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    public Bitmap toBitmap(byte[] btarray){
        return BitmapFactory.decodeByteArray(btarray, 0, btarray.length);
    }
}
