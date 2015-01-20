package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

/**
 * Created by rob on 16-12-2014.
 */
public class OrderSummaryActivity extends Activity{

    ListView orderListView;
    private OrderListAdapter adapter;
    private ArrayList<JSONObject> orders;
    VolleyHelper volleyHelper = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(this);
        this.orders = new ArrayList<JSONObject>();
        this.getOrderData();
        this.orderListView = (ListView)findViewById(R.id.listViewOrders);
    }

    public void openOrder(JSONObject order){
        Intent intent = new Intent(this,OrderInfoActivity.class);
        intent.putExtra("order",order.toString());
        startActivity(intent);
    }

    public void openUser(JSONObject user) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("user", user.toString());
        startActivity(intent);
    }

    private void getOrderData(){
        if(General.reachHost() && this.volleyHelper != null) {

            // Set a loadingmessage for the user whilst serverdata is being retrieved
            pDialog.setMessage(getApplicationContext().getString((R.string.gathering_order_data)));

            // create jsonobject with the data you want to set as parameters to the server Key Value
            JSONObject json = new JSONObject();

            // The url extension of the method you want to call
            String methodName =  "orderinfo/getorderinfo";

            // Put code here to act when you successfully retrieve a JSONObject from the webserver
            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("test", response.toString());
                    Boolean result = response.optBoolean("result");
                    pDialog.hide(); // hides the loadingdialog since the server is done loading
                    if(result){
                        response.remove("result");
                        Iterator<String> iter = response.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            JSONObject order = response.optJSONObject(key);
                            orders.add(order);
                        }
                        adapter = new OrderListAdapter(OrderSummaryActivity.this,orders);
                        orderListView.setAdapter(
                                adapter
                        );
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
            };

            // Put code here to act when a volleyerror occured most often a serverside problem
            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                    volleyError.printStackTrace();
                    pDialog.hide(); // hide dialog when error is retrieved, server is no longer in process
                    Toast.makeText(getApplicationContext(),R.string.volley_error,Toast.LENGTH_SHORT).show();
                }
            };
            pDialog.show(); // show the dialog just before making the post to the server
            // execute the postmethod with the provided parameters
            this.volleyHelper.post(methodName,json,volleyListener,errorListener);
        }
        else{
            Toast.makeText(this,R.string.reach_server_error,Toast.LENGTH_LONG).show();
        }
    }
}
