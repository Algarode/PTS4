package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.OrderLine;
import edwin.team.com.photoclient.Classes.ShoppingCart;
import edwin.team.com.photoclient.Classes.ShowDialog;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

public class ShoppingCartActivity extends Activity {

    private ListView listview;
    private ShoppingCart cart;
    private ShoppingcartListAdapter adapter;
    private TextView sub,btw,total;
    private String selectedPhotoID;
    NumberFormat format = NumberFormat.getCurrencyInstance();
    AlertDialog.Builder builder;
    VolleyHelper volleyHelper = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        this.sub = (TextView)findViewById(R.id.shoppingcart_textview_sub);
        this.btw = (TextView)findViewById(R.id.shoppingcart_textview_btw);
        this.total = (TextView)findViewById(R.id.shoppingcart_textview_total);
        this.builder = new AlertDialog.Builder(ShoppingCartActivity.this);

        this.cart = AppController.getShoppingCart();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.listview = (ListView)findViewById(R.id.listview_shop);
        this.pDialog = new ProgressDialog(this);
        this.volleyHelper = AppController.getVolleyHelper();

        this.updateListview();
    }

    private void updateCosts(){
        this.sub.setText(R.string.sub_total+" : " + format.format(cart.getTotalPriceExTax()));
        this.btw.setText(R.string.tax+" : " + format.format(cart.getTax()));
        this.total.setText(R.string.tax+" : " + format.format(cart.getTotalPriceIncTax()));
    }

    public void updateShoppingCart(int amount, String photoID){
        cart.changeAmount(photoID, amount);
        updateListview();
    }

    private void updateListview(){
        this.adapter = new ShoppingcartListAdapter(this,cart.getOrders());
        this.listview.setAdapter(
            this.adapter
        );

        this.adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView test = (ImageView)v;
                ShowDialog.showImageDialog(ShoppingCartActivity.this, test);
            }
        });
        this.updateCosts();
    }

    public void submitOrder(View view) throws InterruptedException, ExecutionException, JSONException {
        if (General.reachHost() && this.volleyHelper != null) {

            // Set a loadingmessage for the user whilst serverdata is being retrieved
            this.pDialog.setMessage(String.valueOf(R.string.submitting_order_please_hold));

            // create jsonobject with the data you want to set as parameters to the server Key Value
            JSONObject json = new JSONObject();
            json.put("uid", General.USERID);
            Integer count = 0;
            for (OrderLine ol : this.cart.getOrders()) {
                count++;
                JSONObject obj = new JSONObject();
                obj.put("photoID", ol.getPhotoID());
                obj.put("amount", ol.getAmount());
                obj.put("sizeID", ol.getSizeID());
                json.put("order" + count.toString(), obj.toString());
            }
            // The url extension of the method you want to call
            String methodName = "upload/order";

            // Put code here to act when you successfully retrieve a JSONObject from the webserver
            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("test", response.toString());
                    Boolean result = response.optBoolean("result");
                    pDialog.hide(); // hides the loadingdialog since the server is done loading
                    if (result) {
                        cart.emptyShoppingCart();
                        updateListview();
                        Toast.makeText(getApplicationContext(), R.string.order_saved, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.order_saved_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            };

            // Put code here to act when a volleyerror occured most often a serverside problem
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                    volleyError.printStackTrace();
                    pDialog.hide(); // hide dialog when error is retrieved, server is no longer in process
                    Toast.makeText(getApplicationContext(), R.string.volley_error, Toast.LENGTH_SHORT).show();
                }
            };
            this.pDialog.show(); // show the dialog just before making the post to the server
            // execute the postmethod with the provided parameters
            this.volleyHelper.post(methodName, json, volleyListener, errorListener);
        }
        else{
            Toast.makeText(this, R.string.reach_server_error, Toast.LENGTH_LONG).show();
        }
    }

    public void removeOrder(String photoID){
        this.selectedPhotoID = photoID;
        this.builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    cart.deleteOrderLine(selectedPhotoID);
                    updateListview();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };



}
