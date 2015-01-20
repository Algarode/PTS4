package edwin.team.com.photoclient.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

/**
 * Created by rob on 5-1-2015.
 */
public class OrderLineAdapter extends ArrayAdapter<JSONObject>{

    private Context context;
    private ArrayList<JSONObject> orderLines;
    private VolleyHelper helper = AppController.getVolleyHelper();
    private ImageLoader imageLoader;

    public OrderLineAdapter(Context context, ArrayList<JSONObject> orders) {
        super(context, R.layout.orderline_list_item, orders);
        this.context = context;
        this.orderLines = orders;
        this.imageLoader = helper.getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.orderline_list_item,null);
        }

        if(view != null){
            final JSONObject obj = orderLines.get(position);

            final NetworkImageView niv = (NetworkImageView)view.findViewById(R.id.li_photo_ol);
            TextView photoGrapher = (TextView)view.findViewById(R.id.li_photographer_ol);
            TextView amount = (TextView)view.findViewById(R.id.li_amount_ol);
            TextView measure = (TextView)view.findViewById(R.id.li_measure_ol);
            TextView piecePrice = (TextView)view.findViewById(R.id.li_price_ol);
            TextView totalPrice = (TextView)view.findViewById(R.id.li_total_price_ol);
            niv.setImageUrl(obj.optString("url"),imageLoader);
            niv.setDefaultImageResId(R.drawable.loading);
            niv.setErrorImageResId(R.drawable.error);
            niv.setId(position);

            photoGrapher.setText(obj.optString("photographer"));
            amount.setText(String.valueOf(obj.optInt("amount")));
            measure.setText(obj.optString("measure"));
            piecePrice.setText(String.valueOf(obj.optDouble("piece_price")));
            totalPrice.setText(String.valueOf((obj.optDouble("piece_price") * obj.optInt("amount"))));

            niv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OrderInfoActivity)context).enlarge(niv);
                }
            });
        }

        return view;
    }
}
