package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class FillCodeActivity extends Activity {

    private VolleyHelper volleyHelper = null;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_code);
        pDialog = new ProgressDialog(this);
        this.volleyHelper = AppController.getVolleyHelper();
    }



    public void getPhoto(View view) throws JSONException {
        EditText code = (EditText) findViewById(R.id.UniekeCodeCash);
        //code.setText("p_g6x3rlb5k");
        if(!code.getText().toString().equals("")){
            if(General.reachHost() && this.volleyHelper != null) {
                pDialog.setMessage(getApplicationContext().getString(R.string.claiming_code));
                JSONObject json = new JSONObject();
                json.put("code", code.getText().toString());
                json.put("uid", General.USERID);
                Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Claim code", response.toString());
                        Boolean result = response.optBoolean("result");
                        pDialog.hide();
                        if(result){
                            Toast.makeText(getApplicationContext(), R.string.content_added, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.code_invalid,Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                        volleyError.printStackTrace();
                        pDialog.hide();
                        Toast.makeText(getApplicationContext(),R.string.volley_error,Toast.LENGTH_SHORT).show();
                    }
                };
                pDialog.show();

                this.volleyHelper.post("generic/claimCode",json,volleyListener,errorListener);
            }
            else{
                Toast.makeText(this,R.string.reach_server_error,Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fill_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
