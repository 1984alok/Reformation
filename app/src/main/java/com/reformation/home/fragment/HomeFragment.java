package com.reformation.home.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapter.HomeEventAdapter;
import adapter.HomeMenuAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.HomeMenuModelResponse;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;

/**
 * Created by Alok on 26-03-2017.
 */
public class HomeFragment extends Fragment {

    public HomeFragment(){}
    View view;
    ApiInterface mApiInterface;
    ArrayList<String> evenCatg = new ArrayList<>();
    ArrayList<TopicweekResponse.Event> eventList = new ArrayList<>();
    private ImageView topicImgview;
    private TextView topicTitle,topicDesc;
    private ProgressDialog dlg;
    private Context context;
    private HomeEventAdapter homeEventAdapter;
    private HomeMenuAdapter homeMenuAdapter;
    private RecyclerView eventRecyclerView,menuRecyclerView;
    private LinearLayoutManager horizontalLayoutManagaer,verticalLinearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null)
         view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        topicImgview =(ImageView)view.findViewById(R.id.topicImg);
        topicTitle =(TextView) view.findViewById(R.id.textViewTopicTitle);
        topicDesc =(TextView) view.findViewById(R.id.textViewTopicDesc);
        context = getActivity();
        dlg = new ProgressDialog(context);
        dlg.setTitle("Loading...");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        eventRecyclerView = (RecyclerView)view.findViewById(R.id.horizontal_recycler_eventView);
        menuRecyclerView = (RecyclerView)view.findViewById(R.id.horizontal_recycler_menu);
        horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        verticalLinearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        eventRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(horizontalLayoutManagaer);

        menuRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setLayoutManager(verticalLinearLayoutManager);

        getTopicWeek();
        getHomeMenu();
    }

    private void getTopicWeek(){

        dlg.show();
        Call<TopicweekResponse> call = mApiInterface.getTopicWeek(Constant.LANG_ENG);
        call.enqueue(new Callback<TopicweekResponse>() {
            @Override
            public void onResponse(Call<TopicweekResponse> call, Response<TopicweekResponse> response) {
                dlg.dismiss();
                if(response.isSuccessful()){
                    TopicweekResponse model = response.body();
                    loadView(model);
                }

            }

            @Override
            public void onFailure(Call<TopicweekResponse> call, Throwable t) {
                Log.d("onFailure ::",t.getMessage());
                dlg.dismiss();

            }
        });
    }

    private void loadView(TopicweekResponse model) {
        TopicweekResponse.TopicWeekModel topicWeekModel = model.getResponseData().get(0);
        if(topicWeekModel!=null){
            if(eventList!=null) {
                eventList = topicWeekModel.getEvent();
                homeEventAdapter = new HomeEventAdapter(context, eventList);
                eventRecyclerView.setAdapter(homeEventAdapter);
            }


            if(topicWeekModel.getHeaderPic()!=null) {
                Picasso.with(context).load(topicWeekModel.getHeaderPic())
                        // .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.ic_photo_frame)
                        .into(topicImgview);
            }

            topicTitle.setText(topicWeekModel.getToWeekTitle());
            topicDesc.setText(topicWeekModel.getToWeekDes()!=null?topicWeekModel.getToWeekDes():getResources().getString(R.string.topic_desc));




        }


    }

    private void getHomeMenu(){
        dlg.show();
        Call<HomeMenuModelResponse> call = mApiInterface.getMenu(Constant.LANG_ENG);
        call.enqueue(new Callback<HomeMenuModelResponse>() {
            @Override
            public void onResponse(Call<HomeMenuModelResponse> call, Response<HomeMenuModelResponse> response) {
                if(response.isSuccessful()){
                   ArrayList< HomeMenuModelResponse.MenuModel> modelList = response.body().getResponseData();
                    if(modelList!=null){
                        homeMenuAdapter = new HomeMenuAdapter(context,modelList);
                        menuRecyclerView.setAdapter(homeMenuAdapter);
                    }


                }
                dlg.dismiss();

            }

            @Override
            public void onFailure(Call<HomeMenuModelResponse> call, Throwable t) {
                dlg.dismiss();
            }
        });
    }


}
