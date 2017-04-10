package utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Alok on 08-04-2017.
 */
public class FontUtls {

    public static void loadFont(Context context, String fontPath, TextView textView){
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        textView.setTypeface(tf);
    }
}



