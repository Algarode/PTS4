package edwin.team.com.photoclient.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.R;

/**
 * Created by rob on 6-1-2015.
 */
public class SubscriptionListAdapter extends ArrayAdapter<JSONObject>{

    private Context context;
    private ArrayList<JSONObject> subscriptions;

    public SubscriptionListAdapter(Context context, ArrayList<JSONObject> subscriptions) {
        super(context,R.layout.subscription_list_item, subscriptions);
        this.context = context;
        this.subscriptions = subscriptions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.subscription_list_item,null);
        }

        if(view != null){
            final JSONObject obj = subscriptions.get(position);

            TextView photographer = (TextView)view.findViewById(R.id.li_photographer_sub);
            TextView fromDate = (TextView)view.findViewById(R.id.li_from_sub);
            TextView toDate = (TextView)view.findViewById(R.id.li_to_sub);
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.li_active_sub);

            final int id = obj.optInt("id");
            photographer.setText(obj.optString("photographer"));
            fromDate.setText(obj.optString("from_date"));
            toDate.setText(obj.optString("to_date"));

            if(obj.optBoolean("is_active"))
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ((SubscriptionActivity)context).activate(b, id);
                }
            });
        }

        return view;
    }
}
