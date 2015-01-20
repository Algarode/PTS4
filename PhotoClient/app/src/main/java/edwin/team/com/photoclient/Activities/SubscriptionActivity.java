package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

/**
 * Created by rob on 6-1-2015.
 */
public class SubscriptionActivity extends Activity{

    private ListView listView;
    private ArrayList<JSONObject> subscriptions;
    private SubscriptionListAdapter adapter;
    VolleyHelper volleyHelper = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(this);
        this.subscriptions = new ArrayList<JSONObject>();
        this.getSubscriptions();
        this.init();
    }

    private void init(){
        this.listView = (ListView)findViewById(R.id.listViewSubscriptions);
    }

    private void getSubscriptions(){
        if(General.reachHost() && this.volleyHelper != null) {

            // Set a loadingmessage for the user whilst serverdata is being retrieved
            pDialog.setMessage(getApplicationContext().getString(R.string.fetching_data));

            // create jsonobject with the data you want to set as parameters to the server Key Value
            JSONObject json = new JSONObject();

            // The url extension of the method you want to call
            String methodName =  "photographer/getsubscriptions";

            // Put code here to act when you successfully retrieve a JSONObject from the webserver
            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("test", response.toString());
                    Boolean result = response.optBoolean("result");
                    pDialog.hide(); // hides the loadingdialog since the server is done loading
                    if(result){
                        int count = 0;
                        while(response.optJSONObject("sub"+count) != null){
                            subscriptions.add(response.optJSONObject("sub"+count));
                            count++;
                        }
                        adapter = new SubscriptionListAdapter(SubscriptionActivity.this,subscriptions);
                        listView.setAdapter(
                                adapter
                        );
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.reach_server_error, Toast.LENGTH_SHORT).show();
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

    // method to activate or deactivate a photographer
    public void activate(Boolean active, int id){
        if(General.reachHost() && this.volleyHelper != null) {

            // Set a loadingmessage for the user whilst serverdata is being retrieved
            pDialog.setMessage(getApplicationContext().getString(R.string.changes_saved));

            // create jsonobject with the data you want to set as parameters to the server Key Value
            JSONObject json = new JSONObject();
            try {
                json.put("activate",active);
                json.put("id",id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // The url extension of the method you want to call
            String methodName =  "photographer/setsubscription";

            // Put code here to act when you successfully retrieve a JSONObject from the webserver
            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("test", response.toString());
                    pDialog.hide();
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
