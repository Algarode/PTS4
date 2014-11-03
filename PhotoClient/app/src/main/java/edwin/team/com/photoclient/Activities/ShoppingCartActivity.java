package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import edwin.team.com.photoclient.Classes.ShoppingCart;
import edwin.team.com.photoclient.R;

public class ShoppingCartActivity extends Activity {

    private ListView listview;
    private ShoppingCart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cart = new ShoppingCart();
        cart.addOrderLine(1,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(2,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(3,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(4,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(5,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(6,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(7,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(8,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(9,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(10,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");
        cart.addOrderLine(11,1.55,1,1,getResources().getDrawable( R.drawable.camera ),"test");

        setContentView(R.layout.activity_shoppingcart);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        listview = (ListView)findViewById(R.id.listview_shop);
        listview.setAdapter(
            new ShoppingcartListAdapter(this,cart.getOrders())
        );
    }
}
