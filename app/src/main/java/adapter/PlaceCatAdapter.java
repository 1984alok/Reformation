package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.EventPlaceCatg;
import utils.Utils;

/**
 * Created by muvi on 16/6/17.
 */

public class PlaceCatAdapter extends RecyclerView.Adapter<PlaceCatAdapter.MyViewHolder> {

    private ArrayList<EventPlaceCatg.ResponseDatum> dataList;
    public Context ctx;
    PlaceCatAdapter.OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        FrameLayout frameLayout;

        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.list_item);
            frameLayout = (FrameLayout) view.findViewById(R.id.frame);


            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, itemView, getPosition());
                    }
                }
            });

        }


    }


    public PlaceCatAdapter(Context ctx, ArrayList<EventPlaceCatg.ResponseDatum> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @Override
    public PlaceCatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fillter_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceCatAdapter.MyViewHolder holder, int position) {
        EventPlaceCatg.ResponseDatum model = dataList.get(position);
        // Log.i("gate data", model.getTitle());

        holder.txtViewTitle.setText(model.getTitle() != null ? model.getTitle() : "");

        if(model.isChecked()) {
            holder.frameLayout.setForeground(ctx.getResources().getDrawable(R.color.transparent));
            holder.txtViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(Utils.getFillterIconIcon(ctx, model.getTitle().trim()), null, ctx.getResources().getDrawable(R.drawable.checkmark), null);
        }else{
            holder.frameLayout.setForeground(ctx.getResources().getDrawable(R.color.semi_transparent));
            holder.txtViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(Utils.getFillterIconIcon(ctx, model.getTitle().trim()), null, ctx.getResources().getDrawable(R.drawable.seek_handler), null);
        }
        Log.i("gate data pos", position + "");
    }

    @Override
    public int getItemCount() {
        Log.i("gate data size", dataList.size() + "");
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View clickView, View view, int position);
    }

    public void setOnItemClickListener(PlaceCatAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}