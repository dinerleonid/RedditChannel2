package com.example.leonwork.redditchannel;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FavoritesDBHelper extends SQLiteOpenHelper{
    private static final String FAVORITES_TABLE_TITLE = "favoritesTable";
    private static final String FAVORITES_ID = "idInternal";
    private static final String FAVORITES_TITLE = "topicTitle";
    private static final String FAVORITES_TOPIC_URL = "topicUrl";
    private static final String FAVORITES_THUMBNAIL_URL = "topicThumbnailUrl";
    private static final String FAVORITES_TOPIC_ID = "topicID";


    public FavoritesDBHelper(Context context) {
        super(context, "favoriteslist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = String.format("CREATE TABLE %s " +
                "( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT )",
                FAVORITES_TABLE_TITLE,
                FAVORITES_ID,
                FAVORITES_TITLE,
                FAVORITES_TOPIC_URL,
                FAVORITES_THUMBNAIL_URL,
                FAVORITES_TOPIC_ID);
        db.execSQL(sql);
    }

    public long insertFavorite(Channel favorite){
        ContentValues values = new ContentValues();
        values.put(FAVORITES_TITLE, favorite.getTopicTitle());
        values.put(FAVORITES_TOPIC_URL, favorite.getTopicUrl());
        values.put(FAVORITES_THUMBNAIL_URL, favorite.getThumbnailUrl());
        values.put(FAVORITES_TOPIC_ID, favorite.getTopicID());
        SQLiteDatabase db = getWritableDatabase();
        long newID = db.insert(FAVORITES_TABLE_TITLE, null, values);
        db.close();
        return newID;
    }

    public ArrayList<Channel> getAllFavorites(){
        ArrayList<Channel> favorites = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(FAVORITES_TABLE_TITLE, null, null, null, null,null, FAVORITES_TITLE);
        while(c.moveToNext()){
            Long idInternal = c.getLong( c.getColumnIndex(FAVORITES_ID) );
            String topicTitle = c.getString( c.getColumnIndex(FAVORITES_TITLE) );
            String topicUrl = c.getString(c.getColumnIndex(FAVORITES_TOPIC_URL));
            String topicThumbnailUrl = c.getString(c.getColumnIndex(FAVORITES_THUMBNAIL_URL));
            String topicID = c.getString(c.getColumnIndex(FAVORITES_TOPIC_ID));
            favorites.add(new Channel(topicTitle, topicThumbnailUrl, topicUrl, idInternal, topicID));
        }
        db.close();
        return favorites;
    }

    public void deleteFavorite(long idInternal){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FAVORITES_TABLE_TITLE, FAVORITES_ID + "=" + idInternal, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
