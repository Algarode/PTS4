package edwin.team.com.photoclient.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import edwin.team.com.photoclient.R;

/**
 * Created by Hafid on 28-10-2014.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageCollection> collection;
    private VolleyHelper helper = AppController.getVolleyHelper();
    private ImageLoader imageLoader;


    public ImageAdapter (Context context, ArrayList<ImageCollection> collection ){
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

        final NetworkImageView image = (NetworkImageView )view.findViewById(R.id.grid_item_image);

        image.setImageUrl(collection.get(position).getImageUrl(),imageLoader);
        image.setDefaultImageResId(R.drawable.loading);
        image.setErrorImageResId(R.drawable.error);
        image.setId(position);


        if(General.ROLEID == 2){
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowDialog.showPhotoStatistics(context,image,collection.get(position));
                }
            });
        }

        CheckBox cbox = (CheckBox)view.findViewById(R.id.grid_item_checkbox);
        cbox.setTag(position);

        return view;
    }

    public void unCheckEverything()
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_grid_layout, null);

        for( int i = 0; i < getCount(); i++){
            CheckBox cbox = (CheckBox)view.findViewById(R.id.grid_item_checkbox);
            cbox.setChecked(false);
        }
    }
}
