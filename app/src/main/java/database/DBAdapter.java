package database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final String DATABASE_NAME = "ReformationDB"; //$NON-NLS-1$

	public static final int DATABASE_VERSION = 1;


	private static final String CREATE_TABLE_FAV =
			"create table FavTable (_id integer primary key autoincrement, " //$NON-NLS-1$
					+ FavDB.FAV_ID + " TEXT," //$NON-NLS-1$
					+ FavDB.FAV_NAME + " TEXT," //$NON-NLS-1$
					+ FavDB.FAV_NAME_DE + " TEXT,"
					+ FavDB.FAV_START + " TEXT," //$NON-NLS-1$
					+ FavDB.FAV_DATE + " TEXT,"
					+ FavDB.FAV_ADDRSS + " TEXT," //$NON-NLS-1$
					+ FavDB.FAV_ADDRSS_DE + " TEXT,"
					+ FavDB.FAV_CATG + " TEXT" + ");"; //$NON-NLS-1$ //$NON-NLS-2$


	private static final String CREATE_TABLE_AUDIO =
			"create table AudioTable (_id integer primary key autoincrement, " //$NON-NLS-1$
					+ AudioDB.AUDIOID + " TEXT," //$NON-NLS-1$
					+ AudioDB.AUDIODOWNLOADID + " integer," //$NON-NLS-1$
					+ AudioDB.AUDIODOWNLOADSTATUS + " integer,"
					+ AudioDB.AUDIOGATEID + " TEXT," //$NON-NLS-1$
					+ AudioDB.AUDIOSDCARDPATH_EN + " TEXT,"
					+ AudioDB.AUDIOSDCARDPATH_DE + " TEXT," //$NON-NLS-1$
					+ AudioDB.AUDIODOWNLOADPATH_EN + " TEXT,"
					+ AudioDB.AUDIODOWNLOADPATH_DE + " TEXT" + ");"; //$NON-NLS-1$ //$NON-NLS-2$




	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	/**
	 * Constructor
	 * @param ctx
	 */
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		this.DBHelper = new DatabaseHelper(this.context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{

			db.execSQL(CREATE_TABLE_FAV);
			db.execSQL(CREATE_TABLE_AUDIO);
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
							  int newVersion)
		{
			// Adding any table mods to this guy here
		}
	}

	/**
	 * open the db
	 * @return this
	 * @throws SQLException
	 * return type: DBAdapter
	 */
	public DBAdapter open() throws SQLException
	{
		this.db = this.DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * close the db
	 * return type: void
	 */
	public void close()
	{
		this.DBHelper.close();
	}

}
