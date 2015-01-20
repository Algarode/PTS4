package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ImageAdapterPickPhoto;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.Classes.Price;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

/**
 * Created by stefan on 13-Jan-15.
 */
public class EditPickPhotoActivity extends Activity {

    private GridView gridView = null;
    private VolleyHelper volleyHelper = null;
    ProgressDialog progressDialog;
    private ArrayList<ImageCollection> collection;
    private ArrayList<ImageCollection> checkedCollection = new ArrayList<ImageCollection>();
    private BitmapByteArrayConversion convert = new BitmapByteArrayConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_edit);
        this.volleyHelper = AppController.getVolleyHelper();
        this.progressDialog = new ProgressDialog(this);
        gridView = (GridView) findViewById(R.id.gvPickEditPhoto);

        try {
            getPhotos();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getPhotos() throws ExecutionException, InterruptedException, JSONException {

        JSONObject jsonObject = new JSONObject();
        collection = new ArrayList<ImageCollection>();
        String methodName = "generic/getCollection";

        if (General.reachHost() && this.volleyHelper != null) {

            jsonObject.put("uid", General.USERID);
            progressDialog.setMessage(getApplicationContext().getString(R.string.fetching_data));

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseJson) {
                    if (responseJson != null) {
                        try {
                            for (Iterator<String> iter = responseJson.keys(); iter.hasNext(); ) {
                                String key = iter.next();
                                ArrayList<Price> prices = new ArrayList<Price>();
                                JSONObject obj = (JSONObject) responseJson.get(key);
                                JSONObject po = (JSONObject) obj.get("price");
                                for (int i = 1; i <= 4; i++) {
                                    JSONObject priceObject = (JSONObject) po.get("price" + i);
                                    prices.add(new Price(priceObject.optInt("width"), priceObject.optInt("height"), priceObject.optDouble("price"), priceObject.optInt("sizePriceID")));
                                }
                                ImageCollection coll = new ImageCollection(obj.getString("location"), obj.getString("id"), prices);
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
                    Toast.makeText(getApplicationContext(), "An error occurred with the server please try again later", Toast.LENGTH_SHORT).show();

                }
            };

            progressDialog.show();

            this.volleyHelper.post(methodName, jsonObject, volleyListener, errorListener);

        }

    }

    private void setGridView () {
        if (collection.size() > 0) {
            ImageAdapterPickPhoto imageAdapter = new ImageAdapterPickPhoto(this, collection,true);
            gridView.setAdapter(imageAdapter);
        } else
            Toast.makeText(getApplicationContext(), "Er konden geen foto's worden opgehaald omdat uw collectie leeg is.", Toast.LENGTH_LONG).show();
    }

    public void onRbImageChecked (View view){
        boolean checked = ((RadioButton) view).isChecked();
        int position = (Integer)view.getTag();
        ImageCollection col = collection.get(position);
        if(checked){
            checkedCollection.clear();
            checkedCollection.add(col);
        }
    }

    public void btnSendToEdit(View view){
            if (checkedCollection.size() == 1){
                int id = getResources().getIdentifier("resource" + 0, "id", getPackageName());

                NetworkImageView image = (NetworkImageView) findViewById(id);
                General.bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Intent intent = new Intent(this, EditPhotoActivity.class);
                intent.putExtra("CurrentItem", checkedCollection.get(0));
                this.startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "Selecteerd 1 afbeelding, niet meer niet minde", Toast.LENGTH_SHORT).show();
            }
    }
}
