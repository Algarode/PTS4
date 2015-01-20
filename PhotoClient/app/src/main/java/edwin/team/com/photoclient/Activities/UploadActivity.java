package edwin.team.com.photoclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    ArrayList<File> files = new ArrayList<File>();

    public UploadActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        upload = (Button)findViewById(R.id.upload_button);
        upload.setClickable(true);
    }




    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent, getApplicationContext().getString(R.string.select_picture)), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            if(requestCode == SELECT_PICTURE)
            {
                LinearLayout overviewLayout = (LinearLayout)findViewById(R.id.uploadimageoverivew);

                LinearLayout.LayoutParams linparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                linparams.weight = 5.0f;
                overviewLayout.setLayoutParams(linparams);

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
        UploadManager umanager;
        if(files.size() != 0){
            umanager = new UploadManager(this);
            umanager.setOnTaskFinishedEvent(new UploadManager.OnTaskExecutionFinished() {
                @Override
                public void OnTaskFinishedEvent(String Reslut) {
                    Toast t;
                    if(General.isResultGood(Reslut)) {
                        t = Toast.makeText(getApplicationContext(),R.string.uploading_succeeded,Toast.LENGTH_LONG);
                    } else
                        t = Toast.makeText(getApplicationContext(),R.string.error_uploading,Toast.LENGTH_LONG);

                    t.show();
                }
            });

            umanager.execute(files);
        } else {
            Toast t = Toast.makeText(this,R.string.select_images_first,Toast.LENGTH_SHORT);
            t.show();
        }
    }
}
