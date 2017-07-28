package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reformation.home.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apihandler.ApiClient;
import model.Gallery;
import model.TopicweekResponse;
import utils.LoadInPicasso;
import utils.Utils;

/**
 * Created by Alok on 22-04-2017.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder>{

    private ArrayList<Gallery>modelList;
    public Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgPic;
      //  public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
          //  progressBar = (ProgressBar) view.findViewById(R.id.dlg);
            imgPic = (ImageView) view.findViewById(R.id.homeMenuImg);
        }
    }


    public GalleryAdapter(Context ctx, ArrayList<Gallery> modelList) {
        this.modelList = modelList;
        this.ctx = ctx;
    }

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_image_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.MyViewHolder holder, int position) {
        Gallery model = modelList.get(position);
        if (model != null) {
            if (model.getFileName() != null) {
              //  holder.progressBar.setVisibility(View.GONE);
                holder.imgPic.setVisibility(View.VISIBLE);
              //  LoadInPicasso.getInstance(ctx).loadPic(holder.imgPic,model.getFileName());
               /* Picasso.with(ctx).load(ApiClient.BASE_URL+model.getFileName())
                        .error(R.drawable.gallery_default)
                        // .centerInside()
                        // .resize(500,230)
                        //  .fit()
                        .into(holder.imgPic);*/
                Log.d("PICASSSO", "URL: "+ApiClient.BASE_URL+model.getFileName());
                /*Picasso.with(ctx).load("https://d2gx0xinochgze.cloudfront.net/1074/public/public/system/pgposters/1421/standard/Watch-Free-Download-PNG_1499247000.png")
                        .placeholder(R.drawable.ic_photo_frame)
                        .error(R.drawable.gallery_default)
                        .fit()
                        .centerCrop()
                        .into(holder.imgPic);*/

                Glide.with(ctx)
                        .load(ApiClient.BASE_URL+model.getFileName())
                        .override(500,210)
                        .fitCenter()
                        .placeholder(R.drawable.ic_photo_frame)
                        .error(R.drawable.gallery_default)
                        .into(holder.imgPic);



            }
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
