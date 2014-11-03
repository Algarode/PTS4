package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.ImageAdapter;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.R;

public class CollectionActivity extends Activity {

    private ArrayList<ImageCollection> collection;
    private ArrayList<ImageCollection> checkedCollection = new ArrayList<ImageCollection>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        GridView gview =(GridView)findViewById(R.id.gridview);
        gview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        BitmapByteArrayConversion convert = new BitmapByteArrayConversion();

        ImageCollection a = new ImageCollection(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.camera),1);
        a.setImageByte(convert.toByteArray(a.getImage()));
        ImageCollection b = new ImageCollection(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.camera),1);
        b.setImageByte(convert.toByteArray(b.getImage()));


        collection = new ArrayList<ImageCollection>();
        collection.add(a);
        collection.add(b);

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
            Intent intent = new Intent(this, ProductConfirmationActivity.class);
            intent.putExtra("collection",checkedCollection);
            this.startActivity(intent);
        } else
           Toast.makeText(getApplicationContext(),"Geen foto's geselecteerd",Toast.LENGTH_SHORT).show();
    }
}
