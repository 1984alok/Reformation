package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import model.GateModel;

/**
 * Created by Alok on 10-04-2017.
 */
public class GateAdapter extends RecyclerView.Adapter<GateAdapter.MyViewHolder>{

    private ArrayList<GateModel> dataList;
    public Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.gateName);

        }
    }


    public GateAdapter(Context ctx, ArrayList<GateModel> dataList) {
        this.dataList = dataList;
        this.ctx=ctx;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gate_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GateAdapter.MyViewHolder holder, int position) {
        GateModel model = dataList.get(position);
        holder.txtViewTitle.setText(model.getTitle());
        Log.i("gate data",model.getTitle());
        Log.i("gate data pos",position+"");
    }

    @Override
    public int getItemCount() {
        Log.i("gate data size",dataList.size()+"");
        return dataList.size();
    }
}
