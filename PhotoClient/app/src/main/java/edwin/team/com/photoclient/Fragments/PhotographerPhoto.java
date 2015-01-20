package edwin.team.com.photoclient.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.Activities.PhotographerCollectionActivity;
import edwin.team.com.photoclient.Classes.Album;
import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ImageAdapter;
import edwin.team.com.photoclient.Classes.ImageCollection;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.Interface.AlbumChangedEvent;
import edwin.team.com.photoclient.Interface.ChooseAlbumDialogListener;
import edwin.team.com.photoclient.Interface.PhotoGrapherPhotoEvent;
import edwin.team.com.photoclient.R;


public class PhotographerPhoto extends Fragment
        implements  View.OnClickListener,
                    PhotoGrapherPhotoEvent,
                    ChooseAlbumDialogListener{

    private GridView gview = null;
    private VolleyHelper volleyHelper = null;
    private ArrayList<ImageCollection> collection;
    private ArrayList<ImageCollection> checkedCollection = new ArrayList<ImageCollection>();
    private ArrayList<Album> albums = new ArrayList<Album>();
    private ProgressDialog pDialog;
    private Album chosenAlbum;
    private ImageAdapter iAdapter;
    private String albumid = "";


    public PhotographerPhoto() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photographer_photo, container, false);

        Bundle bundle = this.getArguments();
        if(bundle != null)
            this.albumid = bundle.getString("albumid");



        this.volleyHelper = AppController.getVolleyHelper();
        gview =(GridView)rootView.findViewById(R.id.gridviewPhotographerPhoto);
        Button btnToAlbum = (Button)rootView.findViewById(R.id.btnToAlbum);
        if(!albumid.isEmpty()){
            btnToAlbum.setVisibility(View.INVISIBLE);
        }else
            btnToAlbum.setVisibility(View.VISIBLE);

        Button btnDeletePhoto = (Button)rootView.findViewById(R.id.btnDeletePhoto);
        btnDeletePhoto.setOnClickListener(this);
        btnToAlbum.setOnClickListener(this);

        getImageCollections();
        return rootView;
    }

    private void getImageCollections() {
        collection = new ArrayList<ImageCollection>();
        String methodName =  "photographer/getimages";
        JSONObject json = new JSONObject();
        try {
            json.put("uid", General.USERID);
            if(!albumid.isEmpty()){
                json.put("albumid",albumid);
            }

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("array");

                        for(int i= 0; i < jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ImageCollection coll = new ImageCollection(
                                    obj.getString("location"),
                                    obj.getString("id"),null,obj.getInt("sold"),obj.optDouble("profit"));
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
        iAdapter = new ImageAdapter(getActivity(),collection);
        gview.setAdapter(iAdapter);
    }

    private void createAlbumJSONObject(String albumId){
        try{

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            jsonObject.put("albumid",albumId);
            jsonObject.put("uid", 17);
            for(ImageCollection img : checkedCollection){
                jsonArray.put(img.getId());
            }
            jsonObject.put("photos", jsonArray);
            unCheckAll();
            saveChangesOnServer(jsonObject);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    private void setAllAlbums(){
        ArrayList<Album> albums = ((PhotographerCollectionActivity)getActivity()).getAllAlbums();
        this.albums = albums;
    }

    private void reloadAlbums(){
        ((PhotographerCollectionActivity)getActivity()).reloadAlbums();
    }

    private void unCheckAll() {
        checkedCollection.clear();
        iAdapter.unCheckEverything();
        iAdapter.notifyDataSetChanged();
    }

    private void saveChangesOnServer(JSONObject jsonObject){
            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Boolean result = response.optBoolean("result");
                    pDialog.hide();
                    if(result) {
                        Toast.makeText(getActivity().getApplicationContext(), "Photo's moved!", Toast.LENGTH_SHORT).show();
                        ((AlbumChangedEvent)getActivity()).OnAlbumChangedEvent();
                        checkedCollection.clear();
                    }
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "Error moving photo's, please try again", Toast.LENGTH_SHORT).show();

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                    volleyError.printStackTrace();
                    pDialog.hide();
                    Toast.makeText(getActivity().getApplicationContext(),"An error occurred with the server please try again later",Toast.LENGTH_SHORT).show();
                }
            };

            this.volleyHelper.post("photographer/photostoalbum",jsonObject,volleyListener,errorListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(PhotographerPhoto.this.getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnToAlbum:
                    openAlbumDialog();
                break;
            case R.id.btnDeletePhoto:
                    confirmPhotoDelete();
                break;
        }
    }

    private void confirmPhotoDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                try {
                    deletePhoto();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deletePhoto() throws JSONException{

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(ImageCollection img : checkedCollection){
            jsonArray.put(img.getId());
            collection.remove(img);
        }
        jsonObject.put("array", jsonArray);

        pDialog.setMessage("Removing photo's please wait...");

        Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean result = response.optBoolean("result");
                pDialog.hide();
                if(result) {
                    Toast.makeText(getActivity().getApplicationContext(), "Photo's deleted!", Toast.LENGTH_SHORT).show();
                    unCheckAll();
                    reloadAlbums();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Error Deleting photo's, please try again", Toast.LENGTH_SHORT).show();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("VolleyError", "Error: " + volleyError.getMessage());
                volleyError.printStackTrace();
                pDialog.hide();
                Toast.makeText(getActivity().getApplicationContext(),"An error occurred with the server please try again later",Toast.LENGTH_SHORT).show();
            }
        };

        pDialog.show();
        this.volleyHelper.post("photographer/deletephoto",jsonObject,volleyListener,errorListener);
    }

    private void openAlbumDialog(){
        if(checkedCollection.size() != 0){
            setAllAlbums();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            Bundle bundle = new Bundle();
            bundle.putSerializable("album", albums);


            AlbumOverviewDialog dialog = new AlbumOverviewDialog();
            dialog.setArguments(bundle);
            dialog.show(ft, "dialog");
        }
        else
            Toast.makeText(getActivity(),"No images choosen",Toast.LENGTH_SHORT);

    }

    @Override
    public void onCheckedEvent(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        int position = (Integer)view.getTag();
        ImageCollection col = collection.get(position);
        if(checked){
            checkedCollection.add(col);
        } else  {
            checkedCollection.remove(col);
        }
    }

    @Override
    public void onChooseAlbumDialog(int positionInArray) {
        pDialog.setMessage("Moving to album please wait...");
        this.chosenAlbum = albums.get(positionInArray);
        chosenAlbum.setAmount(chosenAlbum.getAmount() + checkedCollection.size());
        createAlbumJSONObject(chosenAlbum.getToken());
        pDialog.show();
    }


    public static class AlbumOverviewDialog extends DialogFragment implements AdapterView.OnItemClickListener {
        private ChooseAlbumDialogListener callback;
        private Album album;
        private View view;
        private ArrayList<Album> albums;

        public AlbumOverviewDialog(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
            Bundle bundle = this.getArguments();
            this.albums = (ArrayList<Album>)bundle.getSerializable("album");
            view = inflater.inflate(R.layout.choose_album_layout, container);

            ListView listView = (ListView)view.findViewById(R.id.albumListView);
            listView.setOnItemClickListener(this);
            callback = (ChooseAlbumDialogListener) getFragmentManager().getFragments().get(1);

            ArrayList<String> albumNames = new ArrayList<String>();
            for(Album a: albums){ albumNames.add(a.getName()); }

            listView.setAdapter(new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_list_item_1,albumNames)
            );



            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            return view;
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            callback.onChooseAlbumDialog(position);
            this.dismiss();
        }
    }
}
