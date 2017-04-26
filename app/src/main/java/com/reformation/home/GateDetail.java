package com.reformation.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import adapter.AudioAdapter;
import adapter.GalleryAdapter;
import adapter.HomeEventAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Audio;
import model.EventDetailGateData;
import model.EventDetailPlaceData;
import model.EventModel;
import model.EventResponseData;
import model.EventDetailGateData;
import model.Gallery;
import model.GateDetailResponse;
import model.GateModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LoadInPicasso;
import utils.LogUtil;
import utils.TransitionAdapter;
import utils.Utils;

import static utils.Utils.isAndroid5;

public class GateDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView gateImgview,leftImg,rightFilterImg;
    private TextView gateTitle, gateDesc, gateHeader;
    private CustomProgresDialog dlg;
    ProgressBar progressBar;

    private RecyclerView gateRecyclerView;
    private LinearLayoutManager layoutManagaer;

    private ApiInterface mApiInterface;
    private GateModel gateModel;
    private ArrayList<Gallery> galleries;
    private ArrayList<Audio> audios;
    private ArrayList<EventModel> eventModels;
    private RecyclerView eventGalleryView,recyclerview_audioguide,recyclerview_more_events;
    private GalleryAdapter galleryAdapter;
    private HomeEventAdapter homeEventAdapter;
    private AudioAdapter audioAdapter;
    private LinearLayoutManager layoutManager,audiLayoutManager,galleryManager;
    String id = "";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);
                getGatDetails(id);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_detail);
        dlg = CustomProgresDialog.getInstance(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        if(isAndroid5()){
            windowTransition();
        }else{
            makeEnterTransition();
        }
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        rightFilterImg=(ImageView)findViewById(R.id.imageViewLeft);
        gateImgview = (ImageView)findViewById(R.id.homeMenuImg);
        progressBar = (ProgressBar)findViewById(R.id.dlg);
        gateTitle = (TextView)findViewById(R.id.gateName);
        gateDesc = (TextView)findViewById(R.id.textViewTopicDesc);
        gateHeader = (TextView)findViewById(R.id.textViewHeaderTitle);

        eventGalleryView = (RecyclerView)findViewById(R.id.recyclerview_gallery);
        recyclerview_audioguide = (RecyclerView)findViewById(R.id.recyclerview_audioguide);
        recyclerview_more_events = (RecyclerView)findViewById(R.id.recyclerview_more_events);

        recyclerview_audioguide.setFocusable(false);
        recyclerview_more_events.setFocusable(false);
        recyclerview_audioguide.setNestedScrollingEnabled(false);
        recyclerview_more_events.setNestedScrollingEnabled(false);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        audiLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        galleryManager  = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Drawable drawable = getResources().getDrawable(R.drawable.line_devider);
        // eventGalleryView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST,drawable));

        recyclerview_audioguide.setHasFixedSize(true);
        recyclerview_more_events.setHasFixedSize(true);
        eventGalleryView.setHasFixedSize(true);

        eventGalleryView.setLayoutManager(layoutManager);
        recyclerview_audioguide.setLayoutManager(audiLayoutManager);
        recyclerview_more_events.setLayoutManager(galleryManager);

        leftImg.setOnClickListener(this);
        gateHeader.setText(getResources().getString(R.string.gate));
        gateModel = (GateModel)getIntent().getSerializableExtra("DATA");
        if(gateModel!=null){
            id = gateModel.getId();
            gateTitle.setText(gateModel.getTitle());
            gateDesc.setText(gateModel.getDescription());
            if (gateModel.getHeaderImage() != null) {
                progressBar.setVisibility(View.VISIBLE);
                gateImgview.setVisibility(View.GONE);
                LoadInPicasso.getInstance(this).loadPic(gateImgview, progressBar, gateModel.getHeaderImage());

            }

        }


    }

    private void getGatDetails(String id) {
        LogUtil.createLog("Eventdetail By Id :",id);

        dlg.showDialog();
        Call<GateDetailResponse> call = mApiInterface.getGateDetaillById(Constant.SELECTED_LANG,id);
        call.enqueue(new Callback<GateDetailResponse>() {
            @Override
            public void onResponse(Call<GateDetailResponse> call, Response<GateDetailResponse> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    GateDetailResponse eventdetailResponse = response.body();
                    if(eventdetailResponse!=null&&eventdetailResponse.getStatus()){
                        GateDetailResponse.ResponseData responseData = eventdetailResponse.getResponseData();
                        if(responseData!=null) {
                            eventModels = responseData.getEventDetails();
                            EventDetailGateData eventDetailGateData = responseData.getGateData();

                            if(eventDetailGateData!=null) {
                                gateModel = eventDetailGateData.getData();
                                audios = eventDetailGateData.getAudio();
                                galleries = eventDetailGateData.getGallery();
                            }
                            loadDataInView();
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<GateDetailResponse> call, Throwable t) {
                Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadDataInView() {
        if(audios!=null&&audios.size()>0){
            audioAdapter = new AudioAdapter(this,audios);
            recyclerview_audioguide.setAdapter(audioAdapter);
        }
        if(galleries!=null&&galleries.size()>0){
            galleryAdapter = new GalleryAdapter(this,galleries);
            eventGalleryView.setAdapter(galleryAdapter);
        }
        if(eventModels!=null&&eventModels.size()>0) {
            homeEventAdapter = new HomeEventAdapter(this, eventModels, Constant.EVENT_TOPIC_DETAIL_TYPE);
            homeEventAdapter.setOnItemClickListener(mItemClickListener);
            recyclerview_more_events.setAdapter(homeEventAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;

        }
    }

    HomeEventAdapter.OnItemClickListener mItemClickListener = new HomeEventAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            new Utils().startEventDetailPage(view,position,GateDetail.this,EventDetailActivity.class,eventModels.get(position));
        }
    };
}
