package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import java.util.concurrent.ExecutionException;
import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.Classes.ProductAdapter;
import edwin.team.com.photoclient.Classes.VolleyHelper;

public class PickPhotoActivity extends Activity {

    private GridView gridView = null;
    private VolleyHelper volleyHelper = null;
    ProgressDialog progressDialog;
    private ArrayList<ImageCollection> collection;
    private ArrayList<ImageCollection> checkedCollection = new ArrayList<ImageCollection>();
    private ArrayList<ImageCollection> imageCollectionList;
    private BitmapByteArrayConversion convert = new BitmapByteArrayConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.volleyHelper = AppController.getVolleyHelper();
        this.progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        this.imageCollectionList = (ArrayList<ImageCollection>) intent.getSerializableExtra("collection");

        for (ImageCollection item : imageCollectionList) {
            checkedCollection.add(item);
        }

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
            progressDialog.setMessage("Fetching data...");

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseJson) {
                    if (responseJson != null) {
                        setGridView();
                        progressDialog.hide();
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

        ProductAdapter productAdapter = new ProductAdapter(this, collection);
        gridView.setAdapter(productAdapter);

    }

    public void onRbImageChecked (View view){
        boolean checked = ((RadioButton) view).isChecked();
        int position = (Integer)view.getTag();
        ImageCollection col = collection.get(position);
        if(checked){
            checkedCollection.add(col);
        } else  {
            checkedCollection.remove(col);
        }
    }

    public void btnClick (View view) {

        if(checkedCollection.size() != 0){

            ProgressDialog d = new ProgressDialog(this);
            d.setMessage("Processing images, please wait");
            d.show();
            for(int i = 0; i < checkedCollection.size(); i++){

                int id = getResources().getIdentifier("resource"+i, "id", getPackageName());

                NetworkImageView image = (NetworkImageView)findViewById(id);
                Bitmap bmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

                checkedCollection.get(i).setImageByte(convert.toByteArray(bmap));
            }

            d.hide();

            Intent intent = new Intent(this, ProductPhotoActivity.class);
            intent.putExtra("collection", checkedCollection);
            this.startActivity(intent);
        } else
            Toast.makeText(getApplicationContext(), "Geen product geselecteerd", Toast.LENGTH_SHORT).show();

    }
    
}
