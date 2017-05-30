package utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reformation.home.EventDetailActivity;
import com.reformation.home.R;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import apihandler.NetworkStatus;
import model.Audio;
import model.EventModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alok on 01-04-2017.
 */
public class Utils {
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }



    public static void showToast(Context ctx, String msg){
        Toast t = Toast.makeText(ctx,msg,Toast.LENGTH_SHORT);
        View v = t.getView();
        v.setPadding(20,10,20,10);
        v.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }

    public static void showLikeToast(Context ctx){
        Toast t = new Toast(ctx);
        View v = LayoutInflater.from(ctx).inflate(R.layout.fav_added,null);
        t.setView(v);
        t.setGravity(Gravity.CENTER,0,0);
        t.setDuration(Toast.LENGTH_LONG);
        t.show();
    }

    public static void showDisLikeToast(Context ctx){
        Toast t = new Toast(ctx);
        View v = LayoutInflater.from(ctx).inflate(R.layout.fav_delete,null);
        t.setView(v);
        t.setGravity(Gravity.CENTER,0,0);
        t.setDuration(Toast.LENGTH_LONG);
        t.show();
    }

    public static void showSnackBar(String msg, View anchor){
        Snackbar.make(anchor,msg,Snackbar.LENGTH_LONG).show();
    }

    public static void showAlert(Context ctx, String title, String msg, DialogInterface.OnClickListener clickListener){
        AlertDialog.Builder bldr = new AlertDialog.Builder(ctx);
        bldr.setTitle(title);
        bldr.setMessage(msg);
        bldr.setPositiveButton("OK",clickListener);
        bldr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        bldr.create().show();

    }


    public static boolean checkDeviceLanguage(){
        LogUtil.createLog("SELECTED LANG in check",Locale.getDefault().getLanguage());
        if(Locale.getDefault().getLanguage().equals("en")){
            Constant.SELECTED_LANG=Constant.LANG_ENG;
            return true;
        }else if(Locale.getDefault().getLanguage().equals("de")){
            Constant.SELECTED_LANG=Constant.LANG_GERMAN;
            return true;
        }
        return false;
    }


    public static void changeLang(Context context,String languageToLoad){
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }


    public static String getYearFromDate(String date){
        String dateArr[] = date.split("-");
        return dateArr[0];
    }

    public static String getMonthFromDate(String date){
        String dateArr[] = date.split("-");
        return dateArr[1];
    }
    public static String getDayFromDate(String date){
        String dateArr[] = date.split("-");
        return dateArr[2];
    }

    public static String getDaywithTHFormatFromDate(String date){
        String dateArr[] = date.split("-");
        return dateArr[2]+getDayOfMonthSuffix(Integer.parseInt(dateArr[2]));
    }

    public static String getWeekNameFromDay(String dateString){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date date = null;
        try {
            date = inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    public static String getDayOfMonthSuffix(final int n) {
       /* if(n >= 1 && n <= 31){
            LogUtil.createLog("illegal day of month: ","" + n);
            return "";
        }*/
        if(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)) {
            if (n >= 11 && n <= 13) {
                return "th";
            }
            switch (n % 10) {
                case 1:
                    return "st";
                case 2:
                    return "nd";
                case 3:
                    return "rd";
                default:
                    return "th";
            }
        }else{
            return ".";
        }
    }

    public static String formatDate( String inputDateStr){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MMM-dd");
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }





    public static String formatEvenrtDate( String inputDateStr){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd.MM.");
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void revealView(FrameLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hideView(final FrameLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }



    public void startEventDetailPage(View v, int position, Context ctx, Class<?> calledActivity, EventModel eventModel){
        Intent transitionIntent = new Intent(ctx, calledActivity);
        transitionIntent.putExtra(EventDetailActivity.EXTRA_PARAM_ID,position);
        transitionIntent.putExtra("DATA",eventModel);

        LinearLayout catagPlaceHolder = (LinearLayout) v.findViewById(R.id.catgList);
        TextView txtEventName = (TextView) v.findViewById(R.id.txtEventName);
        TextView txtEventTime = (TextView) v.findViewById(R.id.txtEventTime);

        View navigationBar = ((Activity)ctx).findViewById(android.R.id.navigationBarBackground);
        View statusBar =((Activity)ctx).findViewById(android.R.id.statusBarBackground);

        Pair<View, String> holderPair = Pair.create((View) catagPlaceHolder, "catgList");
        Pair<View, String> titelPair = Pair.create((View) txtEventName, "txtEventName");
        Pair<View, String> timePair = Pair.create((View) txtEventTime, "txtEventTime");
        Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
        Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
        ArrayList<Pair<View, String>> list = new ArrayList<>();

        list.add(holderPair);
        list.add(titelPair);
        list.add(timePair);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list.add(navPair);
            list.add(statusPair);
        }

        //remove any views that are null
        for (ListIterator<Pair<View, String>> iter = list.listIterator(); iter.hasNext();) {
            Pair pair = iter.next();
            if (pair.first == null) iter.remove();
        }

        Pair<View, String>[] sharedElements = list.toArray(new Pair[list.size()]);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity)ctx),sharedElements);
        ActivityCompat.startActivity(((Activity)ctx), transitionIntent, options.toBundle());
    }


    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }

    public static String getCurrentTime(){
        String timeformat = "HH:mm:ss";
        SimpleDateFormat obDateFormat = new SimpleDateFormat(timeformat);
        Calendar time = Calendar.getInstance();
        System.out.println("Time in 24-hour format :"
                + obDateFormat.format(time.getTime()));
        return obDateFormat.format(time.getTime());

    }


    public static boolean isNetworkAvailable(Context ctx,View view){
        if(!NetworkStatus.getInstance().isConnected(ctx)){
            showSnackBar(ctx.getResources().getString(R.string.network_error_txt),view);
            return false;
        }else{
            return true;
        }

    }

    public static String getHrFormat() {
        if(Constant.SELECTED_LANG.equals(Constant.LANG_GERMAN)){
            return " Uhr";
        }else{
            return " h";
        }
    }

    /*public static String getHrFormatForEvent(EventModel eventModel) {
        String returnString ="00";
        if(eventModel!=null&&eventModel.getStart()!=null)){
            String[] hourFormat = eventModel.getStart().split(":");
            if(){}
            return eventModel.getStart().split(":")[0]+Utils.getHrFormat();
        }else{
            return "h";
        }
    }*/

    public void animateHeartButton(final ImageView view, final boolean status) {

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(view, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //  holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                // view.setImageResource(status ? R.drawable.heart_filled : R.drawable.heart);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setImageResource(status ? R.drawable.heart_filled : R.drawable.heart);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

    }


    public static long addEventToCalenderForTopicWeek(Context ctx,long startTime,long endTime,String tittel){
        /*Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", startTime);
       // intent.putExtra("allDay", true);
       // intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", endTime);
        intent.putExtra("title", tittel);
        ctx.startActivity(intent);*/
        Cursor cur = null;
        try
        {
            // provide CalendarContract.Calendars.CONTENT_URI to
            // ContentResolver to query calendars
            cur = ctx.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,null, null, null, null);
            if (cur.moveToFirst())
            {
                long calendarID=cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID));
                ContentValues eventValues = new ContentValues();
                // provide the required fields for creating an event to
                // ContentValues instance and insert event using
                // ContentResolver
                eventValues.put (CalendarContract.Events.CALENDAR_ID, calendarID);
                eventValues.put(CalendarContract.Events.TITLE, tittel);
                // eventValues.put(CalendarContract.Events.DESCRIPTION," Calendar API"+desc);
                //  eventValues.put(CalendarContract.Events.ALL_DAY,true);
                eventValues.put(CalendarContract.Events.DTSTART, startTime);
                eventValues.put(CalendarContract.Events.DTEND, endTime);
                eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
                Uri eventUri = ctx.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eventValues);
                long eventID = ContentUris.parseId(eventUri);
                LogUtil.createLog("eventID","-----------"+eventID);
                return eventID;


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cur != null)
            {
                cur.close();
            }
        }
        return -1;
    }

    public static long  getMillisecFromDate(  String givenDateString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void putHashMapIntoSharedPref(Context ctx,HashMap<String, Long> testHashMap){
        //create test hashmap
        //convert to string using gson
        Gson gson = new Gson();
        String hashMapString = gson.toJson(testHashMap);

        //save in shared prefs
        SharedPreferences prefs = ctx.getSharedPreferences("test", MODE_PRIVATE);
        prefs.edit().putString("hashString", hashMapString).apply();

    }

    public static HashMap<String,Long> getHashMapfromSharedPref(Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("test", MODE_PRIVATE);
        Gson gson = new Gson();
        //get from shared prefs
        String storedHashMapString =prefs.getString("hashString", "oopsDintWork");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Long>>(){}.getType();
        HashMap<String, Long> testHashMap2 = gson.fromJson(storedHashMapString, type);
        return testHashMap2;
    }



    public static void createCLAPPDirectory(){
        File folder = new File(Constant.DATABASE_FILE_PATH);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }

       /* File folder = new File(Constants.DATABASE_FILE_PATH);
        boolean success = true;
        if(folder.exists()){
            folder.delete();
        }
        folder.mkdirs();*/
    }


    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static String getDistance( Location loc1, Location loc2) {
        if (loc1 != null){
            int distanceInMeters = (int)loc1.distanceTo(loc2) / 1000;
            return String.valueOf(distanceInMeters);
        }else{
            return "0";
        }
    }


    public static ArrayList<Audio> removeDuplicateAudio(ArrayList<Audio> audios){
// add elements to al, including duplicates
        ArrayList<Audio> result = new ArrayList<Audio>();
        Set<String> titles = new HashSet<String>();

        for( Audio item : audios ) {
            if( titles.add(item.getId())) {
                result.add( item );
            }
        };
        return result;
    }
}
