package com.reformation.home.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;
import java.util.List;

import model.EventModel;
import model.FavModel;
import utils.Constant;
import utils.Utils;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class FavEventRecyclerViewAdapter extends RecyclerView.Adapter<FavEventRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<FavModel> mValues;
    private final EventFavFragment.OnListFragmentInteractionListener mListener;

    public FavEventRecyclerViewAdapter(ArrayList<FavModel> items, EventFavFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fav_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?mValues.get(position).getName():
                mValues.get(position).getName_de());
        holder.mContentView.setText(Utils.formatEvenrtDate(mValues.get(position).getDate())+" - "+mValues.get(position).getStart().split(":")[0]+Utils.getHrFormat());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
