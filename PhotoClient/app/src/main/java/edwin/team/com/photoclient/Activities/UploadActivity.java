package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edwin.team.com.photoclient.Classes.General;
import edwin.team.com.photoclient.Classes.ServerManager;
import edwin.team.com.photoclient.Classes.UploadManager;
import edwin.team.com.photoclient.R;

public class UploadActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private ImageView uploadImageView;
    private Bitmap bmPicture = null;
    private Button upload;
    private EditText etName = null;

    private static final String LINE_FEED = "\r\n";
    PrintWriter writer;
    OutputStream ostream;
    final String boundary = "*****"+Long.toString(System.currentTimeMillis())+"*****";
    final String twoHyphens = "--";
    DataOutputStream dataoutputstream;
    FileInputStream fStream;
    ArrayList<File> files = new ArrayList<File>();



    public UploadActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        etName = (EditText)findViewById(R.id.etName);
        etName.setText(getUniqueId());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        upload = (Button)findViewById(R.id.upload_button);
        upload.setClickable(true);
    }

    private String getUniqueId(){
        String result = "";
        try {
            result = new ServerManager().execute("upload/getunique").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }



    public void selectImage(View view) {
        String text = etName.getText().toString();
        if(text.length() > 0){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }
        else{
            Toast.makeText(this,"Please insert the name before you select a picture",Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            if(requestCode == SELECT_PICTURE)
            {
                int clipCount = data.getClipData().getItemCount();
                for(int i=0; i < clipCount; i++){
                    String path = getRealPathFromURI(data.getClipData().getItemAt(i).getUri());
                    File f = new File(path);

                    files.add(f);
                    try {
                        ImageView img = new ImageView(this);
                        Bitmap bmp = BitmapFactory.decodeFile(path);

                        img.setImageBitmap(bmp);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                500);
                        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        img.setPadding(0,0,0,15);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.imageviewUpload);
                        layout.addView(img, 0, params);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void uploadData(String json) throws ExecutionException, InterruptedException, JSONException {
        String result = new ServerManager().execute(json,"upload/image").get();
        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.optString("result") == "true"){
            finish();
        }
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getRealPathFromURI(Uri contentUri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri,proj,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void upload(View view) throws ExecutionException, InterruptedException, JSONException, IOException {

        HashMap<String, Object> map =  new HashMap<String, Object>();
        map.put("token","took");
        map.put("files",files);

        String s = new UploadManager().execute(map).get();
        if(!General.isResultGood(s)){
            //Hier nog wat doen
        }
    }
}
