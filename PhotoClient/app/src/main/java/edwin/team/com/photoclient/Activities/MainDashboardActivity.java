package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Fragments.PhotographerDashboardFragment;
import edwin.team.com.photoclient.Fragments.ProducentDashboardFragment;
import edwin.team.com.photoclient.Fragments.UserDashboardFragment;
import edwin.team.com.photoclient.R;

public class MainDashboardActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);


        if (savedInstanceState == null) {

            Fragment fragment = null;
            switch (General.ROLEID){
                case 3:
                        fragment = new UserDashboardFragment();
                    break;
                case 2:
                        fragment = new PhotographerDashboardFragment();
                    break;
                case 1:
                        fragment = new ProducentDashboardFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startIntent(Activity activity){
        Intent intent = new Intent(this, activity.getClass());
        this.startActivity(intent);
    }

    public void btnEdit(View view){
        startIntent(new ChangeUserInfoActivity());
    }

    public void btnUserCollection(View view){
        startIntent(new CollectionActivity());
    }

    public void btnGoToShoppingcart(View view){ startIntent(new ShoppingCartActivity()); }

    public void btnOnCode(View view){ startIntent(new FillCodeActivity()); }

    public void btnEditPic(View view) { startIntent(new EditPickPhotoActivity());}

    public void btnProductOnImages(View view){ startIntent(new PickProductActivity());}

    //Photograph
    public void btnPhotographCollection(View view){startIntent(new PhotographerCollectionActivity()); }

    public void btnEditPrice(View view){ startIntent(new PhotographerPriceActivity()); }

    public void btnUpload(View view){   startIntent(new UploadActivity());  }

    //Big chief

    public void btnToggle(View view){ startIntent(new SubscriptionActivity());}

    public void btnCheckOrders(View view){ startIntent(new OrderSummaryActivity());}

}
