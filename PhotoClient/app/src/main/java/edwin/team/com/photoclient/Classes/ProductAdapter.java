package edwin.team.com.photoclient.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;
import edwin.team.com.photoclient.R;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageCollection> collection;
    private VolleyHelper helper = AppController.getVolleyHelper();
    private ImageLoader imageLoader;


    public ProductAdapter (Context context, ArrayList<ImageCollection> collection ){
        this.context = context;
        this.collection = collection;
        this.imageLoader = helper.getImageLoader();
    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.image_grid_layout, null);

        NetworkImageView image = (NetworkImageView )view.findViewById(R.id.grid_item_imagee);

        image.setImageUrl(collection.get(position).getImageUrl(), imageLoader);
        image.setDefaultImageResId(R.drawable.loading);
        image.setErrorImageResId(R.drawable.error);
        image.setId(position);


        RadioButton rbut = (RadioButton) view.findViewById(R.id.grid_item_radiobutton);
        rbut.setTag(position);

        return view;
    }
}
