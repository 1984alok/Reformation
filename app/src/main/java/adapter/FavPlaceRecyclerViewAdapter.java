package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reformation.home.R;
import com.reformation.home.fragment.PlaceFavFragment;

import java.util.ArrayList;

import model.FavModel;
import utils.Constant;

/**
 * Created by muvi on 15/5/17.
 */

public class FavPlaceRecyclerViewAdapter extends RecyclerView.Adapter<FavPlaceRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<FavModel> mValues;
    private final PlaceFavFragment.OnListPlaceFragmentInteractionListener mListener;

    public FavPlaceRecyclerViewAdapter(ArrayList<FavModel> items, PlaceFavFragment.OnListPlaceFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public FavPlaceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fav_event, parent, false);
        return new FavPlaceRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavPlaceRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?mValues.get(position).getName():
                mValues.get(position).getName_de());
        holder.mContentView.setText(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?mValues.get(position).getAddrss():
                mValues.get(position).getAddrss_de());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListPlaceFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public FavModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
