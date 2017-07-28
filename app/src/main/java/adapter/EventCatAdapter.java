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

import model.EvenTCatg;
import model.EventPlaceCatg;
import utils.Utils;

/**
 * Created by muvi on 12/7/17.
 */

public class EventCatAdapter  extends RecyclerView.Adapter<EventCatAdapter.MyViewHolder> {

    private ArrayList<EvenTCatg.ResponseDatum> dataList;
    public Context ctx;
    OnItemClickListener mItemClickListener;

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


    public EventCatAdapter(Context ctx, ArrayList<EvenTCatg.ResponseDatum> dataList) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fillter_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EvenTCatg.ResponseDatum model = dataList.get(position);
        // Log.i("gate data", model.getTitle());

        holder.txtViewTitle.setText(model.getTitle() != null ? model.getTitle() : "");

        if(model.isChecked()) {
            holder.frameLayout.setForeground(ctx.getResources().getDrawable(R.color.transparent));
            holder.txtViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.checkmark), null);
        }else{
            holder.frameLayout.setForeground(ctx.getResources().getDrawable(R.color.semi_transparent));
            holder.txtViewTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ctx.getResources().getDrawable(R.drawable.seek_handler), null);
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

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
