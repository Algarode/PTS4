package edwin.team.com.photoclient;

import android.app.Activity;
import android.content.Intent;
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

public class RegistrationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerLand);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.land_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void register(View view) {
        // Email veld zoeken
        EditText email = (EditText) findViewById(R.id.etEmail);
        // String gemaakt die gevuld wordt met de text van het email-veld
        String semail = email.getText().toString();

        // Bovenstaande commentaar is van toepassing op onderstaande EditTexts
        EditText wachtwoord = (EditText) findViewById(R.id.etWachtwoord);
        String swachtwoord = wachtwoord.getText().toString();

        EditText naam = (EditText) findViewById(R.id.etNaam);
        String snaam = naam.getText().toString();

        EditText tussenvoegsel = (EditText) findViewById(R.id.etTussenvoegsel);
        String stussenvoegsel = tussenvoegsel.getText().toString();

        EditText achternaam = (EditText) findViewById(R.id.etAchternaam);
        String sachternaam = achternaam.getText().toString();

        EditText straat = (EditText) findViewById(R.id.etStraatnaam);
        String sstraat = straat.getText().toString();

        EditText huisnummer = (EditText) findViewById(R.id.etHuisnummer);
        String shuisnummer = huisnummer.getText().toString();

        EditText postcode = (EditText) findViewById(R.id.etPostcode);
        String spostcode = postcode.getText().toString();

        EditText stad = (EditText) findViewById(R.id.etStad);
        String sstad = stad.getText().toString();

        RadioButton man = (RadioButton) findViewById(R.id.rbMan);
        RadioButton vrouw = (RadioButton) findViewById(R.id.rbVrouw);
        String geslacht = "";

        CheckBox fotograaf = (CheckBox) findViewById(R.id.cbFotograaf);
        boolean isFotograaf = false;

        // Controleren of er velden leeg zijn
        if (semail.matches("") || swachtwoord.matches("") || snaam.matches("") || stussenvoegsel.matches("") || sachternaam.matches("") ||
                sstraat.matches("") || shuisnummer.matches("") || spostcode.matches("") || sstad.matches("")) {
            // Toast (notificatie) laten zien als er velden leeg zijn
            Toast.makeText(this, "Sommige velden zijn niet ingevuld.", Toast.LENGTH_SHORT).show();
            // Uit de if springen als het fout gaat
            return;
        } else if (!semail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) { // Controleren of het email veld voldoet aan de regex
            // Toast (notificatie) laten zien als er geen geldig email adres is ingevuld
            Toast.makeText(this, "Geen geldig email adres ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!snaam.matches("[A-Z][a-zA-Z]*")) {
            Toast.makeText(this, "Geen geldige naam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!stussenvoegsel.matches("^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])?[a-zA-Z])$")) {
            Toast.makeText(this, "Geen geldig tussenvoegsel ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!sachternaam.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
            Toast.makeText(this, "Geen geldige achternaam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!man.isChecked() && !vrouw.isChecked()) {
            Toast.makeText(this, "Er is geen geslacht geselecteerd.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!sstraat.matches("^([1-9][e][\\s])*([a-zA-Z]+(([\\.][\\s])|([\\s]))?")) {
            Toast.makeText(this, "Geen geldige straatnaam ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!shuisnummer.matches("[1-9][0-9]*(([-][1-9][0-9]*)|([\\s]?[a-zA-Z]+))?$")) {
            Toast.makeText(this, "Geen geldig huisnummer ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!spostcode.matches("^[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$")) {
            Toast.makeText(this, "Geen geldige postcode ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!sstad.matches("^(([2][e][[:space:]]|['][ts][-[:space:]]))?[ëéÉËa-zA-Z]{2,}((\\s|[-](\\s)?)[ëéÉËa-zA-Z]{2,})*$")) {
            Toast.makeText(this, "Geen geldige stad ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Geslacht checken
            if (man.isChecked()) {
                geslacht = "man";
            } else {
                geslacht = "vrouw";
            }

            // Controleren of de fotograaf checkbox is geselecteerd
            if (fotograaf.isChecked()) {
                isFotograaf = true;
            }

            // JSONObject maken, hier worden de waardes van de tekstvelden ingestopt
            JSONObject object = new JSONObject();
            try {
                // Alle velden putten
                object.put("email", semail);
                object.put("password", swachtwoord);
                object.put("naam", snaam);
                object.put("tussenvoegsel", stussenvoegsel);
                object.put("achternaam", sachternaam);
                object.put("geslacht", geslacht);
                object.put("straat", sstraat);
                object.put("huisnummer", shuisnummer);
                object.put("postcode", spostcode);
                object.put("stad", sstad);
                object.put("fotograaf", isFotograaf);

                // Het JSONObject "converteren" naar String. Deze String kan weer worden doorgestuurd naar de webservice
                String json = object.toString();
                // TODO Post String naar webservice

            } catch (JSONException ex) {
                ex.getMessage();
            }

            Toast.makeText("Uw registratie is verstuurd.", Toast.LENGTH_LONG);

            // Upload activity openen
            Intent intent = new Intent(this,uploadActivity.class);
            startActivity(intent);
            finish();
        }
    }
}