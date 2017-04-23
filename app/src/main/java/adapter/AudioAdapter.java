package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.Audio;

/**
 * Created by Alok on 22-04-2017.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    private ArrayList<Audio> dataList;
    public Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtViewTitle,txtViewSpkr,txtViewSize;
        ImageView downloadImageView;

        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.textViewAudioGuideTitel);
            txtViewSpkr = (TextView) view.findViewById(R.id.textViewAudioSpeaker);
            txtViewSize = (TextView) view.findViewById(R.id.textViewAudioSize);
            downloadImageView = (ImageView) view.findViewById(R.id.imageViewDownLoad);

        }


    }


    public AudioAdapter(Context ctx, ArrayList<Audio> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_layout_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AudioAdapter.MyViewHolder holder, int position) {
        Audio model = dataList.get(position);
        holder.txtViewTitle.setText(model.getTitleEn()!=null?model.getTitle():"");
        holder.txtViewSize.setText(model.getAudioSizeEn()!=null?model.getAudioSizeEn():"");
        holder.txtViewSpkr.setText(model.getSpeakerEn()!=null?model.getSpeakerEn():"");
       // Log.i("gate data", model.getTitle());
        Log.i("gate data pos", position + "");
    }

    @Override
    public int getItemCount() {
        Log.i("gate data size", dataList.size() + "");
        return dataList.size();
    }


}
