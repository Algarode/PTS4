package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;


public class ChangeUserInfoActivity extends Activity {

    private JSONObject jsonObject = new JSONObject();
    private VolleyHelper volleyHelper = null;
    private Context context = this;
    EditText middle_name, last_name, street, house_number, city, postal_code, email, pass;
    Spinner spinner;
    int spinnerposition;
    String value = "";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.volleyHelper = AppController.getVolleyHelper();
        this.pDialog = new ProgressDialog(this);

        try {
            getUserData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void appendData(JSONObject data) {
        middle_name.setText(data.optString("middle_name"));
        last_name.setText(data.optString("last_name"));
        street.setText(data.optString("street"));
        house_number.setText(data.optString("house_number"));
        city.setText(data.optString("city"));
        postal_code.setText(data.optString("postal_code"));
        email.setText(data.optString("email"));
    }

    private void getUserData() throws ExecutionException, InterruptedException, JSONException {

        if (General.reachHost() && this.volleyHelper != null) {

            pDialog.setMessage(String.valueOf(R.string.fetching_data));

            jsonObject.put("userID", General.USERID);
            middle_name = (EditText) findViewById(R.id.etTussenvoegsel);
            last_name = (EditText) findViewById(R.id.etAchternaam);
            street = (EditText) findViewById(R.id.etStraatnaam);
            house_number = (EditText) findViewById(R.id.etHuisnummer);
            city = (EditText) findViewById(R.id.etStad);
            postal_code = (EditText) findViewById(R.id.etPostcode);
            email = (EditText) findViewById(R.id.etEmail);
            spinner = (Spinner) findViewById(R.id.spinnerLand);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.land_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseJson) {
                    if (responseJson != null) {
                        pDialog.hide();
                        appendData(responseJson);
                        spinner.setSelection(spinnerposition);
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                    volleyError.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.volley_error, Toast.LENGTH_SHORT).show();
                }
            };

            pDialog.show();

            this.volleyHelper.post("account/select", jsonObject, volleyListener, errorListener);

        }

    }

    public void update(View view) throws JSONException, ExecutionException, InterruptedException {

        if (General.reachHost() && volleyHelper != null) {

            EditText editText = (EditText) findViewById(R.id.etAchternaam);
            value = editText.getText().toString();
            if (!value.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) { //Achternaam
                Toast.makeText(this, R.string.invalid_lastname, Toast.LENGTH_SHORT).show();
                return;
            }
            jsonObject.put("last_name", value);

            editText = (EditText) findViewById(R.id.etTussenvoegsel);
            value = editText.getText().toString();
            jsonObject.put("middle_name", value);

            editText = (EditText) findViewById(R.id.etStraatnaam);
            value = editText.getText().toString();
            if (!value.matches("^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?)$")) { //Straat
                Toast.makeText(this, R.string.invalid_street, Toast.LENGTH_SHORT).show();
                return;
            }
            jsonObject.put("street", value);

            editText = (EditText) findViewById(R.id.etHuisnummer);
            value = editText.getText().toString();
            if (!value.matches("^[0-9]+[a-zA-Z]+|[0-9]+$")) { //Huisnummer
                Toast.makeText(this, R.string.invalid_housenr, Toast.LENGTH_SHORT).show();
                return;
            }
            jsonObject.put("house_number", value);

            editText = (EditText) findViewById(R.id.etStad);
            value = editText.getText().toString();
            if (!value.matches("^(([2][e][[:space:]]|['][ts][-[:space:]]))?[ëéÉËa-zA-Z]{2,}((\\s|[-](\\s)?)[ëéÉËa-zA-Z]{2,})*$")) { //Stad
                Toast.makeText(this, R.string.invalid_city, Toast.LENGTH_SHORT).show();
                return;
            }
            jsonObject.put("city", value);

            editText = (EditText) findViewById(R.id.etPostcode);
            value = editText.getText().toString();
            if (!value.matches("^[0-9]{4}[ ]?[a-z|A-Z]{2}$")) { //Postcode
                Toast.makeText(this, R.string.invalid_postal, Toast.LENGTH_SHORT).show();
                return;
            }
            jsonObject.put("postal_code", value);

            editText = (EditText) findViewById(R.id.etEmail);
            value = editText.getText().toString();
            if (!value.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) { //Email
                Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                return;
            }
            jsonObject.put("email", value);

            pass = (EditText) findViewById(R.id.etWachtwoord);
            jsonObject.put("password", pass.getText().toString());
            spinner = (Spinner) findViewById(R.id.spinnerLand);
            jsonObject.put("country", spinner.getSelectedItem().toString());
            jsonObject.put("userID", String.valueOf(General.USERID));

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObject) {
                    Intent dashBoard = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(dashBoard);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                    volleyError.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.volley_error, Toast.LENGTH_SHORT).show();
                }
            };

            this.volleyHelper.post("account/update", jsonObject, volleyListener, errorListener);

            Toast.makeText(this, R.string.reach_server_error, Toast.LENGTH_SHORT).show();
        }
    }
}