package com.kelevnor.facebookfeeddemoapp.Rest;

import android.os.AsyncTask;
import android.util.Log;

import com.kelevnor.facebookfeeddemoapp.Utility.UtilityHelperClass;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mariossifalakis.
 */
public class REST_getFeed extends AsyncTask<Void, Void, Void> {

    String ANDROIDHIVE_FEED_URL = "https://api.androidhive.info/feed/feed.json";
    boolean completedCall = false;
    String response_passed = "";
    OnAsyncResult onAsyncResult;
    /**
     * @author Marios Sifalakis
     * Class Constructor; Executes a login call
     * ONE DIRECT CALL
     */
    public REST_getFeed(){

    }

    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }
    /**
     * @author Marios Sifalakis
     * @return "VOID"
     *
     * Async task to execute REST Methods (Removes NetworkOnMainThread Exception)
     * It is executed every time a constructor is instantiated
     */

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL(ANDROIDHIVE_FEED_URL);
            Log.d("url", ANDROIDHIVE_FEED_URL);
            HttpsURLConnection urlConnection;
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                response_passed = UtilityHelperClass.GETurlConnectionInputStream(urlConnection);
                completedCall = true;
            }
            else{
                response_passed = UtilityHelperClass.GETurlConnectionErrorStream(urlConnection);
                completedCall = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if (completedCall) {
            onAsyncResult.onResultSuccess(1, response_passed);
        } else {
            onAsyncResult.onResultFail(0, response_passed);
        }
    }

    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, String message);
        void onResultFail(int resultCode, String errorMessage);
    }

}