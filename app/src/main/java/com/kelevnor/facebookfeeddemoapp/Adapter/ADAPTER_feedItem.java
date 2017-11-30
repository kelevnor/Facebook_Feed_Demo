package com.kelevnor.facebookfeeddemoapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kelevnor.facebookfeeddemoapp.Object.Feed;
import com.kelevnor.facebookfeeddemoapp.R;
import com.kelevnor.facebookfeeddemoapp.Utility.ImageLoader;
import com.kelevnor.facebookfeeddemoapp.Utility.ImageLoaderFeed;
import com.kelevnor.facebookfeeddemoapp.Utility.UtilityHelperClass;

import java.util.ArrayList;

/**
 * Created by mariossifalakis.
 */
public class ADAPTER_feedItem extends BaseAdapter{
    private ArrayList<Feed> searchArrayList;

    ImageLoader imageLoader;
    ImageLoaderFeed imageLoaderFeed;
    Typeface openSansSemiBold,openSansRegular, openSansBold, openSansThin;

    Activity act;
    Context con;
    private LayoutInflater mInflater;

    public ADAPTER_feedItem(Activity act, ArrayList<Feed> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(act.getApplicationContext());
        this.con = act.getApplicationContext();
        this.act = act;
        //Creating fonts

//        imageLoader = new ImageLoader(act.getApplicationContext());

        openSansThin = Typeface.createFromAsset(con.getAssets(),"fonts/OpenSans-Light.ttf");
        openSansRegular = Typeface.createFromAsset(con.getAssets(),"fonts/OpenSans-Regular.ttf");
        openSansSemiBold = Typeface.createFromAsset(con.getAssets(), "fonts/OpenSans-Semibold.ttf");
        openSansBold = Typeface.createFromAsset(con.getAssets(), "fonts/OpenSans-Bold.ttf");

    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Feed getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.feed_item, null);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }


        //set views
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        holder.textstatusmessage = (TextView) convertView.findViewById(R.id.txtStatusMsg);
        holder.textUrl = (TextView) convertView.findViewById(R.id.txtUrl);
        holder.feedImage1 = (ImageView) convertView.findViewById(R.id.feedImage1);
        holder.profile = (ImageView) convertView.findViewById(R.id.profilePic);

        //Setting fonts
        holder.timestamp.setTypeface(openSansThin);
        holder.textstatusmessage.setTypeface(openSansRegular);
        holder.name.setTypeface(openSansSemiBold);
        //populate views
        holder.name.setText(searchArrayList.get(position).getName());
        Log.e("in_item", searchArrayList.get(position).getName());

        holder.textstatusmessage.setText(searchArrayList.get(position).getStatus());
        holder.textUrl.setText(searchArrayList.get(position).getUrl());

        if(searchArrayList.get(position).getTimeStamp() != null && !searchArrayList.get(position).getTimeStamp().isEmpty()){
            Long timeLong = Long.parseLong(searchArrayList.get(position).getTimeStamp());
            holder.timestamp.setText(UtilityHelperClass.getDate(timeLong));
        }

        if (searchArrayList.get(position).getImage() != null && !searchArrayList.get(position).getImage().isEmpty()) {
            holder.feedImage1.setVisibility(View.VISIBLE);
            imageLoaderFeed = new ImageLoaderFeed(act.getApplicationContext());
            holder.feedImage1.setImageBitmap(null);

            imageLoaderFeed.DisplayImage(searchArrayList.get(position).getImage(), holder.feedImage1, act);
            Log.e("image",searchArrayList.get(position).getImage());
        }
        else{
            holder.feedImage1.setVisibility(View.GONE);
            holder.feedImage1.setImageBitmap(null);
            holder.feedImage1.setImageBitmap(null);
        }

        if (searchArrayList.get(position).getProfilePic() != null && !searchArrayList.get(position).getProfilePic().isEmpty()) {
            holder.profile.setVisibility(View.VISIBLE);
            imageLoader = new ImageLoader(act.getApplicationContext());
            holder.feedImage1.setImageBitmap(null);
            imageLoader.DisplayImage(searchArrayList.get(position).getProfilePic(), holder.profile, act);
        }
        else{
            holder.profile.setVisibility(View.INVISIBLE);
            holder.profile.setImageBitmap(null);
            holder.profile.setImageBitmap(null);
        }

        return convertView;
    }



    static class ViewHolder {
        TextView name;
        TextView timestamp;
        TextView textstatusmessage;
        TextView textUrl;
        ImageView feedImage1;
        ImageView profile;
    }



}

