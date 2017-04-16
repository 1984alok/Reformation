package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reformation.home.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import apihandler.ApiClient;
import apihandler.ApiInterface;

/**
 * Created by Alok on 08-04-2017.
 */
public class LoadInPicasso {
    private Context context;
    private Target target;
    private Picasso picasso;
    private static LoadInPicasso instance;
    private LoadInPicasso(Context context){
        this.context=context;
       picasso = Picasso.with(context);

    }

    public static LoadInPicasso getInstance(Context context){
        if(instance==null){
            return new LoadInPicasso(context);
        }
        return  instance;
    }

    public void loadPic(final ImageView imgPic,final ProgressBar progressBar,String path){
        progressBar.setVisibility(View.VISIBLE);
        imgPic.setVisibility(View.VISIBLE);
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loading of the bitmap was a success
                // TODO do some action with the bitmap
                progressBar.setVisibility(View.GONE);
                imgPic.setImageBitmap(bitmap);
                LogUtil.createLog("Target","onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // loading of the bitmap failed
                // TODO do some action/warning/error message
                LogUtil.createLog("Target","onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                LogUtil.createLog("Target","onPrepareLoad");
            }
        };

        imgPic.setTag(target);
        picasso.load(ApiClient.BASE_URL+path)
                .into(target);
    }




    public  void releasePicasso(){

    }


}

