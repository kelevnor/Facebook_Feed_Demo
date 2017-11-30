
package com.kelevnor.facebookfeeddemoapp.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedItem {

    @SerializedName("feed")
    @Expose
    private ArrayList<Feed> feed = null;

    public ArrayList<Feed> getFeed() {
        return feed;
    }

    public void setFeed(ArrayList<Feed> feed) {
        this.feed = feed;
    }

}
