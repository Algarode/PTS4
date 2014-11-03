package edwin.team.com.photoclient.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.R;

public class ProductConfirmationAdapter extends ArrayAdapter<ImageCollection> {

    private Context context;
    private ArrayList<ImageCollection> imageCollection;
    BitmapByteArrayConversion bbac = new BitmapByteArrayConversion();

    public ProductConfirmationAdapter(Context context, ArrayList<ImageCollection> imageCollection) {
        super(context, R.layout.product_item, imageCollection);
        this.context = context;
        this.imageCollection = imageCollection;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.product_item, null);
        }

        if (view != null) {
            ImageCollection collection = imageCollection.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivProductImage);
            TextView tvName = (TextView) view.findViewById(R.id.tvProductName);
            Spinner spinner = (Spinner) view.findViewById(R.id.spinnerSizes);
            EditText etAmount = (EditText) view.findViewById(R.id.etProductAmount);
            TextView tvPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            imageView.setImageBitmap(collection.getImageByte(bbac.toBitmap(collection.getImageByte())));
            tvName.setText();
            spinner.setAdapter(ProductConfirmationActivity.adapter);
            etAmount.setText();
            tvPrice.setText();
        }

        return view;
    }
}
