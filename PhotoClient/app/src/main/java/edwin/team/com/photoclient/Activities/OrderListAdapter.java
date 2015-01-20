package edwin.team.com.photoclient.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.R;

/**
 * Created by rob on 16-12-2014.
 */
public class OrderListAdapter extends ArrayAdapter<JSONObject>{

    private Context context;
    private ArrayList<JSONObject> orders;

    public OrderListAdapter(Context context, ArrayList<JSONObject> orders) {
        super(context, R.layout.order_list_item, orders);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.order_list_item,null);
        }

        if(view != null){
            final JSONObject obj = orders.get(position);

            TextView ordernumber = (TextView)view.findViewById(R.id.li_order_order);
            TextView customer = (TextView)view.findViewById(R.id.li_order_user);
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.li_order_sent);

            final JSONObject order = obj.optJSONObject("order");
            final JSONObject cust = obj.optJSONObject("customer");

            String ordernr = "#" + String.valueOf(order.optInt("nr"));
            String customerName = cust.optString("first_name") + cust.optString("middle_name") + cust.optString("last_name");

            ordernumber.setText(ordernr);
            customer.setText(customerName);

            if(order.optBoolean("sent"))
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);

            ordernumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OrderSummaryActivity)context).openOrder(order);
                }
            });

            customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OrderSummaryActivity)context).openUser(cust);
                }
            });
        }
        
        return view;
    }
}
