package com.reformation.home.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reformation.home.R;

import java.util.ArrayList;

import adapter.FavPlaceRecyclerViewAdapter;
import database.DBAdapter;
import database.FavDB;
import model.EventModel;
import model.FavModel;
import utils.CustomProgresDialog;

/**
 * Created by muvi on 15/5/17.
 */

public class PlaceFavFragment extends Fragment {


    DBAdapter dbAdapter;
    FavDB favDB;
    CustomProgresDialog customProgresDialog;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String CATG = "catg";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private PlaceFavFragment.OnListPlaceFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    String catg = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlaceFavFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PlaceFavFragment newInstance(String catg, int columnCount) {
        PlaceFavFragment fragment = new PlaceFavFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(CATG, catg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            catg = getArguments().getString(CATG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_event_list_item, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Drawable drawable = getResources().getDrawable(R.drawable.line_devider3);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST,drawable));
            customProgresDialog = CustomProgresDialog.getInstance(getActivity());

            loadFavEvent();
        }
        return view;
    }

    private void loadFavEvent() {
        new AsyncTask<Void, Void, ArrayList<FavModel>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                customProgresDialog.showDialog();
                dbAdapter = new DBAdapter(getActivity());
                dbAdapter.open();
                favDB = new FavDB(getActivity());
                favDB.open();
            }

            @Override
            protected void onPostExecute(ArrayList<FavModel> favModels) {
                super.onPostExecute(favModels);
                if (favModels != null && favModels.size() > 0) {
                    recyclerView.setAdapter(new FavPlaceRecyclerViewAdapter(favModels, mListener));
                }
                customProgresDialog.hideDialog();
                favDB.close();
                dbAdapter.close();
            }

            @Override
            protected ArrayList<FavModel> doInBackground(Void... params) {
                return favDB.getFavinfo(catg);
            }
        }.execute();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if (context instanceof OnListPlaceFragmentInteractionListener) {
            mListener = (OnListPlaceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListPlaceFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListPlaceFragmentInteraction(FavModel item);
    }
}
