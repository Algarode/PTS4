package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.ShowDialog;
import edwin.team.com.photoclient.R;

/**
 * Created by rob on 16-12-2014.
 */
public class OrderInfoActivity extends Activity {

    private String order_nr,date;
    private OrderLineAdapter adapter;
    ListView orderListView;
    private ArrayList<JSONObject> orderLines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        this.orderLines = new ArrayList<JSONObject>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(getIntent().getStringExtra("order"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int i = 1;
        while(obj.optJSONObject("line"+i) != null){
            this.orderLines.add(obj.optJSONObject("line"+i));
            i++;
        }
        this.orderListView = (ListView)findViewById(R.id.listViewOrderLines);
        this.adapter = new OrderLineAdapter(this,this.orderLines);
        this.orderListView.setAdapter(
                this.adapter
        );
    }

    public void enlarge(NetworkImageView ni){
        ShowDialog.showImageDialog(OrderInfoActivity.this, ni);
    }
}
