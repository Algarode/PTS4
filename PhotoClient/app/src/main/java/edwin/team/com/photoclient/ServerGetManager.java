package edwin.team.com.photoclient;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by stefan on 29-Sep-14.
 */
public class ServerGetManager extends AsyncTask<String,Void,String> {

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
        String url = "http://192.168.1.101:8080/FotoproducentAPI/api/"+strings[0];
        Log.e("url", url);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        String result ="";
        try {
            HttpResponse response = client.execute(get);
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