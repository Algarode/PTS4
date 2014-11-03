package edwin.team.com.photoclient.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;
import edwin.team.com.photoclient.R;

public class ShowDialog extends Activity {

    public static void showImageDialog(Context context, ImageView imageView) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);

        ImageView imageView1 = (ImageView) dialog.findViewById(R.id.imageViewDialog);
        imageView1.setImageDrawable(imageView.getDrawable());

        dialog.show();

    }

}
