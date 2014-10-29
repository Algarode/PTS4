package com.fontys.edwin.fotoopproductdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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

            ShowDialog showDialog = new ShowDialog();
            ImageView imageView = (ImageView) findViewById(R.id.imageViewForeground);
            showDialog.showImageDialog(MainActivity.this, imageView);

            }

        });

    }

}
