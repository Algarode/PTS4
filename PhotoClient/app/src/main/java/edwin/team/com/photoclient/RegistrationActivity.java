package edwin.team.com.photoclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
        EditText email = (EditText) findViewById(R.id.etEmail);
        String semail = email.getText().toString();

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

        if (semail.matches("") || swachtwoord.matches("") || snaam.matches("") || stussenvoegsel.matches("") || sachternaam.matches("") ||
                sstraat.matches("") || shuisnummer.matches("") || spostcode.matches("") || sstad.matches("")) {
            Toast.makeText(this, "Sommige velden zijn niet ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!semail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            Toast.makeText(this, "Geen geldig email adres ingevuld.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!snaam.matches("[A-Z][a-zA-Z]*")) {
            return;
        } else if (!stussenvoegsel.matches("")) {
            return;
        } else if (!sachternaam.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")) {
            return;
        } else if (!sstraat.matches("")) {
            return;
        } else if (!shuisnummer.matches("")) {
            return;
        } else if (!spostcode.matches("")) {
            return;
        } else if (!sstad.matches("")) {
            return;
        } else {
            // TODO stuur gegevens door naar server

            // Upload activity openen
            Intent intent = new Intent(this,uploadActivity.class);
            startActivity(intent);
            finish();
        }
    }
}