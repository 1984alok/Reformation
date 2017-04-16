package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.TopicweekResponse;
import utils.Constant;
import utils.FontUtls;
import utils.Utils;

/**
 * Created by Alok on 26-03-2017.
 */
public class HomeEventAdapter  extends RecyclerView.Adapter<HomeEventAdapter.MyViewHolder> {

    private ArrayList<TopicweekResponse.Event> eventList;
    public Context ctx;
    private int type;
    OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtViewTitle,time;
        public LinearLayout linearLayout; RelativeLayout frame;
        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.txtEventName);
            time = (TextView) view.findViewById(R.id.txtEventTime);
            linearLayout = (LinearLayout) view.findViewById(R.id.catgList);
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


    public HomeEventAdapter(Context ctx,ArrayList<TopicweekResponse.Event> horizontalList,int type) {
        this.eventList = horizontalList;
        this.ctx=ctx;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        if (type== Constant.EVENT_TOPIC_HOME_TYPE)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_event_list, parent, false);
        else  if (type== Constant.EVENT_TOPIC_DETAIL_TYPE)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_topic_detail, parent, false);
        else  if (type== Constant.EVENT_TDAYTOMORW)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.today_tomrow_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TopicweekResponse.Event event = eventList.get(position);
        holder.txtViewTitle.setText(event.getTitel());
        FontUtls.loadFont(ctx, "fonts/RobotoCondensed-Regular.ttf",  holder.txtViewTitle);
        holder.time.setText(Utils.formatEvenrtDate(event.getDate())+" - "+event.getStart());
        if(event.getCategory()!=null){
            String[] catList = event.getCategory().split(",");
            if(holder.linearLayout.getChildCount()>0){
                holder.linearLayout.removeAllViews();
            }
            for (int i = 0; i <catList.length ; i++) {
                // if(holder.linearLayout.getChildCount()<catList.length) {
                TextView viewTxt = (TextView) LayoutInflater.from(ctx).inflate(R.layout.event_catg_list, null);
                viewTxt.setText(catList[i].toString());
                viewTxt.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.catg_bg2));
                holder.linearLayout.addView(viewTxt);
                //  }

            }
        }

       /* if(position==eventList.size()-1){
            holder.txtViewTitle.setVisibility(View.INVISIBLE);
            holder.linearLayout.setVisibility(View.INVISIBLE);
            holder.time.setText(ctx.getResources().getString(R.string.discover_more));
            //holder.time.setTextSize(ctx.getResources().getDimension(R.dimen.px15));
        }else {
            holder.txtViewTitle.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.VISIBLE);
           // holder.time.setTextSize(ctx.getResources().getDimension(R.dimen.px17));
        }*/


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
