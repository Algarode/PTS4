package edwin.team.com.photoclient.Classes;


import android.os.StrictMode;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class General {
      static String IPADRESS = "192.168.99.118";
      static String PORT = "8080";
      static String FULLAPIPATH = "http://"+IPADRESS+":"+PORT+"/FotoproducentAPI/api/";
      public static int USERID = 0;
      public static int USERROLE = 0;

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
}
