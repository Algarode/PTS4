package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.Price;
import edwin.team.com.photoclient.Classes.ProductAdapter;
import edwin.team.com.photoclient.Classes.ProductCollection;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

public class PickProductActivity extends Activity {

    private GridView gridView = null;
    private VolleyHelper volleyHelper = null;
    ProgressDialog progressDialog;
    private ArrayList<ProductCollection> collection;
    private ProductCollection chosenProduct;
    private BitmapByteArrayConversion convert = new BitmapByteArrayConversion();
    private boolean isChecked = false;
    private BitmapByteArrayConversion bbac = new BitmapByteArrayConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_product);
        this.volleyHelper = AppController.getVolleyHelper();
        this.progressDialog = new ProgressDialog(this);
        gridView = (GridView) findViewById(R.id.gvPickProduct);

        try {
            getProducts();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getProducts() throws ExecutionException, InterruptedException, JSONException {

        JSONObject jsonObject = new JSONObject();
        collection = new ArrayList<ProductCollection>();
        String methodName = "products/getProducts";
        
        if (General.reachHost() && this.volleyHelper != null) {

            jsonObject.put("uid", General.USERID);
            progressDialog.setMessage(getString(R.string.fetching_products));

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseJson) {
                    if (responseJson != null) {
                        try {
                            for (Iterator<String> iter = responseJson.keys(); iter.hasNext();) {
                                String key = iter.next();
                                ArrayList<Price> prices = new ArrayList<Price>();
                                JSONObject obj = (JSONObject) responseJson.get(key);
                                ProductCollection coll = new ProductCollection(obj.getString("location"), obj.getString("id"), obj.getString("name"), obj.optDouble("price"));
                                collection.add(coll);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            setGridView();
                            progressDialog.hide();
                        }
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                    volleyError.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Er is een error opgetreden, probeer het later opnieuw", Toast.LENGTH_LONG).show();

                }
            };

            //progressDialog.show();

            this.volleyHelper.post(methodName, jsonObject, volleyListener, errorListener);

        }

    }

    private void setGridView () {

        ProductAdapter productAdapter = new ProductAdapter(collection, this);
        gridView.setAdapter(productAdapter);

    }

    public void onRbImageChecked (ProductCollection collect, ImageView i) {

        for(ProductCollection col : this.collection){
            if(col.getId() == collect.getId()){
                col.setChecked(true);
                this.chosenProduct = col;
                this.isChecked = true;
                col.setImageByte(bbac.toByteArray(General.generateBitmap(i)));
            }
            else
                col.setChecked(false);
        }
        setGridView();
    }

    public void btnClick (View view) {

        if(isChecked){
            Intent intent = new Intent(this, PickPhotoActivity.class);
            intent.putExtra("collection", chosenProduct);
            this.startActivity(intent);
            finish();
        } else
        Toast.makeText(getApplicationContext(), getString(R.string.no_product_selected), Toast.LENGTH_SHORT).show();

    }

}
