package utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.reformation.home.R;

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

    }
}
