package edwin.team.com.photoclient.Activities;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.R;

public class ProductConfirmationAdapter extends ArrayAdapter<ImageCollection>{

    private Context context;
    private ArrayList<ImageCollection> imageCollection;
    BitmapByteArrayConversion bbac;
    NumberFormat format = NumberFormat.getCurrencyInstance();

    public ProductConfirmationAdapter(Context context, ArrayList<ImageCollection> imageCollection) {
        super(context, R.layout.product_item, imageCollection);
        this.context = context;
        this.imageCollection = imageCollection;
        bbac = new BitmapByteArrayConversion();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.product_item, null);
        }

        if (view != null) {
            final ImageCollection collection = imageCollection.get(position);

            ImageView iView = (ImageView)view.findViewById(R.id.li_photo_pc);
            final Spinner spin = (Spinner)view.findViewById(R.id.li_size_pc);
            final EditText amount = (EditText)view.findViewById(R.id.li_amount_pc);
            TextView price = (TextView)view.findViewById(R.id.li_price_pc);

            amount.setText(String.valueOf(collection.getAmount()));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,collection.getSizeAdapter());
            iView.setImageBitmap(bbac.toBitmap(collection.getImageByte()));
            spin.setAdapter(adapter);
            spin.setSelection(collection.getSizeID());

            final AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ((ProductConfirmationActivity) context).changePrice(position, i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            };

            spin.post(new Runnable() {
                public void run() {
                    spin.setOnItemSelectedListener(listener);
                }
            });
            spin.setTag(position);



            amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        ((ProductConfirmationActivity) context).changeAmount(position, Integer.parseInt(amount.getText().toString()));
                    }
                    return false;
                }
            });
            price.setText(format.format(collection.getStartPrice()));
        }

        return view;
    }

}
