package utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.reformation.home.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alok on 01-04-2017.
 */
public class Utils {




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

}
