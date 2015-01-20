package edwin.team.com.photoclient.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

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

    public static void showPhotoStatistics(Context context, ImageView imageView, ImageCollection ic){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_photo_statistics);
        NumberFormat format = NumberFormat.getCurrencyInstance();

        ImageView imageView1 = (ImageView) dialog.findViewById(R.id.imageViewDialog_statistics);
        TextView amountSold = (TextView)dialog.findViewById(R.id.amount_sold);
        TextView profit = (TextView)dialog.findViewById(R.id.profit);
        imageView1.setImageDrawable(imageView.getDrawable());
        amountSold.setText(String.valueOf(ic.getSold()));
        profit.setText(String.valueOf(format.format(ic.getProfit())));
        dialog.show();
    }

}
