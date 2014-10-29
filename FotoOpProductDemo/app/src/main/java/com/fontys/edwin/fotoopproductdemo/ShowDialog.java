package com.fontys.edwin.fotoopproductdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Edwin on 28-10-2014.
 */
public class ShowDialog extends Activity {

    public void showImageDialog(Context context, ImageView imageView) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_view);

        ImageView imageView1 = (ImageView) dialog.findViewById(R.id.imageViewDialog);
        imageView1.setImageDrawable(imageView.getDrawable());

        dialog.show();

    }

}
