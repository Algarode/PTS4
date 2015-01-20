package edwin.team.com.photoclient.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edwin.team.com.photoclient.Classes.Album;
import edwin.team.com.photoclient.Classes.AlbumAdapter;
import edwin.team.com.photoclient.Classes.AppController;
import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.VolleyHelper;
import edwin.team.com.photoclient.Interface.AlbumChangedEvent;
import edwin.team.com.photoclient.Interface.EditDialogListener;
import edwin.team.com.photoclient.R;


public class PhotographerAlbum extends Fragment implements
        View.OnClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        EditDialogListener,
        AlbumChangedEvent{

    private VolleyHelper volleyHelper = null;
    private ProgressDialog pDialog;
    private View rootView;
    private GridView gview = null;
    private ArrayList<Album> albums;
    private AlbumAdapter iAdapter = null;
    int chosenAlbum = 0;

    public PhotographerAlbum() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_photographer_album, container, false);

        gview = (GridView)rootView.findViewById(R.id.gridviewPhotographerAlbum);
        gview.setOnItemLongClickListener(this);
        gview.setOnItemClickListener(this);
        Button b = (Button)rootView.findViewById(R.id.btnCreateAlbum);
        b.setOnClickListener(this);

        downloadAlbums();

        /* Debug when no internet is available */
        //albums = new ArrayList<Album>();
        //albums.add(new Album(
        //        "Test","test",0
        //));
        //setGridAdapter();



        return rootView;
    }

    public void btnCreateAlbum (View v) throws JSONException {
        EditText edittext = (EditText)getView().findViewById(R.id.etAlbumName);
        String albumname = edittext.getText().toString();

        if(!albumname.isEmpty()){
            JSONObject json = new JSONObject();
            json.put("uid", General.USERID);
            json.put("name",albumname);
            pDialog.setMessage(getActivity().getApplicationContext().getString(R.string.creating_album));

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Boolean result = response.optBoolean("result");
                    pDialog.hide();
                    if(result) {
                        Toast.makeText(getActivity().getApplicationContext(), "Album created!", Toast.LENGTH_SHORT).show();
                        albums.add(new Album(
                                response.optString("name"),
                                response.optString("token"),
                                0
                        ));
                        iAdapter.notifyDataSetChanged();
                    }
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "Error creating album, please try again", Toast.LENGTH_SHORT).show();

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
            this.volleyHelper.post("photographer/createalbum",json,volleyListener,errorListener);

        }
        else
            Toast.makeText(v.getContext(),"No album name",Toast.LENGTH_SHORT).show();

        edittext.setText("");
    }

    public void downloadAlbums(){
        albums = new ArrayList<Album>();
        final JSONObject json = new JSONObject();
        try {
            json.put("uid", General.USERID);

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("array");

                        for(int i= 0; i < jsonArray.length(); i++){
                            JSONObject obj = jsonArray.getJSONObject(i);
                            albums.add(new Album(
                                    obj.getString("name"),
                                    obj.getString("token"),
                                    obj.getInt("amount")
                            ));
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

            this.volleyHelper.post("photographer/getalbum",json,volleyListener,errorListener);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setGridAdapter(){
        if(iAdapter == null) {
            iAdapter = new AlbumAdapter(getActivity(), albums);
            gview.setAdapter(iAdapter);
        }else
            iAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.volleyHelper = AppController.getVolleyHelper();
        pDialog = new ProgressDialog(PhotographerAlbum.this.getActivity());
    }


    public ArrayList<Album> getAlbums(){
        return albums;
    }


    /*
        Listeners
     */
    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        switch (v.getId()){
            case R.id.btnCreateAlbum:
                try {
                    btnCreateAlbum(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.chosenAlbum = position;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putSerializable("album",albums.get(position));

        EditAlbumDialog dialog = new EditAlbumDialog();
        dialog.setArguments(bundle);
        dialog.show(ft,"dialog");
        return false;
    }

    @Override
    public void onFinishEditDialog(String action, Album album) {

        if(action.equals("name")){
            editAlbumName(album);
        } else if(action.equals("delete")){
            deleteAlbum(album);
        }
    }

    private void editAlbumName(final Album album){

        if(!album.getName().isEmpty()){
            final JSONObject json = new JSONObject();
            try {
                json.put("token", album.getToken());
                json.put("name", album.getName());
                pDialog.setMessage(getActivity().getApplicationContext().getString(R.string.editing_album));

                Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        pDialog.hide();
                        if(General.isResultGood(jsonObject.toString())){
                            albums.get(chosenAlbum).setName(album.getName());
                            iAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity().getApplicationContext(), "Album name succesfully changed", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getActivity().getApplicationContext(), "Error changing name, please try again", Toast.LENGTH_SHORT).show();
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        pDialog.hide();
                        Toast.makeText(getActivity().getApplicationContext(),"An error occurred with the server please try again later",Toast.LENGTH_SHORT).show();
                    }
                };
                pDialog.show();
                this.volleyHelper.post("photographer/editalbumname",json,volleyListener,errorListener);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else
            Toast.makeText(getActivity().getApplicationContext(),"You need to enter a name",Toast.LENGTH_SHORT).show();

    }

    private void deleteAlbum(final Album album){
        final JSONObject json = new JSONObject();
        try {
            json.put("token", album.getToken());

            pDialog.setMessage("Deleting the album please wait...");

            Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    pDialog.hide();
                    if(General.isResultGood(jsonObject.toString())){
                        albums.remove(album);
                        iAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity().getApplicationContext(), "Succesfully deleted the album!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getActivity().getApplicationContext(), "Error deleting album, please try again", Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    pDialog.hide();
                    Toast.makeText(getActivity().getApplicationContext(),"An error occurred with the server please try again later",Toast.LENGTH_SHORT).show();
                }
            };
            pDialog.show();
            this.volleyHelper.post("photographer/deletealbum",json,volleyListener,errorListener);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("albumid",albums.get(position).getToken());

        Fragment newFragment = new PhotographerPhoto();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_photographer_album, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void OnAlbumChangedEvent() {
        iAdapter.notifyDataSetChanged();
    }

    public static class EditAlbumDialog extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener {
        private EditDialogListener callback;
        private Album album;
        private View view;


        public EditAlbumDialog(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
            Bundle bundle = this.getArguments();
            this.album = (Album)bundle.getSerializable("album");
            view = inflater.inflate(R.layout.edit_album_layout, container);

            Button b = (Button)view.findViewById(R.id.btnDeleteAlbum);
            Button be = (Button)view.findViewById(R.id.saveNewName);
            b.setOnClickListener(this);
            be.setOnClickListener(this);

            callback = (EditDialogListener) getFragmentManager().getFragments().get(0);




            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            return view;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                return false;
            }
            return false;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnDeleteAlbum:
                    callback.onFinishEditDialog("delete", album);
                    break;
                case R.id.saveNewName:
                    EditText newName = (EditText)view.findViewById(R.id.newAlbumName);
                    album.setName(newName.getText().toString());

                    callback.onFinishEditDialog("name", album);
                    break;
            }
            this.dismiss();
        }
    }
}
