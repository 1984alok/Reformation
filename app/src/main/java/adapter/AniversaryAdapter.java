package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reformation.home.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apihandler.ApiClient;
import model.AnniversaryModelResponse;
import model.TopicweekResponse;

/**
 * Created by Alok on 02-04-2017.
 */
public class AniversaryAdapter extends RecyclerView.Adapter<AniversaryAdapter.MyViewHolder> {

    private ArrayList<AnniversaryModelResponse.ResponseModel> eventList;
    public Context ctx;
    OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.imageViewAnniversry);
            img.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,itemView, getPosition());
            }
        }
    }


    public AniversaryAdapter(Context ctx,ArrayList<AnniversaryModelResponse.ResponseModel> horizontalList) {
        this.eventList = horizontalList;
        this.ctx=ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.anniversary_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AnniversaryModelResponse.ResponseModel model = eventList.get(position);
        if(model.getImage()!=null) {
            Picasso.with(ctx).load(ApiClient.BASE_URL+model.getImage())
                    // .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_photo_frame)
                    .into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View clickView,View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
