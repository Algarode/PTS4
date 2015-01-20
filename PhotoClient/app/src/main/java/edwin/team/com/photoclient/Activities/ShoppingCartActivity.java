package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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
    //set the environment for production/sandbox/no netowrk
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_CLIENT_ID = "AWkWThCsnOjsjwZxR2KwkhQFHJAAtJ2Fxxex3ql2gOY5RZbNJJkWxgjrhIEX";
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("PhotoClient");
    private static final int REQUEST_CODE_PAYMENT = 1;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.listview = (ListView)findViewById(R.id.listview_shop);
        this.pDialog = new ProgressDialog(this);
        this.volleyHelper = AppController.getVolleyHelper();

        this.updateListview();

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }

    private void updateCosts(){
        this.sub.setText(getApplicationContext().getString(R.string.sub_total)+" : " + format.format(cart.getTotalPriceExTax()));
        this.btw.setText(getApplicationContext().getString(R.string.tax)+" : " + format.format(cart.getTax()));
        this.total.setText(getApplicationContext().getString(R.string.total)+" : " + format.format(cart.getTotalPriceIncTax()));
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

        PayPalPayment payment = new PayPalPayment(new BigDecimal(cart.getTotalPriceIncTax()),"USD","fotobestelling",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(ShoppingCartActivity.this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent,REQUEST_CODE_PAYMENT);
    }

    private void saveOrder() throws JSONException {
        if (General.reachHost() && this.volleyHelper != null) {

            // Set a loadingmessage for the user whilst serverdata is being retrieved
            this.pDialog.setMessage(getApplicationContext().getString(R.string.submitting_order_please_hold));

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

                //add-on by Stefan to allow different file sizes
                if (ol.getNeoBitmap()!=null){
                    obj.put("Neo", "true");
                    obj.put("filter", ol.getNeoBitmap().filterID);
                    obj.put("X", ol.getNeoBitmap().x);
                    obj.put("y", ol.getNeoBitmap().y);
                    obj.put("width", ol.getNeoBitmap().width);
                    obj.put("height", ol.getNeoBitmap().height);
                } else {
                    obj.put("Neo", "false");
                }
                if (ol.getProductId() != "") {
                    obj.put("productID", ol.getProductId());
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("test", confirm.toJSONObject().toString(4));
                        Log.i("test", confirm.getPayment().toJSONObject().toString(4));
                        this.saveOrder();
                    } catch (JSONException e) {
                        Log.e("test", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("test", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "test",
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
        }

}
