package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

public class PhotographerPriceActivity extends Activity{

    EditText sp1,sp2,sp3,sp4;
    TextView pp1,pp2,pp3,pp4;
    Button savePrices;
    VolleyHelper volleyHelper = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photographer_prices);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(this);
        this.init();
    }

    private void init(){
        this.sp1 = (EditText)findViewById(R.id.sp_1);
        this.sp2 = (EditText)findViewById(R.id.sp_2);
        this.sp3 = (EditText)findViewById(R.id.sp_3);
        this.sp4 = (EditText)findViewById(R.id.sp_4);
        this.pp1 = (TextView)findViewById(R.id.pp_1);
        this.pp2 = (TextView)findViewById(R.id.pp_2);
        this.pp3 = (TextView)findViewById(R.id.pp_3);
        this.pp4 = (TextView)findViewById(R.id.pp_4);
        this.savePrices = (Button)findViewById(R.id.prices_save);

        if(General.reachHost() && this.volleyHelper != null) {

            // Set a loadingmessage for the user whilst serverdata is being retrieved
            pDialog.setMessage(getApplicationContext().getString((R.string.fetching_data)));

            // create jsonobject with the data you want to set as parameters to the server Key Value
            JSONObject json = new JSONObject();
            try {
                json.put("pid", General.USERID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // The url extension of the method you want to call
            String methodName =  "photoprice/getprices";

            // Put code here to act when you successfully retrieve a JSONObject from the webserver
            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("test", response.toString());
                    pDialog.hide(); // hides the loadingdialog since the server is done loading
                    setPrices(response);
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

    private void setPrices(JSONObject prices){
        if(prices.length() == 4){
            for(int i = 1; i <=4; i++){
                JSONObject jo = prices.optJSONObject(String.valueOf(i));
                if(jo != null){
                    String size = String.valueOf(jo.optDouble("height")) + " x " + String.valueOf(jo.optDouble("width"));
                    String value = String.valueOf(jo.optDouble("price"));
                    switch (i){
                        case 1:
                            pp1.setText(size);
                            sp1.setText(value);
                            break;
                        case 2:
                            pp2.setText(size);
                            sp2.setText(value);
                            break;
                        case 3:
                            pp3.setText(size);
                            sp3.setText(value);
                            break;
                        case 4:
                            pp4.setText(size);
                            sp4.setText(value);
                            break;
                    }
                }
            }
        }
        else
            Toast.makeText(this,getString(R.string.could_not_retrieve_prices),Toast.LENGTH_SHORT).show();
    }

    public void savePrices(View view) {
        if(sp1.getText().length() > 0 && sp2.getText().length() > 0 && sp3.getText().length() > 0 && sp4.getText().length() > 0){
            if(General.reachHost() && this.volleyHelper != null) {

                // Set a loadingmessage for the user whilst serverdata is being retrieved
                pDialog.setMessage(String.valueOf(R.string.changes_saved));

                // create jsonobject with the data you want to set as parameters to the server Key Value
                JSONObject json = new JSONObject();
                try {
                    json.put("pid", General.USERID);
                    json.put("1",Double.parseDouble(sp1.getText().toString()));
                    json.put("2",Double.parseDouble(sp2.getText().toString()));
                    json.put("3",Double.parseDouble(sp3.getText().toString()));
                    json.put("4",Double.parseDouble(sp4.getText().toString()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // The url extension of the method you want to call
                String methodName =  "photoprice/setprices";

                // Put code here to act when you successfully retrieve a JSONObject from the webserver
                Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test", response.toString());
                        pDialog.hide(); // hides the loadingdialog since the server is done loading
                        setPrices(response);
                        Toast.makeText(getApplicationContext(),getString(R.string.prices_saved),Toast.LENGTH_SHORT).show();
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
        else{
            Toast.makeText(this,R.string.order_saved_failed,Toast.LENGTH_SHORT).show();
        }
    }
}
