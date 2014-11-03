package edwin.team.com.photoclient.Activities;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edwin.team.com.photoclient.Classes.OrderLine;
import edwin.team.com.photoclient.R;

public class ShoppingcartListAdapter extends ArrayAdapter<OrderLine>{

    private Context context;
    private List<OrderLine> shoppingCart;

    public ShoppingcartListAdapter(Context context, List<OrderLine> shoppingCart){
        super(context, R.layout.shoppingcart_item,shoppingCart);
        this.context = context;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.shoppingcart_item, null);
        }

        if(view != null){
            Log.e("test", String.valueOf(position));
            OrderLine order = shoppingCart.get(position);
            ImageView iView = (ImageView)view.findViewById(R.id.li_photo);
            TextView tName = (TextView)view.findViewById(R.id.li_name);
            EditText amount = (EditText)view.findViewById(R.id.li_amount);
            TextView measure = (TextView)view.findViewById(R.id.li_measure);
            iView.setImageDrawable(order.getImage());
            tName.setText(order.getName());
            amount.setText(String.valueOf(order.getAmount()));
            measure.setText("measure");
        }

        return view;
    }
}
