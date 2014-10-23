package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.R;
import edwin.team.com.photoclient.Classes.ServerManager;


public class ChangeUserInfoActivity extends Activity {
    private JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

    private void getUserData() throws ExecutionException, InterruptedException, JSONException {
        jsonObject.put("userID", General.USERID);
        String responseJson = new ServerManager().execute(jsonObject.toString(),"account/select").get();
        if(!responseJson.isEmpty()){
            jsonObject = new JSONObject(responseJson);
            EditText editText = (EditText)findViewById(R.id.etTussenvoegsel);
            editText.setText(jsonObject.optString("middle_name"));

            editText = (EditText)findViewById(R.id.etAchternaam);
            editText.setText(jsonObject.optString("last_name"));

            editText = (EditText)findViewById(R.id.etStraatnaam);
            editText.setText(jsonObject.optString("street"));

            editText = (EditText)findViewById(R.id.etHuisnummer);
            editText.setText(jsonObject.optString("house_number"));

            editText = (EditText)findViewById(R.id.etStad);
            editText.setText(jsonObject.optString("city"));

            editText = (EditText)findViewById(R.id.etPostcode);
            editText.setText(jsonObject.optString("postal_code"));

            editText = (EditText)findViewById(R.id.etEmail);
            editText.setText(jsonObject.optString("email"));

            Spinner spinner = (Spinner) findViewById(R.id.spinnerLand);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.land_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            int spinnerposition = adapter.getPosition(jsonObject.getString("country"));
            spinner.setSelection(spinnerposition);
        }
    }

    public void update(View view) throws JSONException, ExecutionException, InterruptedException {
        jsonObject = new JSONObject();
        String value = "";
        EditText editText = (EditText)findViewById(R.id.etAchternaam);
        value = editText.getText().toString();
        if(!value.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) { //Achternaam
            Toast.makeText(this, "Geen geldige achternaam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonObject.put("last_name",value);
        editText = (EditText)findViewById(R.id.etStraatnaam);
        value = editText.getText().toString();
        if(!value.matches("^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?)$")){ //Straat
            Toast.makeText(this, "Geen geldige straatnaam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonObject.put("street",value);
        editText = (EditText)findViewById(R.id.etHuisnummer);
        value = editText.getText().toString();
        if(!value.matches("^[0-9]+[a-zA-Z]+|[0-9]+$")){ //Huisnummer
            Toast.makeText(this, "Geen geldig huisnummer ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonObject.put("house_number",value);
        editText = (EditText)findViewById(R.id.etStad);
        value = editText.getText().toString();
        if (!value.matches("^(([2][e][[:space:]]|['][ts][-[:space:]]))?[ëéÉËa-zA-Z]{2,}((\\s|[-](\\s)?)[ëéÉËa-zA-Z]{2,})*$")){ //Stad
            Toast.makeText(this, "Geen geldige stad ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonObject.put("city",value);
        editText = (EditText)findViewById(R.id.etPostcode);
        value = editText.getText().toString();
        if(!value.matches("^[0-9]{4}[ ]?[a-z|A-Z]{2}$")){ //Postcode
            Toast.makeText(this, "Geen geldige postcode ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonObject.put("postal_code",value);
        editText = (EditText)findViewById(R.id.etEmail);
        value = editText.getText().toString();
        if(!value.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")){ //Email
            Toast.makeText(this, "Geen geldig email adres ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText pass = (EditText)findViewById(R.id.etWachtwoord);
        jsonObject.put("password",pass.getText().toString());
        jsonObject.put("email",value);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerLand);
        jsonObject.put("country",spinner.getSelectedItem().toString());
        jsonObject.put("userID",String.valueOf(General.USERID));
        String json = jsonObject.toString();
        String result = new ServerManager().execute(json,"account/update").get();
        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.optString("result") == "true"){
            finish();
        }

        Toast.makeText(this, "Gegevens zijn geüpdate", Toast.LENGTH_SHORT).show();
    }
}
