package edwin.team.com.photoclient.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.RecoverySystem;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import edwin.team.com.photoclient.Activities.UploadActivity;

/**
 * Created by Hafid on 14-10-2014.
 */
public class UploadManager extends AsyncTask<ArrayList<File>,Integer,String> {

    private static final String LINE_FEED = "\r\n";
    final String boundary = "*****"+Long.toString(System.currentTimeMillis())+"*****";
    final String twoHyphens = "--";
    DataOutputStream dataoutputstream;
    FileInputStream fStream;
    int totalSize;
    private final ProgressDialog dialog;
    Activity activity;

    private OnTaskExecutionFinished _task_finished_event;

    public interface OnTaskExecutionFinished
    {
        public void OnTaskFinishedEvent(String Reslut);
    }

    public void setOnTaskFinishedEvent(OnTaskExecutionFinished _event)
    {
        if(_event != null)
        {
            this._task_finished_event = _event;
        }
    }

    public UploadManager(Activity activity){
        this.activity = activity;
        dialog = new ProgressDialog(activity);
    }


    @Override
    protected  void onPreExecute(){
        this.dialog.setMessage("Uploading...");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


    @Override
    protected void onProgressUpdate(Integer... values){

    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
            if(this._task_finished_event != null)
            {
                this._task_finished_event.OnTaskFinishedEvent(result);
            }
        }
    }

    @Override
    protected String doInBackground(ArrayList<File>... params) {
        String result = "";
        if(params != null){
            try {
                URL url = new URL(General.FULLAPIPATH+"upload/uploadImage");
                HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();

                httpcon.setRequestMethod("POST");
                httpcon.setUseCaches(false);
                httpcon.setDoInput(true);
                httpcon.setDoOutput(true);


                RecoverySystem.ProgressListener p = new RecoverySystem.ProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        publishProgress((progress / totalSize) * 100);
                        Log.d("progress",String.valueOf((progress / totalSize) * 100));
                    }
                };

                httpcon.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
                dataoutputstream = new DataOutputStream(httpcon.getOutputStream());
                dataoutputstream.writeBytes(twoHyphens + boundary + LINE_FEED);
                dataoutputstream.writeBytes("Content-Disposition: form-data; name=\"user\""+LINE_FEED+LINE_FEED+ General.USERID+LINE_FEED); //General.userID
                dataoutputstream.writeBytes(twoHyphens + boundary + LINE_FEED);

                ArrayList<File> files = params[0];

                for(File f : files){
                    addFilePart(f);
                };

                dataoutputstream.writeBytes(twoHyphens+boundary+twoHyphens+LINE_FEED);
                totalSize = dataoutputstream.size();
                fStream.close();
                dataoutputstream.flush();
                dataoutputstream.close();
                BufferedReader r = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                result = total.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void addFilePart(File uploadFile)
            throws IOException {
        dataoutputstream.writeBytes(twoHyphens+boundary+LINE_FEED);
        dataoutputstream.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + uploadFile.getName() +"\"" + LINE_FEED);
        dataoutputstream.writeBytes("Content-Type: "+ URLConnection.guessContentTypeFromName(uploadFile.getName()) + LINE_FEED);
        dataoutputstream.writeBytes(LINE_FEED);

        fStream = new FileInputStream(uploadFile);
        int bufferSize = (int)uploadFile.length();
        byte[] buffer = new byte[bufferSize];
        int length = -1;

        while ((length = fStream.read(buffer)) != -1){
            dataoutputstream.write(buffer,0,length);
        }
        dataoutputstream.writeBytes(LINE_FEED);
    }


}
