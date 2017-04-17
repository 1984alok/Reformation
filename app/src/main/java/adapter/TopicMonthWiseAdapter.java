package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reformation.home.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.HomeMenuModelResponse;
import model.TopicweekResponse;
import utils.LoadInPicasso;
import utils.Utils;

/**
 * Created by Alok on 10-04-2017.
 */
public class TopicMonthWiseAdapter extends RecyclerView.Adapter<TopicMonthWiseAdapter.MyViewHolder>{

    private ArrayList<TopicweekResponse.TopicWeekModel> modelList;
    public Context ctx;
    OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtViewTitle,txtViewDate,txtViewDesc;
        public Button more;
        public ImageView imgPic;
        public ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.title);
            txtViewDate = (TextView) view.findViewById(R.id.titleDate);
            txtViewDesc = (TextView) view.findViewById(R.id.titleDesc);
            more = (Button) view.findViewById(R.id.buttonMore);
            progressBar = (ProgressBar) view.findViewById(R.id.dlg);
            imgPic = (ImageView) view.findViewById(R.id.homeMenuImg);
            more.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v,itemView, getPosition());
            }
        }
    }


    public TopicMonthWiseAdapter(Context ctx,ArrayList<TopicweekResponse.TopicWeekModel> modelList) {
        this.modelList = modelList;
        this.ctx=ctx;
    }
    @Override
    public TopicMonthWiseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_week_month_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopicMonthWiseAdapter.MyViewHolder holder, int position) {
        TopicweekResponse.TopicWeekModel model = modelList.get(position);
        if(model!=null) {
            holder.txtViewTitle.setText(model.getToWeekTitle());
            String sdate  = Utils.getDaywithTHFormatFromDate(model.getPerStart())+" "+Utils.getMonthFromDate(model.getPerStart());
            String edate  = Utils.getDaywithTHFormatFromDate(model.getPerEnd())+" "+Utils.getMonthFromDate(model.getPerEnd());

            holder.txtViewDate.setText(sdate+"-"+edate);
            holder.txtViewDesc.setText(model.getToWeekDes());
            if (model.getHeaderPic() != null) {
                LoadInPicasso.getInstance(ctx).loadPic(holder.imgPic,holder.progressBar,model.getHeaderPic());
            }
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View clickView,View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
