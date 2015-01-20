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

import edwin.team.com.photoclient.Activities.EditPickPhotoActivity;
import edwin.team.com.photoclient.Activities.PickPhotoActivity;
import edwin.team.com.photoclient.R;

/**
 * Created by Edwin on 11-1-2015.
 */
public class ImageAdapterPickPhoto extends BaseAdapter {

    private Context context;
    private ArrayList<ImageCollection> collection;
    private VolleyHelper helper = AppController.getVolleyHelper();
    private ImageLoader imageLoader;
    private boolean isEditPickPhoto;


    public ImageAdapterPickPhoto(Context context, ArrayList<ImageCollection> collection, boolean isEditPickPhoto) {
        this.context = context;
        this.collection = collection;
        this.imageLoader = helper.getImageLoader();
        this.isEditPickPhoto = isEditPickPhoto;
    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.image_grid_pickphoto, null);
        final ImageCollection col = collection.get(position);
        final NetworkImageView image = (NetworkImageView )view.findViewById(R.id.grid_item_photo);

        image.setImageUrl(collection.get(position).getImageUrl(),imageLoader);
        image.setDefaultImageResId(R.drawable.loading);
        image.setErrorImageResId(R.drawable.error);
        image.setId(position);

        final RadioButton rbut = (RadioButton) view.findViewById(R.id.grid_item_radiobutton);
        rbut.setTag(position);
        if(col.isChecked())
            rbut.setChecked(true);
        rbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEditPickPhoto)
                    ((PickPhotoActivity)context).onRbImageChecked(col,image);
                else
                    ((EditPickPhotoActivity)context).onRbImageChecked(rbut);
            }
        });

        return view;
    }
}
