package com.kelevnor.facebookfeeddemoapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.kelevnor.facebookfeeddemoapp.Adapter.ADAPTER_feedItem;
import com.kelevnor.facebookfeeddemoapp.Object.Feed;
import com.kelevnor.facebookfeeddemoapp.Object.FeedItem;
import com.kelevnor.facebookfeeddemoapp.Rest.REST_getFeed;
import com.kelevnor.facebookfeeddemoapp.Utility.UtilityHelperClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST = 112;
    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
    public static ArrayList<Feed> listOfFeed;
    ListView list;
    TextView nointernetlabel;
    Typeface openSansSemiBold,openSansRegular, openSansBold, openSansThin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST );
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    UtilityHelperClass.oneButtonBuilder(MainActivity.this, getResources().getString(R.string.neutral_btn), getResources().getString(R.string.no_permissions_title), getResources().getString(R.string.no_permissions_message));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        UtilityHelperClass.twoButtonsBuilder(MainActivity.this, getResources().getString(R.string.positive_btn), getResources().getString(R.string.negative_btn), getResources().getString(R.string.exit_title), getResources().getString(R.string.exit_message), UtilityHelperClass.TYPE_EXIT);
    }

    private void setViews(){
        openSansRegular = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        openSansThin = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");
        openSansSemiBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");
        openSansBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
        list = (ListView) findViewById(R.id.lv_list);
        nointernetlabel = (TextView) findViewById(R.id.tv_nointernetlabel);
        nointernetlabel.setTypeface(openSansSemiBold);
        if(UtilityHelperClass.checkInternetAvailability(this)){
            //GET Call to retreive feed
            list.setVisibility(View.VISIBLE);
            REST_getFeed getFeed = new REST_getFeed();
            getFeed.setOnResultListener(asynResult);
            getFeed.execute();
        }
        else{
            list.setVisibility(View.GONE);
            //if no internet action
            UtilityHelperClass.oneButtonBuilder(MainActivity.this, getResources().getString(R.string.neutral_btn), getResources().getString(R.string.no_internet_title), getResources().getString(R.string.no_internet_message));
        }
    }

    private void populateListOfFeed (JSONArray outerArray, String result){
        Gson gson = new Gson();
        FeedItem feedItem = gson.fromJson(result, FeedItem.class);
        listOfFeed = feedItem.getFeed();
        //Setting listOfFeed in my ADAPTER to display values
        list.setAdapter(new ADAPTER_feedItem(MainActivity.this, listOfFeed));
    }

    //On result listener for REST_getFeed async task
    REST_getFeed.OnAsyncResult asynResult = new REST_getFeed.OnAsyncResult() {
        @Override
        public void onResultSuccess(int resultCode, String result) {
            Log.d("SUCCESS", result);

            JSONObject outer = null;
            JSONArray outerArray = null;
            listOfFeed = new ArrayList<>();
            try {
                outer = new JSONObject(result);
                outerArray = outer.optJSONArray("feed");
                //method to populate list of feed
                populateListOfFeed(outerArray, result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.d("FAIL", errorResult);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, with specified parent
        // activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh_feed) {
            //GET Call to retreive feed
            if(UtilityHelperClass.checkInternetAvailability(this)){
                list.setVisibility(View.VISIBLE);
                //GET Call to retreive feed
                REST_getFeed getFeed = new REST_getFeed();
                getFeed.setOnResultListener(asynResult);
                getFeed.execute();
            }
            else{
//              list.setVisibility(View.GONE);        //keep if we want to remove outdated feed from list
                //if no internet action
                UtilityHelperClass.oneButtonBuilder(MainActivity.this, getResources().getString(R.string.neutral_btn), getResources().getString(R.string.no_internet_title), getResources().getString(R.string.no_internet_message));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
