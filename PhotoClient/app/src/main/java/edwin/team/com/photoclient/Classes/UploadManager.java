package edwin.team.com.photoclient.Classes;

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

/**
 * Created by Hafid on 14-10-2014.
 */
public class UploadManager extends AsyncTask<HashMap<String,Object>,Integer,String> {

    private static final String LINE_FEED = "\r\n";
    final String boundary = "*****"+Long.toString(System.currentTimeMillis())+"*****";
    final String twoHyphens = "--";
    DataOutputStream dataoutputstream;
    FileInputStream fStream;
    int totalSize;

    @Override
    protected void onProgressUpdate(Integer... values){

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(HashMap<String, Object>... params) {
        String result = "";
        if(params != null){
            HashMap<String, Object> map = params[0];
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
                dataoutputstream.writeBytes("Content-Disposition: form-data; name=\"token\""+LINE_FEED+LINE_FEED+map.get("token")+LINE_FEED);
                dataoutputstream.writeBytes(twoHyphens + boundary + LINE_FEED);
                dataoutputstream.writeBytes("Content-Disposition: form-data; name=\"user\""+LINE_FEED+LINE_FEED+ General.USERID+LINE_FEED); //General.userID
                dataoutputstream.writeBytes(twoHyphens + boundary + LINE_FEED);

                ArrayList<File> files = (ArrayList<File>)map.get("files");

                for(File f : files){
                    addFilePart(f);
                };

                dataoutputstream.writeBytes(twoHyphens+boundary+twoHyphens+LINE_FEED);
                totalSize = dataoutputstream.size();
                fStream.close();
                dataoutputstream.flush();
                dataoutputstream.close();
                //result  = httpcon.getResponseMessage();
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
