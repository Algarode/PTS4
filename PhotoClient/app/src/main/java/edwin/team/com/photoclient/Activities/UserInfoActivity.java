package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edwin.team.com.photoclient.R;

/**
 * Created by rob on 16-12-2014.
 */
public class UserInfoActivity extends Activity{

    private String email,first_name,middle_name,last_name,street,house_nr,city,postal_code,gender,country;
    private TextView tv_email,tv_first_name,tv_middle_name,tv_last_name,tv_street,tv_house_nr,tv_city,tv_postal_code,tv_gender,tv_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        String jsonData = getIntent().getStringExtra("user");
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.initUserData(obj);
        initTextViews();
        setUserData();
    }

    private void initUserData(JSONObject jo){
        this.email = jo.optString("email");
        this.first_name = jo.optString("first_name");
        this.middle_name = jo.optString("middle_name");
        this.last_name = jo.optString("last_name");
        this.street = jo.optString("street");
        this.house_nr = jo.optString("house_nr");
        this.country = jo.optString("country");
        this.city = jo.optString("city");
        this.postal_code = jo.optString("postal_code");
        this.gender = jo.optString("gender");
    }

    private void initTextViews(){
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_first_name = (TextView)findViewById(R.id.tv_first_name);
        tv_middle_name = (TextView)findViewById(R.id.tv_middle_name);
        tv_last_name = (TextView)findViewById(R.id.tv_last_name);
        tv_street = (TextView)findViewById(R.id.tv_street);
        tv_house_nr = (TextView)findViewById(R.id.tv_house_number);
        tv_postal_code = (TextView)findViewById(R.id.tv_postal_code);
        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_country = (TextView)findViewById(R.id.tv_country);
        tv_gender = (TextView)findViewById(R.id.tv_gender);
    }

    private void setUserData(){
        tv_email.setText(this.email);
        tv_first_name.setText(this.first_name);
        tv_middle_name.setText(this.middle_name);
        tv_last_name.setText(this.last_name);
        tv_street.setText(this.street);
        tv_house_nr.setText(this.house_nr);
        tv_postal_code.setText(this.postal_code);
        tv_city.setText(this.city);
        tv_country.setText(this.country);
        tv_gender.setText(this.gender);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
