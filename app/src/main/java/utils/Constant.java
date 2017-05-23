package utils;

import android.location.Location;
import android.os.Environment;

/**
 * Created by Alok on 26-03-2017.
 */
public class Constant {

    public static final String  DATABASE_FILE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.reformation.home/DB";

    public static final String AUDIO_DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.reformation.home/Audio";

    public static final String LANG_ENG = "en";
    public static final String LANG_GERMAN = "de";

    public static final String SATUS_TRUE = "200";
    public static String SELECTED_LANG = "";
    public static final int EVENT_TOPIC_DETAIL_TYPE = 2;
    public static final int EVENT_TOPIC_HOME_TYPE = 1;
    public static final int EVENT_TDAYTOMORW = 3;

    public static final String TAG_EXHIBITOR = "Exhibitors";
    public static final String TAG_OTHER_LOCATION = "locations";


    public static final int ACTION_NOT_DOWNLOAD_YET = 0;
    public static final int ACTION_DOWNLOAD_STARTED = 1;
    public static final int ACTION_DOWNLOAD_FAILED = 2;
    public static final int ACTION_DOWNLOAD_COMPLETED = 3;
    public static final int ACTION_DOWNLOAD_RUNNING = 4;

    public static Location appLoc = null;
}
