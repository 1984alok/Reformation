package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.HomeMenuModelResponse;

/**
 * Created by Alok on 26-03-2017.
 */
public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder> {

    private ArrayList<HomeMenuModelResponse.MenuModel> modelArrayList;
    public Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public ImageView imgPic;
        public MyViewHolder(View view) {
            super(view);
            txtViewTitle = (TextView) view.findViewById(R.id.textViewHeaderTitle);
            imgPic = (ImageView) view.findViewById(R.id.homeMenuImg);

        }
    }


    public HomeMenuAdapter(Context ctx,ArrayList<HomeMenuModelResponse.MenuModel> horizontalList) {
        this.modelArrayList = horizontalList;
        this.ctx=ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_menu_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        HomeMenuModelResponse.MenuModel model = modelArrayList.get(position);
        holder.txtViewTitle.setText(model.getTitle());
        if(model.getImage()!=null) {
            Picasso.with(ctx).load(model.getImage())
                    // .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_photo_frame)
                    .into(holder.imgPic);
        }




    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }
}

