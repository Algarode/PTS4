package com.fontys.edwin.fotoopproductdemo;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {

    public static Drawable[] drawables = new Drawable[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();

        //Drawable dr = getResources().getDrawable(R.drawable.bird);
        //Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

        //Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));

        drawables[0] = resources.getDrawable(R.drawable.mok);
        drawables[1] = resources.getDrawable(R.drawable.bird);
        //drawables[1] = d;

        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageDrawable(layerDrawable);
        iv.setLayoutParams(layoutParams);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(getLayoutInflater().inflate(R.layout.firetruck, null));
                dialog.show();
            }
        });
    }

}
