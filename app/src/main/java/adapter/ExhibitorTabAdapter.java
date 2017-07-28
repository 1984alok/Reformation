package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reformation.home.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apihandler.ApiClient;
import model.Exhibitor;

/**
 * Created by IMFCORP\alok.acharya on 18/4/17.
 */
public class ExhibitorTabAdapter extends RecyclerView.Adapter<ExhibitorTabAdapter.MyViewHolder> {

    private ArrayList<Exhibitor> dataList;
    public Context ctx;
    OnItemClickListener mItemClickListener;
    View itemView;
   public  static int  height = 0;

    public int getHeightOfView() {
        int height = 270;
        if(itemView!=null){
            height = itemView.getHeight();
        }
        return height;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtViewTitle,status,address;
        RelativeLayout frame;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
            status = (TextView) view.findViewById(R.id.status);
            imageView = (ImageView) view.findViewById(R.id.imageViewExhibitor);
            frame = (RelativeLayout) view.findViewById(R.id.frame);
            frame.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, itemView, getPosition());
            }
        }
    }



    public ExhibitorTabAdapter(Context ctx, ArrayList<Exhibitor> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhibitor_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exhibitor model = dataList.get(position);
        holder.txtViewTitle.setText(model.getPlaceName());
        holder.address.setText(model.getAddress());
        holder.status.setText(model.getStatus());
        if(model.getStatus().contentEquals("close")){
            holder.status.setVisibility(View.VISIBLE);
        }else{
            holder.status.setVisibility(View.GONE);
        }

        if(model.getHeaderPic()!=null) {
            Picasso.with(ctx).load(ApiClient.BASE_URL+model.getHeaderPic())
                    // .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_photo_frame)
                    .resize(80,80)
                    .centerCrop()
                    .into(holder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        Log.i("gate data size", dataList.size() + "");
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View clickView, View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
