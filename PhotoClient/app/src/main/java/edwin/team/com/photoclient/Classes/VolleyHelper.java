package edwin.team.com.photoclient.Classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by rob on 9-11-2014.
 */
public class VolleyHelper {

    private final Context context;
    private final RequestQueue requestQueue;
    private final ImageLoader imageLoader;
    private final String url;

    // called once in the appController oncreate which will be fired on application startup
    public VolleyHelper(Context c, String baseUrl){
        this.context = c;
        this.url = baseUrl;
        this.requestQueue = AppController.getInstance().getRequestQueue();
        this.imageLoader = AppController.getInstance().getImageLoader();
    }

    // Returns the apipath including the specified methodpath
    private String constructURL(String MethodPath){
        return this.url+MethodPath;
    }

    public void post(String method, JSONObject jsonRequest,
                     Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.POST, constructURL(method), jsonRequest, listener, errorListener)
        {
            @Override public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }
        };
        requestQueue.add(objRequest);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }


}
