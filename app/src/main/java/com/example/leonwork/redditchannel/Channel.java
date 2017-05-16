package com.example.leonwork.redditchannel;

import android.os.Parcel;
import android.os.Parcelable;



public class Channel implements Parcelable{

    private String topicTitle, thumbnailUrl, topicUrl, topicID;
    private long idInternal;

    public Channel(String topicTitle, String thumbnailUrl, String topicUrl, long idInternal, String topicID) {
        this.topicTitle = topicTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.topicUrl = topicUrl;
        this.idInternal = idInternal;
        this.topicID = topicID;
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) { return new Channel(in); }

        @Override
        public Channel[] newArray(int size) { return new Channel[size]; }
    };

    public String getTopicTitle() { return topicTitle; }

    public void setTopicTitle(String topicTitle) { this.topicTitle = topicTitle; }

    public String getThumbnailUrl() { return thumbnailUrl; }

    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getTopicUrl() { return topicUrl; }

    public void setTopicUrl(String topicUrl) { this.topicUrl = topicUrl; }

    public long getIdInternal() { return idInternal; }

    public void setIdInternal(long idInternal) { this.idInternal = idInternal; }

    public String getTopicID() { return topicID; }

    public void setTopicID(String topicID) { this.topicID = topicID; }

    @Override
    public int describeContents() { return 0; }

    protected Channel(Parcel in) {
        topicTitle = in.readString();
        thumbnailUrl = in.readString();
        topicUrl = in.readString();
        idInternal = in.readLong();
        topicID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topicTitle);
        dest.writeString(thumbnailUrl);
        dest.writeString(topicUrl);
        dest.writeLong(idInternal);
        dest.writeString(topicID);
    }
}
