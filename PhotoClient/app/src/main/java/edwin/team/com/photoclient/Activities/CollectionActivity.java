package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ImageAdapter;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.Classes.Price;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.R;

public class CollectionActivity extends Activity {

    private ArrayList<ImageCollection> collection;
    private ArrayList<ImageCollection> checkedCollection = new ArrayList<ImageCollection>();
    private GridView gview = null;
    private VolleyHelper volleyHelper = null;
    private BitmapByteArrayConversion convert = new BitmapByteArrayConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        this.volleyHelper = AppController.getVolleyHelper();
        gview =(GridView)findViewById(R.id.gridview);
        gview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        getImageCollections();
    }

    private void getImageCollections() {
        collection = new ArrayList<ImageCollection>();
        String methodName =  "generic/getCollection";
        JSONObject json = new JSONObject();
        try {
            json.put("uid", General.USERID);

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try{
                        for(Iterator<String> iter = jsonObject.keys();iter.hasNext();) {
                            String key = iter.next();
                            ArrayList<Price> prices = new ArrayList<Price>();
                            JSONObject obj = (JSONObject)jsonObject.get(key);
                            JSONObject po = (JSONObject)obj.get("price");
                            for(int i = 1; i <=4; i++){
                                JSONObject priceObject = (JSONObject)po.get("price"+i);
                                prices.add(new Price(priceObject.optInt("width"),priceObject.optInt("height"),priceObject.optDouble("price"),priceObject.optInt("sizePriceID")));
                            }
                            ImageCollection coll = new ImageCollection(
                                    obj.getString("location"),
                                    obj.getString("id"),prices);
                            collection.add(coll);
                        }
                    }catch(JSONException ex ){
                        ex.printStackTrace();
                    }
                    finally {
                        setGridAdapter();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            };

            this.volleyHelper.post(methodName,json,volleyListener,errorListener);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setGridAdapter(){
        ImageAdapter iAdapter = new ImageAdapter(this,collection);
        gview.setAdapter(iAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCbImageChecked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        int position = (Integer)view.getTag();
        ImageCollection col = collection.get(position);
        if(checked){
            checkedCollection.add(col);
        } else  {
            checkedCollection.remove(col);
        }
    }

    public void btnProductConfirm(View view){
        if(checkedCollection.size() != 0){

            ProgressDialog d = new ProgressDialog(this);
            d.setMessage(String.valueOf(R.string.processing_images));
            d.show();
            for(int i = 0; i < checkedCollection.size(); i++){

                int id = getResources().getIdentifier("resource"+i, "id", getPackageName());

                NetworkImageView image = (NetworkImageView)findViewById(id);
                Bitmap bmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

                checkedCollection.get(i).setImageByte(convert.toByteArray(bmap));
            }

            d.hide();

            Intent intent = new Intent(this, ProductConfirmationActivity.class);
            intent.putExtra("collection", checkedCollection);
            this.startActivity(intent);
        } else
           Toast.makeText(getApplicationContext(),R.string.no_photos_selected,Toast.LENGTH_SHORT).show();
    }
}
