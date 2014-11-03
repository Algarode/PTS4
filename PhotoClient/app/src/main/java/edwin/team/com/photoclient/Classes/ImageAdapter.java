package edwin.team.com.photoclient.Classes;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import edwin.team.com.photoclient.R;

/**
 * Created by Hafid on 28-10-2014.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageCollection> collection;

    public ImageAdapter (Context context, ArrayList<ImageCollection> collection ){
        this.context = context;
        this.collection = collection;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.image_grid_layout, null);

        ImageView iview = (ImageView)view.findViewById(R.id.grid_item_image);
        iview.setImageBitmap(collection.get(position).getImage());

        CheckBox cbox = (CheckBox)view.findViewById(R.id.grid_item_checkbox);
        cbox.setTag(position);

        return view;
    }
}
