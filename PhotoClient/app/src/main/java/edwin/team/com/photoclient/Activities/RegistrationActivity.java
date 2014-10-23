package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edwin.team.com.photoclient.R;
import edwin.team.com.photoclient.Classes.ServerManager;

public class RegistrationActivity extends Activity {

    EditText email,wachtwoord,tussenvoegsel,naam,achternaam,straat,huisnummer,postcode,stad;
    RadioButton man,vrouw;
    CheckBox fotograaf;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.init();

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
            Toast.makeText(this, "Sommige velden zijn niet ingevuld.", Toast.LENGTH_SHORT).show();
            // Uit de if springen als het fout gaat
            return;
        } else if (!semail.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")) { // Controleren of het email veld voldoet aan de regex
            // Toast (notificatie) laten zien als er geen geldig email adres is ingevuld
            Toast.makeText(this, "Geen geldig email adres ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!snaam.matches("[a-zA-Z]*")) {
            Toast.makeText(this, "Geen geldige naam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!stussenvoegsel.matches("")) {
            if (!stussenvoegsel.matches("^[a-zA-Z ]+(([\\'\\,\\.\\s-][a-zA-Z\\s])?[a-zA-Z])+$")) {
                Toast.makeText(this, "Geen geldig tussenvoegsel ingevuld.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (!sachternaam.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
            Toast.makeText(this, "Geen geldige achternaam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!man.isChecked() && !vrouw.isChecked()) {
            Toast.makeText(this, "Er is geen geslacht geselecteerd.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!sstraat.matches("^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?)$")) {
            Toast.makeText(this, "Geen geldige straatnaam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!shuisnummer.matches("^[0-9]+[a-zA-Z]+|[0-9]+$")) {
            Toast.makeText(this, "Geen geldig huisnummer ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!spostcode.matches("^[0-9]{4}[ ]?[a-z|A-Z]{2}$")) {
            Toast.makeText(this, "Geen geldige postcode ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!sstad.matches("^(([2][e][[:space:]]|['][ts][-[:space:]]))?[ëéÉËa-zA-Z]{2,}((\\s|[-](\\s)?)[ëéÉËa-zA-Z]{2,})*$")) {
            Toast.makeText(this, "Geen geldige stad ingevuld.", Toast.LENGTH_SHORT).show();
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

            // JSONObject maken, hier worden de waardes van de tekstvelden ingestopt
            JSONObject object = new JSONObject();
            try {
                // Alle velden putten
                object.put("email", semail);
                object.put("password", hashedpw);
                object.put("naam", snaam);
                object.put("tussenvoegsel", stussenvoegsel);
                object.put("achternaam", sachternaam);
                object.put("geslacht", geslacht);
                object.put("straat", sstraat);
                object.put("huisnummer", shuisnummer);
                object.put("postcode", spostcode);
                object.put("stad", sstad);
                object.put("fotograaf", isFotograaf);
                object.put("land", spinner.getSelectedItem().toString());

                // Het JSONObject "converteren" naar String. Deze String kan weer worden doorgestuurd naar de webservice
                String json = object.toString();

                if (isNetworkAvailable() == true) {
                    // JSON String meegeven, URL parameters meegeven
                    String result = new ServerManager().execute(json, "register/user").get();
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.optString("result") == "true")
                        finish();
                    else
                        Toast.makeText(this, "Fout bij registreren: " + jsonObject.optString("error"), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Uw apparaat is niet met het internet verbonden, uw registratie is daardoor niet verstuurd.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException ex) {
                ex.getMessage();
            }
        }
    }

    private boolean isNetworkAvailable() {
        // Netwerkstatus checken (verbonden met internet of niet)
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

}