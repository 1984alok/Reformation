package utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.reformation.home.R;

/**
 * Created by Alok on 02-04-2017.
 */
public class CustomProgresDialog{
    private ProgressDialog dlg;
    private static CustomProgresDialog customProgresDialog;
    private CustomProgresDialog(Context context) {
        dlg = new ProgressDialog(context,R.style.FullHeightDialog);
        dlg.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dlg.setCancelable(false);
    }

    public static CustomProgresDialog getInstance(Context ctx){
        if(customProgresDialog==null){
            customProgresDialog = new CustomProgresDialog(ctx);
        }
        return customProgresDialog;
    }

    public void hideDialog(){
        if(dlg!=null&&dlg.isShowing()){
            dlg.hide();
        }
    }

    public void showDialog(){
        if(dlg!=null&&!dlg.isShowing()){
            dlg.show();
            dlg.setContentView(R.layout.custom_progress_layout);
        }
    }

}
