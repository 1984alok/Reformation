package com.reformation.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.HomeEventAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.EventModel;
import utils.Constant;
import utils.Utils;

public class FilterResultActivity extends AppCompatActivity {

    @BindView(R.id.filterRecycler)
    RecyclerView filterRecycler;
    @BindView(R.id.textViewHeaderTitle)
    TextView textViewHeaderTitle;
    @BindView(R.id.imageViewLeft)
    ImageView imageViewLeft;


    private HomeEventAdapter homeEventAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<EventModel> eventModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);
        ButterKnife.bind(this);
        textViewHeaderTitle.setText(getResources().getString(R.string.program_text));
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        filterRecycler.setLayoutManager(layoutManager);
        filterRecycler.setHasFixedSize(true);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        eventModels = (ArrayList<EventModel>) args.getSerializable("ARRAYLIST");


        if(eventModels!=null&&eventModels.size()>0) {
            homeEventAdapter = new HomeEventAdapter(this, eventModels, Constant.EVENT_TOPIC_DETAIL_TYPE);
            homeEventAdapter.setOnItemClickListener(mItemClickListener);
            filterRecycler.setAdapter(homeEventAdapter);
        }
    }

    @OnClick(R.id.imageViewLeft)
    void closeActivity(){
        finish();
    }

    HomeEventAdapter.OnItemClickListener mItemClickListener = new HomeEventAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            new Utils().startEventDetailPage(view,position,FilterResultActivity.this,EventDetailActivity.class,eventModels.get(position));
        }
    };

}
