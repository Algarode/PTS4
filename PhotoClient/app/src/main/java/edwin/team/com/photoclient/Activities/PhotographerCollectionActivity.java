package edwin.team.com.photoclient.Activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.Album;
import edwin.team.com.photoclient.Fragments.PhotographerAlbum;
import edwin.team.com.photoclient.Fragments.PhotographerPhoto;
import edwin.team.com.photoclient.Interface.AlbumChangedEvent;
import edwin.team.com.photoclient.R;

public class PhotographerCollectionActivity extends FragmentActivity
        implements ActionBar.TabListener,
                    AlbumChangedEvent{

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "Album's", "Photo's" };

    private PhotographerPhoto photographerPhoto;
    private PhotographerAlbum photographerAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TabViewActionBar);
        setContentView(R.layout.activity_photographer_collection);

        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle("");
        actionBar.setIcon(null);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String name : tabs){
            actionBar.addTab(actionBar.newTab().setText(name)
                .setTabListener(this));
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                //Anders verandert die niet van tab
                actionBar.setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public ArrayList<Album> getAllAlbums(){
        if(photographerAlbum != null){
            ArrayList<Album> album = photographerAlbum.getAlbums();
            if(album != null) return album;
        }
        return new ArrayList<Album>();
    }

    public void onCbImageChecked(View v){
        if(photographerPhoto != null){
            photographerPhoto.onCheckedEvent(v);
        }
    }

    public void reloadAlbums(){
        if(photographerAlbum != null)
            photographerAlbum.downloadAlbums();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.photographer_collection, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void OnAlbumChangedEvent() {
        if(photographerAlbum != null){
            photographerAlbum.OnAlbumChangedEvent();
        }
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter{

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    photographerAlbum = new PhotographerAlbum();
                    return photographerAlbum;
                case 1:
                    photographerPhoto = new PhotographerPhoto();
                    return photographerPhoto;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
