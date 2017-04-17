package com.reformation.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.R;
import com.reformation.home.TopicWeekDetailActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.FragAdapter;
import adapter.TopicMonthWiseAdapter;
import adapter.TopicOverviewAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;

/**
 * Created by Alok on 26-03-2017.
 */
public class ProgramFragment extends Fragment implements View.OnClickListener{

    View view;
    FragAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private RecyclerView topicMnthRecyclerView,topicOvrvwRecyclerView;
    private LinearLayoutManager horizontalLayoutManagaer,verticalLayoutManagaer;
    private ImageView imageViewSearch, imageViewFilter;
    private TextView topicHeader;
    private CustomProgresDialog dlg;
    ApiInterface mApiInterface;
    Context context;
    TopicMonthWiseAdapter topicMonthWiseAdapter;
    TopicOverviewAdapter topicOverviewAdapter;
    ArrayList<TopicweekResponse.TopicWeekModel> mnthDatalist,topicOvrvwList;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentTodayEvent(),getResources().getString(R.string.today));
        adapter.addFragment(new FragmentTmrwEvent(),getResources().getString(R.string.tomorrow));
        viewPager.setAdapter(adapter);
    }

    public ProgramFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_program, container, false);
        super.onCreate(savedInstanceState);
        context = getActivity();
        dlg = CustomProgresDialog.getInstance(context);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        imageViewFilter =(ImageView)view.findViewById(R.id.imageViewLeft);
        imageViewSearch =(ImageView)view.findViewById(R.id.imageViewRight);
        topicHeader =(TextView)view.findViewById(R.id.textViewHeaderTitle);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        topicMnthRecyclerView = (RecyclerView)view.findViewById(R.id.horizontal_recycler_topic);
        topicOvrvwRecyclerView = (RecyclerView)view.findViewById(R.id.horizontal_recycler_topicoverview);
        horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        verticalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

         Drawable drawable = getResources().getDrawable(R.drawable.line_devider_two);


        topicOvrvwRecyclerView.setFocusable(false);
        topicOvrvwRecyclerView.setNestedScrollingEnabled(false);
        topicOvrvwRecyclerView.setHasFixedSize(true);
        topicOvrvwRecyclerView.setLayoutManager(verticalLayoutManagaer);
        
        topicMnthRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST,drawable));
        topicMnthRecyclerView.setHasFixedSize(true);
        topicMnthRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        imageViewFilter.setImageResource(R.drawable.filter_blue);
        imageViewSearch.setImageResource(R.drawable.search);
        imageViewFilter.setOnClickListener(this);
        topicHeader.setText(getResources().getString(R.string.program_text));

        getTopicMonthWise();
        getTopicDateWise();

        return view;
    }

    private void getTopicDateWise() {
        dlg.showDialog();
        Call<TopicweekResponse> call = mApiInterface.getTopicInDateWise(Constant.SELECTED_LANG);
        call.enqueue(new Callback<TopicweekResponse>() {
            @Override
            public void onResponse(Call<TopicweekResponse> call, Response<TopicweekResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    TopicweekResponse model = response.body();
                    topicOvrvwList = model.getResponseData();
                    if(topicOvrvwList!=null&topicOvrvwList.size()>0){
                        topicOverviewAdapter = new TopicOverviewAdapter(context,topicOvrvwList);
                        topicOverviewAdapter.setOnItemClickListener(onItemClickListener);
                        topicOvrvwRecyclerView.setAdapter(topicOverviewAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<TopicweekResponse> call, Throwable t) {
                Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();

            }
        });
    }

    private void getTopicMonthWise() {

        dlg.showDialog();
        Call<TopicweekResponse> call = mApiInterface.getTopicInMonthWise(Constant.SELECTED_LANG);
        call.enqueue(new Callback<TopicweekResponse>() {
            @Override
            public void onResponse(Call<TopicweekResponse> call, Response<TopicweekResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    TopicweekResponse model = response.body();
                    mnthDatalist = model.getResponseData();
                    if(mnthDatalist!=null&mnthDatalist.size()>0){
                        topicMonthWiseAdapter = new TopicMonthWiseAdapter(context,mnthDatalist);
                        topicMonthWiseAdapter.setOnItemClickListener(onItemMonthClickListener);
                        topicMnthRecyclerView.setAdapter(topicMonthWiseAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<TopicweekResponse> call, Throwable t) {
                Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();

            }
        });
    }

    @Override
    public void onClick(View v) {

    }



    TopicOverviewAdapter.OnItemClickListener onItemClickListener = new TopicOverviewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View clickView, View view, int position) {
            final Intent intent = new Intent(getActivity(),TopicWeekDetailActivity.class).putExtra("Data",topicOvrvwList.get(position));
            startActivity(intent);
        }
    };

    TopicMonthWiseAdapter.OnItemClickListener onItemMonthClickListener = new TopicMonthWiseAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            final Intent intent = new Intent(getActivity(),TopicWeekDetailActivity.class).putExtra("Data",mnthDatalist.get(position));
            startActivity(intent);
        }
    };


}
