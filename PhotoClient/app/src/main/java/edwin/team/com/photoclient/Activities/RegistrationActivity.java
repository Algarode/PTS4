package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONObject;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

public class RegistrationActivity extends Activity {

    EditText email,wachtwoord,tussenvoegsel,naam,achternaam,straat,huisnummer,postcode,stad;
    RadioButton man,vrouw;
    CheckBox fotograaf;
    Spinner spinner;
    VolleyHelper volleyHelper = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.init();
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(this);
    }

    private void init(){

        email = (EditText) findViewById(R.id.etEmail);
        wachtwoord  = (EditText) findViewById(R.id.etWachtwoord);
        tussenvoegsel = (EditText) findViewById(R.id.etTussenvoegsel);
        naam = (EditText) findViewById(R.id.etNaam);
        achternaam = (EditText) findViewById(R.id.etAchternaam);
        straat = (EditText) findViewById(R.id.etStraatnaam);
        huisnummer = (EditText) findViewById(R.id.etHuisnummer);
        postcode = (EditText) findViewById(R.id.etPostcode);
        stad = (EditText) findViewById(R.id.etStad);
        man = (RadioButton) findViewById(R.id.rbMan);
        vrouw = (RadioButton) findViewById(R.id.rbVrouw);
        fotograaf = (CheckBox) findViewById(R.id.cbFotograaf);
        spinner = (Spinner) findViewById(R.id.spinnerLand);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.land_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void register(View view) throws Exception {
        String semail = email.getText().toString();
        String swachtwoord = wachtwoord.getText().toString();
        String snaam = naam.getText().toString();
        String stussenvoegsel = tussenvoegsel.getText().toString();
        String sachternaam = achternaam.getText().toString();
        String sstraat = straat.getText().toString();
        String shuisnummer = huisnummer.getText().toString();
        String spostcode = postcode.getText().toString();
        String sstad = stad.getText().toString();
        String geslacht = "";
        boolean isFotograaf = false;

        // Controleren of er velden leeg zijn
        if (semail.matches("") || swachtwoord.matches("") || snaam.matches("") || sachternaam.matches("") ||
                sstraat.matches("") || shuisnummer.matches("") || spostcode.matches("") || sstad.matches("")) {
            // Toast (notificatie) laten zien als er velden leeg zijn
            Toast.makeText(this, R.string.some_fields_empty, Toast.LENGTH_SHORT).show();
            // Uit de if springen als het fout gaat
            return;
        } else if (!semail.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) { // Controleren of het email veld voldoet aan de regex
            // Toast (notificatie) laten zien als er geen geldig email adres is ingevuld
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return;
        } else if (!snaam.matches("[a-zA-Z]*")) {
            Toast.makeText(this, R.string.invalid_name, Toast.LENGTH_SHORT).show();
            return;
        } else if (!stussenvoegsel.matches("")) {
            if (!stussenvoegsel.matches("^[a-zA-Z ]+(([\\'\\,\\.\\s-][a-zA-Z\\s])?[a-zA-Z])+$")) {
                Toast.makeText(this, R.string.invalid_middlename, Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (!sachternaam.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
            Toast.makeText(this, R.string.invalid_lastname, Toast.LENGTH_SHORT).show();
            return;
        } else if (!man.isChecked() && !vrouw.isChecked()) {
            Toast.makeText(this, R.string.sex_selected, Toast.LENGTH_SHORT).show();
            return;
        } else if (!sstraat.matches("^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?)$")) {
            Toast.makeText(this, R.string.invalid_street, Toast.LENGTH_SHORT).show();
            return;
        } else if (!shuisnummer.matches("^[0-9]+[a-zA-Z]+|[0-9]+$")) {
            Toast.makeText(this, R.string.invalid_housenr, Toast.LENGTH_SHORT).show();
            return;
        } else if (!spostcode.matches("^[0-9]{4}[ ]?[a-z|A-Z]{2}$")) {
            Toast.makeText(this, R.string.invalid_postal, Toast.LENGTH_SHORT).show();
            return;
        } else if (!sstad.matches("^(([2][e][[:space:]]|['][ts][-[:space:]]))?[ëéÉËa-zA-Z]{2,}((\\s|[-](\\s)?)[ëéÉËa-zA-Z]{2,})*$")) {
            Toast.makeText(this, R.string.invalid_city, Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Geslacht checken
            if (man.isChecked()) {
                geslacht = "M";
            } else {
                geslacht = "V";
            }

            // Controleren of de fotograaf checkbox is geselecteerd
            if (fotograaf.isChecked()) {
                isFotograaf = true;
            }

            // Password ecnrypten met bcrypt
            String hashedpw = swachtwoord;//BCrypt.hashpw(swachtwoord, BCrypt.gensalt(12));
            // Om te verifiëren of het wachtwoord klopt, gebruik dit:
            // BCrypt.checkpw(password_from_user, hashed_password_from_database);

            if(General.reachHost() && this.volleyHelper != null) {

                // JSONObject maken, hier worden de waardes van de tekstvelden ingestopt
                JSONObject json = new JSONObject();

                // Alle velden putten
                json.put("email", semail);
                json.put("password", hashedpw);
                json.put("naam", snaam);
                json.put("tussenvoegsel", stussenvoegsel);
                json.put("achternaam", sachternaam);
                json.put("geslacht", geslacht);
                json.put("straat", sstraat);
                json.put("huisnummer", shuisnummer);
                json.put("postcode", spostcode);
                json.put("stad", sstad);
                json.put("fotograaf", isFotograaf);
                json.put("land", spinner.getSelectedItem().toString());

                // Set a loadingmessage for the user whilst serverdata is being retrieved
                pDialog.setMessage(getApplicationContext().getString(R.string.account_being_saved));

                // The url extension of the method you want to call
                String methodName =  "register/user";

                // Put code here to act when you successfully retrieve a JSONObject from the webserver
                Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test", response.toString());
                        Boolean result = response.optBoolean("result");
                        pDialog.hide(); // hides the loadingdialog since the server is done loading
                        if(result){
                            Intent LoginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(LoginIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.registration_failed,Toast.LENGTH_SHORT).show();
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
}