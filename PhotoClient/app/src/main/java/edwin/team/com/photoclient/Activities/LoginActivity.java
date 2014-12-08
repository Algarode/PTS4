package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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


public class LoginActivity extends Activity {

    EditText username,password;
    VolleyHelper volleyHelper = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(this);
    }


    public void login(View view) throws JSONException {
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        if(username.getText().toString().equals(""))
            Toast.makeText(this,R.string.enter_email,Toast.LENGTH_SHORT).show();

        else if(password.getText().toString().equals(""))
            Toast.makeText(this,R.string.enter_password,Toast.LENGTH_SHORT).show();

        else {
            if(General.reachHost() && this.volleyHelper != null) {

                // Set a loadingmessage for the user whilst serverdata is being retrieved
                pDialog.setMessage(String.valueOf(R.string.verifying_login));

                // create jsonobject with the data you want to set as parameters to the server Key Value
                JSONObject json = new JSONObject();
                json.put("username",username.getText().toString());
                json.put("password",password.getText().toString());

                // The url extension of the method you want to call
                String methodName =  "generic/login";

                // Put code here to act when you successfully retrieve a JSONObject from the webserver
                Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test",response.toString());
                        Boolean result = response.optBoolean("result");
                        pDialog.hide(); // hides the loadingdialog since the server is done loading
                        if(result){
                            General.ROLEID = response.optInt("role");
                            General.USERID = response.optInt("userID");
                            Intent dashBoarIntent = new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(dashBoarIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.login_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                // Put code here to act when a volleyerror occured most often a serverside problem
                Response.ErrorListener errorListener = new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyLog.e("VolleyError","Error: " + volleyError.getMessage());
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

    public void toRegister(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        this.startActivity(intent);
    }

    public void skiplogin(View v){
        Intent intent = new Intent(this, DashboardActivity.class);
        this.startActivity(intent);
    }
}
