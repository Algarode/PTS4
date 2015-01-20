package edwin.team.com.photoclient.Classes;


import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class General extends AppController {
      static String IPADRESS = "145.93.113.209";
      static String PORT = "8080";
      public static String FULLAPIPATH = "http://"+IPADRESS+":"+PORT+"/FotoproducentAPI/api/";
      public static int USERID = 0;
      public static int ROLEID = 0;
    public static Bitmap bmp;

      public static Boolean reachHost(){
        boolean exists = false;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            SocketAddress sockaddr = new InetSocketAddress(IPADRESS, Integer.parseInt(PORT));
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;
        }catch(Exception e){
            Log.e("HostException","Could not reach the host with adress " + IPADRESS+":"+"port");
            e.printStackTrace();
        }
          return exists;
    }

    public static Boolean isResultGood(String data){
        JSONObject json = null;
        try {
            json = new JSONObject(data);
        } catch (JSONException e) {
            Log.e("Returndata on server incorrect: ","Couldn't convert server returnvalue to jsonobject, verify that the returndata on the server is correct");
            return false;
        }
        Boolean passed = false;
        if(json.optString("result") != null && json.optString("result") != ""){
            String result = json.optString("result");
            if(result.equals("true") || result.equals("false"))
                passed = Boolean.parseBoolean(result);
        }
        return passed;
    }

    public static Bitmap generateBitmap(ImageView v){
        v.setDrawingCacheEnabled(true);

        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }
}
