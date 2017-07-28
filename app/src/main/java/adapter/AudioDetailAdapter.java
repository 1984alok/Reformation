package adapter;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.Audio;
import utils.Constant;
import utils.FontUtls;
import utils.Utils;

/**
 * Created by Alok on 27-04-2017.
 */
public class AudioDetailAdapter extends RecyclerView.Adapter<AudioDetailAdapter.MyViewHolder> {

    private ArrayList<Audio> dataList;
    OnItemClickListener mItemClickListener;
    public Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle, txtViewSpkr, txtViewSize,percentage;
        ImageView downloadImageView,spkr;
        FrameLayout downLoadLayout;
        ProgressBar progressBar;
        RelativeLayout mapFrame;

        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.textViewAudioGuideTitel);
            txtViewSpkr = (TextView) view.findViewById(R.id.textViewAudioSpeaker);
            spkr = (ImageView) view.findViewById(R.id.spkr);
            txtViewSize = (TextView) view.findViewById(R.id.textViewAudioSize);
            percentage = (TextView) view.findViewById(R.id.percentage);
            downloadImageView = (ImageView) view.findViewById(R.id.imageViewDownLoad);
            progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
            downLoadLayout= (FrameLayout) view.findViewById(R.id.downloadRelativeLayout);
            mapFrame= (RelativeLayout) view.findViewById(R.id.mapFrame);

            downLoadLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v,itemView, getPosition());
                    }
                }
            });
            downloadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v,itemView, getPosition());
                    }
                }
            });
            spkr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v,itemView, getPosition());
                    }
                }
            });

            mapFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v,itemView, getPosition());
                    }
                }
            });

        }



    }


    public AudioDetailAdapter(Context ctx, ArrayList<Audio> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_detail_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Audio model = dataList.get(position);
        holder.txtViewTitle.setText(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)
                ?
                (model.getTitleEn() != null ? model.getTitleEn() : "")
                :
                (model.getTitle() != null ? model.getTitle() : ""));
        //  holder.txtViewSize.setText(model.getAudioSizeEn() != null ? model.getAudioSizeEn() : "");
        holder.txtViewSpkr.setText(ctx.getResources().getString(R.string.spker)+" "+(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)
                ?
                (model.getSpeakerEn() != null ? model.getSpeakerEn() : "")
                :
                (model.getSpeaker() != null ? model.getSpeaker() : "")));

       // FontUtls.loadFont(ctx, "fonts/Roboto-Bold.ttf",  holder.txtViewSpkr);
        // Log.i("gate data", model.getTitle());
        Location loc= new Location("GPS");
        loc.setLatitude(model.getLatitude()!=null?Double.parseDouble(model.getLatitude()):0.0);
        loc.setLongitude(model.getLongitude()!=null?Double.parseDouble(model.getLongitude()):0.0);
        holder.txtViewSize.setText(Utils.getDistance(Constant.appLoc,loc)+" km");

        if(model.getDownloadStatus()== Constant.ACTION_DOWNLOAD_RUNNING){
            Log.i("gate data pos", position + "ACTION_DOWNLOAD_RUNNING ::"+ model.getDownloadStatus());
            holder.downloadImageView.setVisibility(View.GONE);
            holder.spkr.setVisibility(View.GONE);
            holder.downloadImageView.setTag("start");
            holder.percentage.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setProgress(model.getDownloadProgress());
            holder.percentage.setText(model.getDownloadProgress()+"%");
        }else if(model.getDownloadStatus()== Constant.ACTION_DOWNLOAD_COMPLETED){
            Log.i("gate data pos", position + "ACTION_DOWNLOAD_COMPLETED ::"+ model.getDownloadStatus());
            holder.downloadImageView.setVisibility(View.VISIBLE);
            holder.downloadImageView.setImageResource(android.R.drawable.ic_menu_delete);
            holder.downloadImageView.setTag("delete");
            holder.percentage.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.spkr.setVisibility(View.VISIBLE);
        }else{
            Log.i("gate data pos", position + "ACTION_DOWNLOAD_NOTSTARTED ::"+ model.getDownloadStatus());
            holder.downloadImageView.setVisibility(View.VISIBLE);
            holder.downloadImageView.setImageResource(R.drawable.download);
            holder.downloadImageView.setTag("start");
            holder.percentage.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.spkr.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        Log.i("gate data size", dataList.size() + "");
        return dataList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View clickView,View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
