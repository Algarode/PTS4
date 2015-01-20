package edwin.team.com.photoclient.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edwin.team.com.photoclient.R;

/**
 * Created by Hafid on 23-11-2014.
 */
public class AlbumAdapter extends BaseAdapter {

    private ArrayList<Album> albums;
    private Context context;

    public AlbumAdapter(Context context, ArrayList<Album> albums){
        this.albums = albums;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albums.size();
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
        view = inflater.inflate(R.layout.album_grid_layout, null);

        Album a = albums.get(position);

        TextView amount = (TextView)view.findViewById(R.id.amount_album_photos);
        amount.setText(String.valueOf(a.getAmount()));
        amount.setTag(a.getToken());


        TextView name = (TextView)view.findViewById(R.id.album_name);
        name.setText(a.getName());




        return view;
    }
}
