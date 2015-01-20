package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.BitmapByteArrayConversion;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.R;

public class ProductConfirmationActivity extends Activity {

    private ListView listView;
    private Button submit;
    private ArrayList<ImageCollection> imageCollectionList;
    private ProductConfirmationAdapter adapter;
    private BitmapByteArrayConversion bbac = new BitmapByteArrayConversion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_confirmation);
        Intent activityThatCalled = getIntent();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.imageCollectionList = (ArrayList<ImageCollection>)activityThatCalled.getSerializableExtra("collection");
        this.listView = (ListView)findViewById(R.id.listViewProducts);
        this.submit = (Button)findViewById(R.id.bt_add_shopping_cart);
        this.updateListview();
    }

    private void updateListview(){
        this.adapter = new ProductConfirmationAdapter(this,this.imageCollectionList);
        this.listView.setAdapter(
                this.adapter
        );
    }

    public void changePrice(int pos,int sizeID){
        ImageCollection col = this.imageCollectionList.get(pos);
        col.setSize(sizeID);
        updateListview();
    }

    public void changeAmount(int pos, int amount){
        ImageCollection col = this.imageCollectionList.get(pos);
        col.setAmount(amount);
        updateListview();
    }

    public void addToShoppingCart(View view) {
        if(imageCollectionList.size() > 0){
            for(ImageCollection col : imageCollectionList){
                AppController.getShoppingCart().addOrderLine(col.getId(),col.getPrice(),col.getSizePriceID(),col.getAmount(),bbac.toBitmap(col.getImageByte()),col.getSizeName());
                this.imageCollectionList.remove(col);
            }
            this.updateListview();
            Toast.makeText(ProductConfirmationActivity.this, R.string.item_added_to_shoppingcart, Toast.LENGTH_SHORT).show();
        }
    }
}
