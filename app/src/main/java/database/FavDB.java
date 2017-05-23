package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

import model.FavModel;
import utils.Constant;

/**
 * Created by Alok on 08-10-2016.
 */
public class FavDB {


    public static final String ROW_ID = "_id";
    public static final String FAV_ID = "favId";
    public static final String FAV_NAME = "favName";
    public static final String FAV_NAME_DE = "favNameDe";
    public static final String FAV_START = "favStart";
    public static final String FAV_DATE = "favDate";
    public static final String FAV_ADDRSS = "favAdd";
    public static final String FAV_ADDRSS_DE = "favAddDe";
    public static final String FAV_CATG = "favCatg";







    private static final String FAV_TABLE = "FavTable";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
           // super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
            super(context, Constant.DATABASE_FILE_PATH
                    + File.separator + "RF_DB"
                    + File.separator + DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
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
    public FavDB(Context ctx) {
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
    public FavDB open() throws SQLException {
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



    public long createFavinfo(FavModel info){

        System.out.println("inserting data into  data base...");
        ContentValues initialValues = new ContentValues();
        initialValues.put(FAV_ID, info.getId());
        initialValues.put(FAV_NAME, info.getName());
        initialValues.put(FAV_NAME_DE, info.getName_de());
        initialValues.put(FAV_START, info.getStart());
        initialValues.put(FAV_DATE, info.getDate());
        initialValues.put(FAV_ADDRSS, info.getAddrss());
        initialValues.put(FAV_ADDRSS_DE, info.getAddrss_de());
        initialValues.put(FAV_CATG, info.getCatg());
        return this.mDb.insert(FAV_TABLE, null, initialValues);
    }




    public ArrayList<FavModel> getFavinfo(String cat) {

        String selectQuery = "SELECT * FROM " + FAV_TABLE+" WHERE "+FAV_CATG+" = '"+cat+"'";
        ArrayList<FavModel> catagList = new ArrayList<FavModel>();
        FavModel info;
        Cursor cur = this.mDb.rawQuery(selectQuery, null);
        if(cur.moveToFirst()){
            do{

                info = new FavModel();
                info.setId(cur.getString(1));
                info.setName(cur.getString(2));
                info.setName_de(cur.getString(3));
                info.setStart(cur.getString(4));
                info.setDate(cur.getString(5));
                info.setAddrss(cur.getString(6));
                info.setAddrss_de(cur.getString(7));
                info.setCatg(cur.getString(8));


                catagList.add(info);
            }while(cur.moveToNext());
        }
        return catagList;


    }

    public boolean isFav(String id,String cat) {

        String selectQuery = "SELECT * FROM " + FAV_TABLE+" WHERE "+FAV_ID+" = '"+id+"' AND "+FAV_CATG+" = '"+cat+"'";
        FavModel info;
        Cursor cursor = this.mDb.rawQuery(selectQuery, null);
        int c = cursor.getCount();
        cursor.close();
        if(c>0){
            return true;
        }else{
            return false;
        }

    }



    //Delete all info

    public void deleteFav(String id){

        String deletequery ="DELETE from "+ FAV_TABLE+" WHERE "+FAV_ID+" = '"+id+"'";;
        this.mDb.execSQL(deletequery);
    }





    // Getting  Count
    public int getCount() {
        String countQuery  = "SELECT * FROM " + FAV_TABLE;
        System.out.println("countQuery......"+countQuery);
        Cursor cursor = this.mDb.rawQuery(countQuery, null);
        int c = cursor.getCount();
        cursor.close();
        // return count
        return c;

    }

}
