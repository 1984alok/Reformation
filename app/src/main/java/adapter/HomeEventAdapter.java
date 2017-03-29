package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.TopicweekResponse;

/**
 * Created by Alok on 26-03-2017.
 */
public class HomeEventAdapter  extends RecyclerView.Adapter<HomeEventAdapter.MyViewHolder> {

    private ArrayList<TopicweekResponse.Event> eventList;
    public Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle,time;
        public LinearLayout linearLayout;
        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.txtEventName);
            time = (TextView) view.findViewById(R.id.txtEventTime);
            linearLayout = (LinearLayout) view.findViewById(R.id.catgList);

        }
    }


    public HomeEventAdapter(Context ctx,ArrayList<TopicweekResponse.Event> horizontalList) {
        this.eventList = horizontalList;
        this.ctx=ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_event_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TopicweekResponse.Event event = eventList.get(position);
        holder.txtViewTitle.setText(event.getTitel());
        holder.time.setText(event.getDate()+" - "+event.getStart());
        if(event.getCategory()!=null){
            String[] catList = event.getCategory().split(",");
            for (int i = 0; i <catList.length ; i++) {
                TextView viewTxt = (TextView)LayoutInflater.from(ctx).inflate(R.layout.event_catg_list,null);
                viewTxt.setText(catList[i].toString());
                holder.linearLayout.addView(viewTxt);

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
}
