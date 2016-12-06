package utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BartovMoti on 05/06/16.
 */
public class ConnectionManager {

    private static final String LOG_TAG = ConnectionManager.class.getSimpleName();

    public static String getData(Uri uri){


        BufferedReader reader = null;
        HttpURLConnection con = null;
        Log.i(LOG_TAG, "Getting data Http request");


        try{
            URL url = new URL(uri.toString());
//            OkHttpClient client = new OkHttpClient();
//            HttpURLConnection con = (HttpURLConnection) url.openConnection(); //This code uses the HttpURLConnection class, a part of Java
//            HttpURLConnection con = client.open(url); //This code uses the OkHttpClient which is a part of the OKHttp library.
            con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

}
