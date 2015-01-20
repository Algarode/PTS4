package edwin.team.com.photoclient.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import edwin.team.com.photoclient.Activities.PickProductActivity;
import edwin.team.com.photoclient.R;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProductCollection> pcollection;
    private VolleyHelper helper = AppController.getVolleyHelper();
    private ImageLoader imageLoader;
    private RadioGroup radioGroup;
    private LayoutInflater inflater;
    private RadioButton radioButton;
    private int selectedPosition = -1;

    public ProductAdapter (ArrayList<ProductCollection> pcollection, Context context) {
        this.context = context;
        this.pcollection = pcollection;
        this.imageLoader = helper.getImageLoader();
        radioGroup = new RadioGroup(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pcollection.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_grid_layout, null);
            holder = new Holder();

            holder.networkImageView = (NetworkImageView) view.findViewById(R.id.grid_item_imagee);

            holder.networkImageView.setImageUrl(pcollection.get(position).getImageURL(), imageLoader);
            holder.networkImageView.setDefaultImageResId(R.drawable.loading);
            holder.networkImageView.setErrorImageResId(R.drawable.error);
            holder.networkImageView.setId(position);

            holder.radioButton = (RadioButton) view.findViewById(R.id.grid_item_radiobutton);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((position != selectedPosition && radioButton != null)) {
                    radioButton.setChecked(false);
                }

                selectedPosition = position;
                radioButton = (RadioButton) view;
            }
        });

        if (selectedPosition != position) {
            holder.radioButton.setChecked(false);
        } else {
            holder.radioButton.setChecked(true);
            if (radioButton != null && holder.radioButton != radioButton) {
                radioButton = holder.radioButton;
            }
        }

            //RadioButton rbut = (RadioButton) view.findViewById(R.id.grid_item_radiobutton);

            //rbut.setTag(position);


        return view;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ProductCollection col = pcollection.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.product_grid_layout, null);

        final NetworkImageView image = (NetworkImageView) view.findViewById(R.id.grid_item_imagee);

        image.setImageUrl(col.getImageURL(), imageLoader);
        image.setDefaultImageResId(R.drawable.loading);
        image.setErrorImageResId(R.drawable.error);
        image.setId(position);

        final RadioButton rbut = (RadioButton) view.findViewById(R.id.grid_item_radiobutton);
        if(col.getChecked())
            rbut.setChecked(true);
        rbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((PickProductActivity)context).onRbImageChecked(col,image);
            }
        });

        return view;

    }

    private class Holder {
        NetworkImageView networkImageView;
        RadioButton radioButton;
    }

}
