package com.reformation.home;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import adapter.AudioAdapter;
import adapter.GalleryAdapter;
import adapter.HomeEventAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import database.AudioDB;
import database.DBAdapter;
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
import services.AudioDownLoadManager;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LoadInPicasso;
import utils.LogUtil;
import utils.TransitionAdapter;
import utils.Utils;

import static utils.Utils.isAndroid5;

public class GateDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView gateImgview,leftImg,rightFilterImg;
    private TextView gateTitle, gateDesc, gateHeader,galleryTxt;
    private ProgressDialog dlg;
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
    HashMap<String,Audio> downLoadMap = new HashMap<>();
    public DBAdapter dbAdapter;
    public AudioDB audioDB;
    AudioDownLoadManager audioDownLoadManager;
    private Timer timerTask = null;
    private RelativeLayout audioRel;


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

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        audioDB = new AudioDB(this);
        audioDB.open();
        audioDownLoadManager = AudioDownLoadManager.getInstance(this);

      //  dlg = CustomProgresDialog.getInstance(this);
        dlg = new ProgressDialog(this);
        dlg.setMessage("Please wait...");
        dlg.setCancelable(false);
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
        galleryTxt = (TextView)findViewById(R.id.galleryTxt);
        audioRel = (RelativeLayout) findViewById(R.id.audioRel);

        eventGalleryView = (RecyclerView)findViewById(R.id.recyclerview_gallery);
        recyclerview_audioguide = (RecyclerView)findViewById(R.id.recyclerview_audioguide);
        recyclerview_more_events = (RecyclerView)findViewById(R.id.recyclerview_more_events);

        recyclerview_audioguide.setItemAnimator(null);

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

        dlg.show();
        Call<GateDetailResponse> call = mApiInterface.getGateDetaillById(Constant.SELECTED_LANG,id);
        call.enqueue(new Callback<GateDetailResponse>() {
            @Override
            public void onResponse(Call<GateDetailResponse> call, Response<GateDetailResponse> response) {

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
                            if(audios!=null&&audios.size()>0) {
                                audios = Utils.removeDuplicateAudio(audios);
                            }
                            fillterAudio(audios);
                        }

                    }

                }
                dlg.dismiss();
            }

            @Override
            public void onFailure(Call<GateDetailResponse> call, Throwable t) {
               // Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.dismiss();

            }
        });
    }

    private void loadDataInView() {
        if(audios!=null&&audios.size()>0){
            audioAdapter = new AudioAdapter(this,audios);
            audioAdapter.setOnItemClickListener(mItemClickListener2);
            recyclerview_audioguide.setAdapter(audioAdapter);
            audioRel.setVisibility(View.VISIBLE);
        }else {
            audioRel.setVisibility(View.GONE);
        }
        if(galleries!=null&&galleries.size()>0){
            galleryAdapter = new GalleryAdapter(this,galleries);
            eventGalleryView.setAdapter(galleryAdapter);
            galleryTxt.setVisibility(View.VISIBLE);
        }else {
            galleryTxt.setVisibility(View.GONE);
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



    AudioAdapter.OnItemClickListener mItemClickListener2 = new AudioAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            switch (clickView.getId()){
                case R.id.imageViewDownLoad:
                    onButtonPressed(position,clickView.getTag().toString());
                    break;
                case R.id.spkr:

                    startActivity(new Intent(GateDetail.this, AudioPlayerActivity.class).putExtra("data",audios.get(position)));

                    break;
            }

        }
    };

    private void onButtonPressed(int pos, String tag) {
        if(tag.equals("start")) {
            initDownLoad(audios.get(pos));
        }else  if(tag.equals("delete")) {
            deleteAudio(audios.get(pos),pos);
        }
    }

    private void deleteAudio(final Audio audio, final int pos) {
        if(audioDB!=null){
            ///  audioDB.deleteAudio(audio.getId());
            if( audioDB.updateAppDownLoadInfo(audio.getId(),Constant.ACTION_NOT_DOWNLOAD_YET)) {
                audio.setDownloadStatus(Constant.ACTION_NOT_DOWNLOAD_YET);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audioAdapter.notifyItemChanged(pos);
                    }
                });
            }
        }
    }





    private void fillterAudio(final ArrayList<Audio> audios) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if(audios!=null&&audios.size()>0) {
                    for (Audio audio : audios) {
                        Audio tempAudio = audioDB.getAudiinfo(audio.getId());
                        if (tempAudio != null) {
                            audio.setDownloadProgress(tempAudio.getDownloadProgress());
                            audio.setDownloadId(tempAudio.getDownloadId());
                            audio.setDownloadStatus(tempAudio.getDownloadStatus());
                            audio.setSdcardPath(tempAudio.getSdcardPath());
                            audio.setSdcardPathDe(tempAudio.getSdcardPathDe());
                            audio = AudioDownLoadManager.getInstance(GateDetail.this).checkDownLoadStatusFromDownloadManager(audio);


                        } else {
                            audio.setDownloadProgress(0);
                            audio.setDownloadId(-1);
                            audio.setDownloadStatus(Constant.ACTION_NOT_DOWNLOAD_YET);
                            audio.setSdcardPath("");
                            audio.setSdcardPathDe("");
                        }
                        Location loc = new Location("GPS");
                        loc.setLatitude(audio.getLatitude() != null ? Double.parseDouble(audio.getLatitude()) : 0.0);
                        loc.setLongitude(audio.getLongitude() != null ? Double.parseDouble(audio.getLongitude()) : 0.0);
                        audio.setDist(Float.parseFloat(Utils.getDistance(Constant.appLoc, loc)));
                        checkAudioDownloadInfoAndUpdate(audio);

                    }

                    LogUtil.createLog("Fillter map", audios.get(0).getTitle() + "" + audios.get(0).getId() + "" + audios.get(0).getDownloadStatus());
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadDataInView();
                if (downLoadMap.size() > 0) {
                    startTimer();
                }
            }
        }.execute();
    }

    public void checkAudioDownloadInfoAndUpdate(Audio audio){
        int status = audio.getDownloadStatus();
        if (status == Constant.ACTION_DOWNLOAD_COMPLETED) {
            LogUtil.createLog("Download ::","ACTION_DOWNLOAD_COMPLETED");
            audio.setDownloadStatus(Constant.ACTION_DOWNLOAD_COMPLETED);
            downLoadMap.remove(audio.getId());
            boolean status1 =  audioDB.updateAppDownLoadID(audio.getId(),audio.getDownloadId());
            boolean status2 =  audioDB.updateAppDownLoadInfo(audio.getId(),audio.getDownloadStatus());
        } else if (status == Constant.ACTION_DOWNLOAD_FAILED) {
            // 1. process for download fail.
            audio.setDownloadStatus(Constant.ACTION_NOT_DOWNLOAD_YET);
            downLoadMap.remove(audio.getId());
            boolean status1 =  audioDB.updateAppDownLoadID(audio.getId(),audio.getDownloadId());
            boolean status2 =  audioDB.updateAppDownLoadInfo(audio.getId(),audio.getDownloadStatus());
            LogUtil.createLog("Download ::","ACTION_DOWNLOAD_COMPLETED");
        }else if (audio.getDownloadStatus() == Constant.ACTION_DOWNLOAD_RUNNING) {
            downLoadMap.put(audio.getId(), audio);
        }

        if(downLoadMap.size()==0){
            stopTimer();
        }

    }

    private void startTimer() {
        Log.i("Timer ","start Timer Called.");
        if (timerTask == null) {
            timerTask = new Timer();
            timerTask.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.i("Timer ","Timer running.");
                    for (int i = 0; i < audios.size(); i++) {
                        final int pos = i;
                        final Audio audio = audios.get(pos);
                        if(audio.getDownloadStatus()==Constant.ACTION_DOWNLOAD_RUNNING) {
                            AudioDownLoadManager.getInstance(GateDetail.this).checkDownLoadStatusFromDownloadManager(audio);
                            checkAudioDownloadInfoAndUpdate(audio);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (audioAdapter != null) {
                                        refreshProgress(audio, pos);
                                    }

                                }
                            });



                        }
                    }

                }
            }, 0, 200);
        }
    }


    public void refreshProgress(Audio audio,int pos) {
        LogUtil.createLog("Download prgrss",""+audio.getDownloadProgress());
        audios.get(pos).setDownloadProgress(audio.getDownloadProgress());
        audioAdapter.notifyItemChanged(pos);


    }


    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
            Log.i("Timer Cancel","Stop Timer Called.");
        }
    }

    public void initDownLoad(Audio audio){
        if(audio.getDownloadStatus()==Constant.ACTION_NOT_DOWNLOAD_YET) {
            String titel = (!Constant.SELECTED_LANG.equals(Constant.LANG_ENG) ? audio.getTitle() : audio.getTitleEn());
            String audioPath = ApiClient.BASE_URL + (!Constant.SELECTED_LANG.equals(Constant.LANG_ENG) ? audio.getAudio() : audio.getAudioEn());
            long downId = audioDownLoadManager.startDownloadManager(audioPath, titel);
            if (downId != -1) {
                audio.setDownloadId((int) downId);
                audio.setDownloadStatus(Constant.ACTION_DOWNLOAD_RUNNING);
                audio.setSdcardPath(audioDownLoadManager.getSdCardPath());
                downLoadMap.put(audio.getId(), audio);
                if(audioDB.getAudiinfo(audio.getId())==null){
                    audioDB.createAudioinfo(audio);
                }else {
                    boolean status = audioDB.updateAppDownLoadID(audio.getId(), audio.getDownloadId());
                    boolean status2 = audioDB.updateAppDownLoadInfo(audio.getId(), audio.getDownloadStatus());
                }

                startTimer();
            }
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioDB.close();
        dbAdapter.close();
    }


}
