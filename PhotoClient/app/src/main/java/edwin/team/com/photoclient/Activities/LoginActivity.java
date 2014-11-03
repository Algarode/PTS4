package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ServerManager;
import edwin.team.com.photoclient.R;


public class LoginActivity extends Activity {

    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    public void login(View view) throws JSONException {
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        if(username.getText().toString().equals(""))
            Toast.makeText(this,"Please enter an email address",Toast.LENGTH_SHORT).show();

        else if(password.getText().toString().equals(""))
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_SHORT).show();

        else {
            //Building the jsonobject to send to the server
            JSONObject json = new JSONObject();
            try {
                json.put("username", username.getText());
                json.put("password", password.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // sending the jsonobject to the server and receiving the result
            if(General.reachHost()) {

                String data = null;
                try {
                    data = new ServerManager().execute(json.toString(), "generic/login").get();
                } catch (InterruptedException e) {
                    Log.e("InterruptedException","Thread has been interrupted and Exception has been thrown");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.e("ExecutionException","ExecutionException has occurred when attempting to retrieve data with login information");
                    e.printStackTrace();
                }

                if(General.isResultGood(data)) {
                    json = new JSONObject(data);
                    if (!json.optString("userID").equals("")) {
                            General.USERID = Integer.parseInt(json.optString("userID"));
                            General.USERROLE = Integer.parseInt(json.optString("role"));
                            Intent intent = new Intent(this, DashboardActivity.class);
                            this.startActivity(intent);
                            finish();
                        }
                }

                else{
                    Toast.makeText(this,"Either username of password is incorrect please try again",Toast.LENGTH_LONG).show();
                }

            }
            else{
                Toast.makeText(this,"Could not reach the host",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void toRegister(View view){
        Intent intent = new Intent(this, DashboardActivity.class);
        this.startActivity(intent);
    }

    public void skiplogin(View v)
    {
        Intent intent = new Intent(this, DashboardActivity.class);
        this.startActivity(intent);
    }
}
