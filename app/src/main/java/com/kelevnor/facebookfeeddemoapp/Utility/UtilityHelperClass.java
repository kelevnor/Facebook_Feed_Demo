package com.kelevnor.facebookfeeddemoapp.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Locale;

public class UtilityHelperClass {



    public static String GETurlConnectionErrorStream(HttpURLConnection urlConnection){

        byte [] buffer = new byte[8192];
        StringBuilder builder = new StringBuilder();
        String response = "";
        int read;

        try {
            InputStream is = new BufferedInputStream(urlConnection.getErrorStream());
            while ((read = is.read(buffer)) != -1)
            {
                builder.append(new String(buffer, 0, read, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("ERROR STREAM", builder.toString());

        response = builder.toString();

        return response;

    }
    public static String GETurlConnectionInputStream(HttpURLConnection urlConnection){
        byte [] buffer = new byte[8192];
        StringBuilder builder = new StringBuilder();
        String response = "";
        int read;

        try {
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            while ((read = is.read(buffer)) != -1)
            {
                builder.append(new String(buffer, 0, read, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("INPUT STREAM", builder.toString());

        response = builder.toString();

        return response;
    }
    //Method to save in Mem SharedPreferences
    public void saveInPreference(Activity act, String name, String content) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(act);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, content);
        editor.commit();
    }

    //Method to get from Mem SharedPreferences
    public String getFromPreference(Activity act, String variable_name) {
        String preference_return;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(act);
        preference_return = preferences.getString(variable_name, "");

        return preference_return;
    }

    public boolean haveNetworkConnection(Activity act) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    //Simple way to check the overall internet connection of the device
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean checkInternetAvailability(Activity act) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = android.text.format.DateFormat.format("MM-dd-yyyy", cal).toString();
        return date;
    }
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {

            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                //Read byte from input stream

                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;

                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}