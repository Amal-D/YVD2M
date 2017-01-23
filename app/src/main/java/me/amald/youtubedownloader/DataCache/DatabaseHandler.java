package me.amald.youtubedownloader.DataCache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import me.amald.youtubedownloader.Util.MLogger;

/**
 * Created by amald on 15/1/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "songsManager";

    // Contacts table name
    private static final String TABLE_SONGS = "songs";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SNAME = "song_name";
    private static final String KEY_SLENGTH = "song_length";
    private static final String KEY_URL = "song_url";
    private static final String KEY_URLDL = "song_dlurl";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_SONGS + "( " + KEY_ID + " INTEGER PRIMARY KEY,"+
                KEY_SNAME + " TEXT," + KEY_SLENGTH + " TEXT," + KEY_URL + " TEXT," + KEY_URLDL + " TEXT" +
                " )";
        db.execSQL(CREATE_TABLE);

        MLogger.debug("tablecreated",CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS"+TABLE_SONGS);
        onCreate(db);
    }

    //DB operations

    public void insertSong(ConvertedMedia convertedMedia){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SNAME,convertedMedia.getSongName());
        values.put(KEY_SLENGTH,convertedMedia.getLength());
        values.put(KEY_URL,convertedMedia.getSongUrl());
        values.put(KEY_URLDL,convertedMedia.getDownloadUrl());

        MLogger.debug("inserted",convertedMedia.getSongName());
        MLogger.debug("inserted",convertedMedia.getLength());
        MLogger.debug("inserted",convertedMedia.getSongUrl());
        MLogger.debug("inserted",convertedMedia.getDownloadUrl());

        db.insert(TABLE_SONGS,null,values);
        db.close();

    }

    public List<ConvertedMedia> getSondDetails(){

        List<ConvertedMedia> songDetail = new ArrayList<ConvertedMedia>();
        String query = "SELECT * FROM "+TABLE_SONGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{

                ConvertedMedia convertedMedia = new ConvertedMedia();
                convertedMedia.setId(Integer.parseInt(cursor.getString(0)));
                convertedMedia.setSongName(cursor.getString(1));
                convertedMedia.setLength(cursor.getString(2));
                convertedMedia.setSongUrl(cursor.getString(3));
                convertedMedia.setDownloadUrl(cursor.getString(4));

                songDetail.add(convertedMedia);

            }while(cursor.moveToNext());

        }

        return songDetail;

    }

    public int getSongCount() {
        Cursor cursor = null;
        try {
            String countQuery = "SELECT * FROM " +TABLE_SONGS;
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);

            return cursor.getCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }



}
