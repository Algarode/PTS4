package edwin.team.com.photoclient;

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

public class ServerManager extends AsyncTask<String,Void,Void>{

    @Override
    protected void onPreExecute() {
        Log.e("checkpoint", "cp1");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("checkpoint", "cp6");
    }

    @Override
    protected Void doInBackground(String... strings) {
        String json = strings[0];
        String url = "http://192.168.99.118:8080/FotoproducentAPI/api/"+strings[1];
        Log.e("url", url);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        StringEntity input = null;
        try {
            input = new StringEntity(json);

            input.setContentType("application/json");
            post.setEntity(input);
            Log.e("checkpoint", input.toString());
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            String result = convertStreamToString(stream);
            Log.e("res",result);
        } catch (IOException e) {
            Log.e("exception occurred", "cp5");
            e.printStackTrace();
        }
        return null;
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