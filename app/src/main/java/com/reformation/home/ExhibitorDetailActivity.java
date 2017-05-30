package com.reformation.home;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import adapter.AudioAdapter;
import adapter.AudioDetailAdapter;
import adapter.GalleryAdapter;
import adapter.HomeEventAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import database.AudioDB;
import database.DBAdapter;
import database.FavDB;
import model.Audio;
import model.EventDetailGateData;
import model.EventDetailPlaceData;
import model.EventModel;
import model.EventResponseData;
import model.EventdetailResponse;
import model.Exhibitor;
import model.ExhibitorDetailResponseById;
import model.FavModel;
import model.Gallery;
import model.GateModel;
import model.PlaceDetailData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.AudioDownLoadManager;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;


public class ExhibitorDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView leftImg,
            favouriteImage,imageViewShare;
    private TextView topicTitle,topicDesc,topic_period,topic_ticketInfo,topic_OpeningInfo,
            topicHeader,textViewaddrss,galleryTxt;
    LatLng locationPlace;
    private LinearLayout catgList;
    private FrameLayout mainView;
    private RelativeLayout mapFrame;
    private RecyclerView eventGalleryView,recyclerview_audioguide,recyclerview_eventList;
    private GalleryAdapter galleryAdapter;
    private AudioAdapter audioAdapter;
    private HomeEventAdapter homeEventAdapter;
    private LinearLayoutManager gallerylayoutManager,audiLayoutManager,eventLayoutManager;
    private ApiInterface mApiInterface;
    private CustomProgresDialog dlg;

    private ArrayList<Gallery> galleries;
    private ArrayList<Audio> audios;
    private ArrayList<EventModel> eventModels;

    private Exhibitor exhibitor;
    private PlaceDetailData placeData;
    HashMap<String,Audio> downLoadMap = new HashMap<>();
    public AudioDB audioDB;
    AudioDownLoadManager audioDownLoadManager;

    private CardView cardAudio;

    LatLng location;
    String id = "";
    DBAdapter dbAdapter;
    FavDB favDB;
    boolean favStatus = false;
    private Timer timerTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_detail);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        audioDB = new AudioDB(this);
        audioDB.open();
        audioDownLoadManager = AudioDownLoadManager.getInstance(this);

        dlg = CustomProgresDialog.getInstance(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        favDB = new FavDB(this);
        favDB.open();
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        favouriteImage=(ImageView)findViewById(R.id.imageViewRight);

        topicTitle =(TextView)findViewById(R.id.txtEventName);
        topicDesc =(TextView)findViewById(R.id.textViewTopicDesc);
        topic_period =(TextView)findViewById(R.id.txtEventTime);
        topic_OpeningInfo= (TextView) findViewById(R.id.textViewOpening);
        topic_ticketInfo= (TextView) findViewById(R.id.textViewTicket);
        galleryTxt= (TextView) findViewById(R.id.galleryTxt);
        cardAudio= (CardView) findViewById(R.id.cardAudio);

        textViewaddrss =(TextView)findViewById(R.id.textViewMap);

        imageViewShare=(ImageView)findViewById(R.id.imageViewShare);
        catgList=(LinearLayout) findViewById(R.id.catgList);
        mapFrame=(RelativeLayout) findViewById(R.id.mapFrame);

        eventGalleryView = (RecyclerView)findViewById(R.id.horizontal_recycler_eventView);
        recyclerview_audioguide = (RecyclerView)findViewById(R.id.recyclerview_audioguide);
        recyclerview_eventList = (RecyclerView)findViewById(R.id.recyclerview_more_events);
        recyclerview_audioguide.setItemAnimator(null);
        recyclerview_audioguide.setFocusable(false);
        recyclerview_audioguide.setNestedScrollingEnabled(false);
        gallerylayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        audiLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        eventLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Drawable drawable = getResources().getDrawable(R.drawable.line_devider);
        // eventGalleryView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST,drawable));

        recyclerview_audioguide.setHasFixedSize(true);
        eventGalleryView.setHasFixedSize(true);
        recyclerview_eventList.setHasFixedSize(true);

        eventGalleryView.setLayoutManager(gallerylayoutManager);
        recyclerview_audioguide.setLayoutManager(audiLayoutManager);
        recyclerview_eventList.setLayoutManager(eventLayoutManager);


        topicHeader.setText(getResources().getString(R.string.exhibitors));
        favouriteImage.setImageResource(R.drawable.heart);
        leftImg.setOnClickListener(this);
        favouriteImage.setOnClickListener(this);
        mapFrame.setOnClickListener(this);
//        imageViewShare.setOnClickListener(this);

        exhibitor = (Exhibitor) getIntent().getSerializableExtra("DATA");
        if(exhibitor!=null){
            id = exhibitor.getId();
            topicTitle.setText(exhibitor.getPlaceName());
            topicDesc.setText(exhibitor.getDescp());
            textViewaddrss.setText(exhibitor.getAddress());
            favStatus = favDB.isFav(id,"place");
            favouriteImage.setImageResource(favStatus?R.drawable.heart_filled:R.drawable.heart);

            getPlaceDetails(id);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;
            case R.id.imageViewRight:
                toggleHeart();

                break;
            case R.id.mapFrame:

                if(locationPlace!=null)
                    startActivity(new Intent(ExhibitorDetailActivity.this,MapsLoadActivity.class)
                            .putExtra("lat",locationPlace.latitude)
                            .putExtra("long",locationPlace.longitude)
                            .putExtra("titel",Constant.SELECTED_LANG.equalsIgnoreCase(Constant.LANG_ENG) ?  placeData.getPlaceName():placeData.getPlaceNameDe()));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favDB.close();
        audioDB.close();
        dbAdapter.close();
    }


    private void toggleHeart() {

        if(favStatus){
            favDB.deleteFav(exhibitor.getId());
            Utils.showDisLikeToast(this);
        }else{
            favDB.createFavinfo(new FavModel(placeData.getPlaceName(),placeData.getPlaceNameDe(),
                    "","",placeData.getId(),exhibitor.getAddress(),exhibitor.getAddress(),"place",true));
            Utils.showLikeToast(this);
        }

        favStatus = favDB.isFav(id,"place");
        new Utils().animateHeartButton(favouriteImage,favStatus);

    }


    private void getPlaceDetails(String id) {

        LogUtil.createLog("Placedetail By Id :",id);

        dlg.showDialog();
        Call<ExhibitorDetailResponseById> call = mApiInterface.getPlaceDetailById(Constant.SELECTED_LANG,id);
        call.enqueue(new Callback<ExhibitorDetailResponseById>() {
            @Override
            public void onResponse(Call<ExhibitorDetailResponseById> call, Response<ExhibitorDetailResponseById> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    ExhibitorDetailResponseById placedetailResponse = response.body();
                    if(placedetailResponse!=null&&placedetailResponse.getStatus()){
                        placeData = placedetailResponse.getResponseData();
                        locationPlace = new LatLng(Double.parseDouble(placeData.getLatitude()), Double.parseDouble(placeData.getLongitude()));
                        if(placeData!=null) {

                            GateModel gateModel = placeData.getGateData();

                            if(gateModel!=null) {
                                audios = gateModel.getAudio();
                                galleries = gateModel.getGallary();
                            }


                            ArrayList<Gallery> placeGalleries = placeData.getPlaceGallery();

                            if(galleries!=null){
                                galleries.addAll(placeGalleries);
                            }else
                                galleries = placeGalleries;

                            eventModels = placeData.getEventsList();

                            if(audios!=null&&audios.size()>0) {
                                audios = Utils.removeDuplicateAudio(audios);
                            }
                            fillterAudio(audios);

                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<ExhibitorDetailResponseById> call, Throwable t) {
               // Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadDataInView() {

        try {
            if(placeData!=null){
                topicTitle.setText(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?placeData.getPlaceName():placeData.getPlaceNameDe());

                if(placeData.getPerSelectionStart()!=null&&placeData.getPerSelectionEnd()!=null
                        && !TextUtils.isEmpty(placeData.getPerSelectionStart())
                        && !TextUtils.isEmpty(placeData.getPerSelectionEnd())) {
                    String sdate = Utils.formatEvenrtDate(placeData.getPerSelectionStart());
                    String edate = Utils.formatEvenrtDate(placeData.getPerSelectionEnd());
                    topic_period.setText(getResources().getString(R.string.period) + ":" + sdate + "-" + edate);
                }else{
                    topic_period.setText("");
                }
                topic_OpeningInfo.setText(getResources().getString(R.string.opening)+":"+getOpeningTime());
                createEventCatgList(this,catgList,placeData.getCategory());
                // topic_ticketInfo.setText(getResources().getString(R.string.ticket_info)+":"+placeData.getTi);
            }

            if(placeData!=null){
                //   textViewaddrss.setText(placeData.getStreet()+""+placeData.getCity()+placeData.getZip()+placeData.getCountry());
                location = new LatLng(Double.parseDouble(placeData.getLatitude()!=null?placeData.getLatitude():"0.0"), Double.parseDouble(placeData.getLongitude()!=null?placeData.getLongitude():"0.0"));
            }

            if(audios!=null&&audios.size()>0){
                audioAdapter = new AudioAdapter(this,audios);
                audioAdapter.setOnItemClickListener(mItemClickListener2);
                recyclerview_audioguide.setAdapter(audioAdapter);
                cardAudio.setVisibility(View.VISIBLE);
            }else {
                cardAudio.setVisibility(View.GONE);
            }
            if(galleries!=null&&galleries.size()>0){
                galleryAdapter = new GalleryAdapter(this,galleries);
                eventGalleryView.setAdapter(galleryAdapter);
                galleryTxt.setVisibility(View.VISIBLE);
            }else {
                galleryTxt.setVisibility(View.GONE);
            }
            if(eventModels!=null&&eventModels.size()>0){
                homeEventAdapter = new HomeEventAdapter(this,eventModels,Constant.EVENT_TDAYTOMORW);
                recyclerview_eventList.setAdapter(homeEventAdapter);
                homeEventAdapter.setOnItemClickListener(mItemClickListener);
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    private String getOpeningTime() {
        StringBuilder stringBuilder = new StringBuilder("");

        if(placeData!=null){
            if(placeData.getOpenSun()!=null&&placeData.getCloseSun()!=null)
                stringBuilder.append(placeData.getOpenSun().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseSun().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.sunday));
            if(placeData.getOpenMon()!=null&&placeData.getCloseMon()!=null)
                stringBuilder.append("\n                 "+placeData.getOpenMon().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseMon().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.monday));
            if(placeData.getOpenTue()!=null&&placeData.getCloseTue()!=null)
                stringBuilder.append("\n                 "+placeData.getOpenTue().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseTue().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.tuesday));
            if(placeData.getOpenWed()!=null&&placeData.getCloseWed()!=null)
                stringBuilder.append("\n                 "+placeData.getOpenWed().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseWed().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.wednesday));
            if(placeData.getOpenThru()!=null&&placeData.getCloseThru()!=null)
                stringBuilder.append("\n                 "+placeData.getOpenThru().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseThru().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.thursday));
            if(placeData.getOpenFri()!=null&&placeData.getCloseFri()!=null)
                stringBuilder.append("\n                 "+placeData.getOpenFri().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseFri().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.friday));
            if(placeData.getOpenSat()!=null&&placeData.getCloseSat()!=null)
                stringBuilder.append("\n                 "+placeData.getOpenSat().split(":")[0]+Utils.getHrFormat()+"-"+placeData.getCloseSat().split(":")[0]+Utils.getHrFormat()+","+getResources().getString(R.string.saterday));
        }
        return stringBuilder.toString();
    }


    public static void createEventCatgList(Context ctx, LinearLayout linearLayout, String catg){
        if(catg!=null){
            String[] catList = catg.split(",");
            if(linearLayout.getChildCount()>0){
                linearLayout.removeAllViews();
            }
            for (int i = 0; i <catList.length ; i++) {
                // if(holder.linearLayout.getChildCount()<catList.length) {
                TextView viewTxt = (TextView) LayoutInflater.from(ctx).inflate(R.layout.event_catg_list, null);
                viewTxt.setText(catList[i].toString());
                viewTxt.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.catg_bg));
                linearLayout.addView(viewTxt);
            }
        }
    }


    HomeEventAdapter.OnItemClickListener mItemClickListener = new HomeEventAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            new Utils().startEventDetailPage(view,position,ExhibitorDetailActivity.this,EventDetailActivity.class,eventModels.get(position));
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

                    startActivity(new Intent(ExhibitorDetailActivity.this, AudioPlayerActivity.class).putExtra("data",audios.get(position)));

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
                            audio = AudioDownLoadManager.getInstance(ExhibitorDetailActivity.this).checkDownLoadStatusFromDownloadManager(audio);


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
                            AudioDownLoadManager.getInstance(ExhibitorDetailActivity.this).checkDownLoadStatusFromDownloadManager(audio);
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




}
