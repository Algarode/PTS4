package edwin.team.com.photoclient.Classes;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerManager extends AsyncTask<String,Void,String>{

    @Override
    protected void onPreExecute() {
        Log.e("checkpoint", "cp1");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        boolean isjson = false;
        String json = "";
        String url = "";
        if(strings.length > 1){
            isjson = true;
            json = strings[0];
            url = General.FULLAPIPATH+strings[1];
        } else {
            url = General.FULLAPIPATH+strings[0];
        }

        Log.e("url", url);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        String result ="";
        StringEntity input = null;
        try {
            if (isjson){
                input = new StringEntity(json);

                input.setContentType("application/json");
                post.setEntity(input);
                Log.e("checkpoint", input.toString());
            }


            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            result = convertStreamToString(stream);
            Log.e("res",result);
        } catch (IOException e) {
            Log.e("exception occurred", "cp5");
            e.printStackTrace();
        }
        return result;
    }

    public static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
