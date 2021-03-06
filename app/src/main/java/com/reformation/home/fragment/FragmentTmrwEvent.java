package com.reformation.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reformation.home.EventDetailActivity;
import com.reformation.home.R;

import java.util.ArrayList;

import adapter.HomeEventAdapter;
import adapter.TodayEventAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.EventModel;
import model.EventResponse;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.Utils;

/**
 * Created by Alok on 10-04-2017.
 */
public class FragmentTmrwEvent extends Fragment {

    public FragmentTmrwEvent(){}
    View view;
    ApiInterface mApiInterface;
    private CustomProgresDialog dlg;
    private Context context;
    private TodayEventAdapter gateAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManagaer;
    ArrayList<EventModel> eventList;
    private HomeEventAdapter homeEventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today, container, false);
        super.onCreate(savedInstanceState);
        context = getActivity();
        dlg = CustomProgresDialog.getInstance(context);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_today);
        //recyclerView.setFocusable(false);
        //recyclerView.setNestedScrollingEnabled(false);
        layoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManagaer);
        getEventList();
        return view;
    }


    private void getEventList() {
        dlg.showDialog();
        Call<EventResponse> call = mApiInterface.getTomorrowEventList(Constant.SELECTED_LANG);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    EventResponse model = response.body();
                    loadEventView(model);
                }

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
               // Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();

            }
        });
    }


    private void loadEventView(EventResponse model) {
        if(model!=null) {
            eventList = model.getResponseData();
            if (eventList != null) {
                homeEventAdapter = new HomeEventAdapter(context, eventList,Constant.EVENT_TDAYTOMORW);
                homeEventAdapter.setOnItemClickListener(mItemClickListener);
                recyclerView.setAdapter(homeEventAdapter);
            }
        }
    }

    HomeEventAdapter.OnItemClickListener mItemClickListener = new HomeEventAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            new Utils().startEventDetailPage(view,position,getActivity(),EventDetailActivity.class,eventList.get(position));
        }
    };
}
