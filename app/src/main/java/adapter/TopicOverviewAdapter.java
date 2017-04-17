package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.GateModel;
import model.TopicweekResponse;
import okhttp3.internal.Util;
import utils.FontUtls;
import utils.Utils;

/**
 * Created by Alok on 18-04-2017.
 */
public class TopicOverviewAdapter extends RecyclerView.Adapter<TopicOverviewAdapter.MyViewHolder>{

    private ArrayList<TopicweekResponse.TopicWeekModel> dataList;
    public Context ctx;
    OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtViewTitle,time;
        RelativeLayout frame;
        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.txtEventName);
            time = (TextView) view.findViewById(R.id.txtEventTime);
            frame = (RelativeLayout)view.findViewById(R.id.frame);
            frame.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,itemView, getPosition());
            }
        }
    }


    public TopicOverviewAdapter(Context ctx, ArrayList<TopicweekResponse.TopicWeekModel> dataList) {
        this.dataList = dataList;
        this.ctx=ctx;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_tomrow_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopicOverviewAdapter.MyViewHolder holder, int position) {
        TopicweekResponse.TopicWeekModel model = dataList.get(position);
        holder.txtViewTitle.setText(model.getToWeekTitle());
        holder.time.setText(Utils.formatEvenrtDate(model.getPerStart())+" - "+Utils.formatEvenrtDate(model.getPerEnd()));
    }

    @Override
    public int getItemCount() {
        Log.i("gate data size",dataList.size()+"");
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View clickView,View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
