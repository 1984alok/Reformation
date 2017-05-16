package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Audio;
import model.FavModel;

/**
 * Created by muvi on 16/5/17.
 */

public class AudioDB {

    public static final String ROW_ID = "_id";
    public static final String AUDIOID = "audId";
    public static final String AUDIODOWNLOADID = "audDownloadId";
    public static final String AUDIODOWNLOADSTATUS = "status";
    public static final String AUDIOGATEID = "gateId";
    public static final String AUDIOSDCARDPATH_EN = "sdcardpathEn";
    public static final String AUDIOSDCARDPATH_DE = "sdcardpathDe";
    public static final String AUDIODOWNLOADPATH_EN = "pathEn";
    public static final String AUDIODOWNLOADPATH_DE = "pathDe";
    public static final String AUDIO_MISC= "misc";






    private static final String AUDIO_TABLE = "AudioTable";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx
     *            the Context within which to work
     */
    public AudioDB(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the cars database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public AudioDB open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
    }



    public long createAudioinfo(Audio info){

        System.out.println("inserting data into  data base...");
        ContentValues initialValues = new ContentValues();
        initialValues.put(AUDIOID, info.getId());
        initialValues.put(AUDIOGATEID, info.getGateareaId());
        initialValues.put(AUDIODOWNLOADID, info.getDownloadId());
        initialValues.put(AUDIOSDCARDPATH_EN, info.getSdcardPath());
        initialValues.put(AUDIOSDCARDPATH_DE, info.getSdcardPathDe());
        initialValues.put(AUDIODOWNLOADPATH_EN, info.getAudioEn());
        initialValues.put(AUDIODOWNLOADPATH_DE, info.getAudio());
        initialValues.put(AUDIODOWNLOADSTATUS, info.getDownloadStatus());
        return this.mDb.insert(AUDIO_TABLE, null, initialValues);
    }




    public Audio getAudiinfo(String gateId) {

        String selectQuery = "SELECT * FROM " + AUDIO_TABLE +" WHERE "+AUDIOGATEID+" = '"+gateId+"'";
        Audio info = null;
        Cursor cur = this.mDb.rawQuery(selectQuery, null);
        if(cur.moveToFirst()){

                info = new Audio();
                info.setId(cur.getString(1));
                info.setGateareaId(cur.getString(2));
                info.setDownloadId(cur.getInt(3));
                info.setSdcardPath(cur.getString(4));
                info.setSdcardPathDe(cur.getString(5));
                info.setAudioEn(cur.getString(6));
                info.setAudio(cur.getString(7));
                info.setDownloadStatus(cur.getInt(8));

        }
        return info;


    }

    public boolean updateAppDownLoadInfo(int id,int downloadStatus){

        System.out.println("updating downloadStatus data into  data base...");
        ContentValues initialValues = new ContentValues();
        initialValues.put(AUDIODOWNLOADSTATUS, ""+downloadStatus);
        return this.mDb.update(AUDIO_TABLE, initialValues, AUDIOID + " = " + id, null) >0;
    }


    public boolean updateAppDownLoadID(int id,int downLoadId){

        System.out.println("updating downLoadId data into  data base...");
        ContentValues initialValues = new ContentValues();
        initialValues.put(AUDIODOWNLOADID, ""+downLoadId);
        return this.mDb.update(AUDIO_TABLE, initialValues, AUDIOID + " = " + id, null) >0;
    }






    //Delete all info

    public void deleteAudio(String id){

        String deletequery ="DELETE from "+ AUDIO_TABLE +" WHERE "+AUDIOID+" = '"+id+"'";;
        this.mDb.execSQL(deletequery);
    }





    // Getting  Count
    public int getCount() {
        String countQuery  = "SELECT * FROM " + AUDIO_TABLE;
        System.out.println("countQuery......"+countQuery);
        Cursor cursor = this.mDb.rawQuery(countQuery, null);
        int c = cursor.getCount();
        cursor.close();
        // return count
        return c;

    }
}
