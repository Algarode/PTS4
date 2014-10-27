package com.fontys.edwin.fotoopproductdemo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView iv = (ImageView) findViewById(R.id.imageViewForeground);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                ImageView imageView = (ImageView) findViewById(R.id.imageViewDialog);
                imageView.setImageDrawable(iv.getDrawable());
                dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_view, null));
                dialog.show();
            }
        });
    }

}
