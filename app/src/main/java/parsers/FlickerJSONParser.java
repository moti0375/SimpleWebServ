package parsers;


import android.net.Uri;
import android.util.Log;

import com.bartovapps.simplewebserv.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BartovMoti on 11/22/14.
 */
public class FlickerJSONParser {
    private static final String LOG_TAG = FlickerJSONParser.class.getSimpleName();


    public static ArrayList<Uri> parseFeed(String content) {
        try {

            JSONObject reader = new JSONObject(content);
            JSONObject photos = reader.getJSONObject("photos");

            JSONArray ar =  photos.getJSONArray("photo");

            Log.i(LOG_TAG, "Json array size " + ar.length());

            ArrayList<Uri> flickerImages = new ArrayList<Uri>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Uri.Builder builder = new Uri.Builder();
                Uri uri = buildPhotoUri(obj);

                if(uri != null){
                    flickerImages.add(uri);
                    Log.i(LOG_TAG, uri.toString());

                }


            }
            return flickerImages;
        }catch (JSONException e) {
                e.printStackTrace();
                return null;
        }
    }

    static Uri buildPhotoUri(JSONObject obj){
        Uri.Builder builder = new Uri.Builder();
        try{
            builder.scheme(Constants.API_SCHEMA)
                    .authority("farm" + obj.getString("farm") + "." +Constants.API_STATIC_FLICKER)
                    .appendPath( obj.getString("server") )
                    .appendPath(obj.getString("id") + "_" + obj.getString("secret") + ".jpg");

            return builder.build();
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }


    }
}
