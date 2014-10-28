package com.fontys.edwin.fotoopproductdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView iv = (ImageView) findViewById(R.id.imageViewForeground);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_view);

                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageViewDialog);
                ImageView iv = (ImageView) findViewById(R.id.imageViewForeground);
                imageView.setImageDrawable(iv.getDrawable());

                dialog.show();
                
            }
        });
    }

}
