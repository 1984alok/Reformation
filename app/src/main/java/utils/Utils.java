package utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reformation.home.EventDetailActivity;
import com.reformation.home.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Alok on 01-04-2017.
 */
public class Utils {


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
        v.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
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

    public static String getDayOfMonthSuffix(final int n) {
       /* if(n >= 1 && n <= 31){
            LogUtil.createLog("illegal day of month: ","" + n);
            return "";
        }*/
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
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



    public void startEventDetailPage(View v,int position,Context ctx,Class<?> calledActivity){
        Intent transitionIntent = new Intent(ctx, calledActivity);
        transitionIntent.putExtra(EventDetailActivity.EXTRA_PARAM_ID,position);
        // transitionIntent.putExtra(DetailActivity.EXTRA_PARAM_HANDLER,recvMsgHandler);

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
}
