package utils;

import android.net.Uri;

import com.bartovapps.simplewebserv.Constants;

/**
 * Created by BartovMoti on 12/06/16.
 */
public class Utils {

    public static Uri buildRestQueryString(String searchParam){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.API_SCHEMA)
                .authority(Constants.API_AUTHORITY)
                .appendPath(Constants.API_SERVICES)
                .appendPath(Constants.API_REST)
                .appendQueryParameter(Constants.API_METHOD_PARAM, Constants.API_PHOTO_SEARCH_METHOD)
                .appendQueryParameter(Constants.API_KEY_URL_PARAM, Constants.API_KEY)
                .appendQueryParameter(Constants.API_FORMAT_PARAM, Constants.API_JSON_FORMAT)
                .appendQueryParameter(Constants.API_TAGS_URL_PARAM, searchParam);

        return builder.build();
    }

    public static String getJsonContentFromFlickerRespose(String flickerResponse){
        flickerResponse = flickerResponse.substring(flickerResponse.indexOf("(") + 1);
        flickerResponse = flickerResponse.substring(0, flickerResponse.length()-1);
        return flickerResponse;
    }
}
