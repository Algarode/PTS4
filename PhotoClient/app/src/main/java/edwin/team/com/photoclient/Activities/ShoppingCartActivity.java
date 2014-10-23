package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import edwin.team.com.photoclient.R;

public class ShoppingCartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setupView();
    }

    private void setupView(){
        ScrollView Sview = (ScrollView)  findViewById(  R.id.Scroller);
        Display display = getWindowManager().getDefaultDisplay();
        Sview.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, display.getHeight() - 150 ));
        EditText amount = (EditText) findViewById(R.id.sc_Aantal);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = Integer.parseInt(s.toString());
                TextView t = (TextView)findViewById(R.id.PrijspStuk);
                TextView tv = (TextView)findViewById(R.id.TotaalPrijs);
                Double l = Double.parseDouble(t.toString());
                Double price = l *i;
                tv.setText(price.toString());
            }

        });
    }
}
