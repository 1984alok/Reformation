package com.reformation.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.reformation.home.fragment.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import adapter.AudioAdapter;
import adapter.GalleryAdapter;
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
import model.FavModel;
import model.Gallery;
import model.GateModel;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.AudioDownLoadManager;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.TransitionAdapter;
import utils.Utils;

import static utils.Utils.isAndroid5;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_PARAM_ID = "place_id";
    private ImageView leftImg,
            rightFilterImg,imageCalandar,imageViewShare;
    private TextView topicTitle,topicDesc,topic_date,topicHeader,textViewaddrss,
            textViewTopicSubTitle1,artist,textViewTicket,
            galleryTxt;
    private CardView audioCard;
    private LinearLayout catgList;
    private FrameLayout mainView;
    private EventModel event;
    private RelativeLayout mapFrame;
    private RecyclerView eventGalleryView,recyclerview_audioguide;
    private GalleryAdapter galleryAdapter;
    private AudioAdapter audioAdapter;
    private LinearLayoutManager layoutManager,audiLayoutManager;
    private ApiInterface mApiInterface;
    private CustomProgresDialog dlg;

    private ArrayList<Gallery> galleries;
    private ArrayList<Audio> audios;
    private Exhibitor exhibitor;
    private GateModel gateModel;
    LatLng locationPlace;
    String id = "";
    FavDB favDB;
    boolean favStatus = false;
    HashMap<String,Audio> downLoadMap = new HashMap<>();
    public DBAdapter dbAdapter;
    public AudioDB audioDB;
    AudioDownLoadManager audioDownLoadManager;
    private Timer timerTask = null;
    String dtTxt ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        audioDB = new AudioDB(this);
        audioDB.open();
        audioDownLoadManager = AudioDownLoadManager.getInstance(this);

        dlg = CustomProgresDialog.getInstance(this);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        favDB = new FavDB(this);
        favDB.open();
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        if(isAndroid5()){
            windowTransition();
        }else{
            makeEnterTransition();
        }


        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        rightFilterImg=(ImageView)findViewById(R.id.imageViewRight);

        topicTitle =(TextView)findViewById(R.id.txtEventName);
        topicDesc =(TextView)findViewById(R.id.textViewTopicDesc);
        topic_date =(TextView)findViewById(R.id.txtEventTime);

        textViewaddrss =(TextView)findViewById(R.id.textViewMap);
        textViewTopicSubTitle1 =(TextView)findViewById(R.id.textViewTopicSubTitle1);
        artist =(TextView)findViewById(R.id.artist);
        textViewTicket =(TextView)findViewById(R.id.textViewTicket);

        imageCalandar=(ImageView)findViewById(R.id.imageCalandar);
        imageViewShare=(ImageView)findViewById(R.id.imageViewShare);
        catgList=(LinearLayout) findViewById(R.id.catgList);
        mapFrame=(RelativeLayout) findViewById(R.id.mapFrame);

        eventGalleryView = (RecyclerView)findViewById(R.id.horizontal_recycler_eventView);
        recyclerview_audioguide = (RecyclerView)findViewById(R.id.recyclerview_audioguide);

        galleryTxt =(TextView)findViewById(R.id.galleryTxt);
        audioCard =(CardView) findViewById(R.id.audioCard);

        recyclerview_audioguide.setItemAnimator(null);
        recyclerview_audioguide.setFocusable(false);
        recyclerview_audioguide.setNestedScrollingEnabled(false);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        audiLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

       // Drawable drawable = getResources().getDrawable(R.drawable.line_devider);
       // eventGalleryView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST,drawable));

        recyclerview_audioguide.setHasFixedSize(true);
        eventGalleryView.setHasFixedSize(true);

        eventGalleryView.setLayoutManager(layoutManager);
        recyclerview_audioguide.setLayoutManager(audiLayoutManager);
        rightFilterImg.setImageResource(R.drawable.heart);

        topicHeader.setText(getResources().getString(R.string.program_text));
        leftImg.setOnClickListener(this);
        mapFrame.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);
        rightFilterImg.setOnClickListener(this);
        imageCalandar.setOnClickListener(this);

        event = (EventModel) getIntent().getSerializableExtra("DATA");
        if(event!=null){
            id = event.getId();
            topicTitle.setText(event.getTitle());
            String sdate = Utils.formatDate(event.getDate());
            String dtTxt =  Utils.getWeekNameFromDay(event.getDate())+", "+Utils.getDaywithTHFormatFromDate(sdate)+" "+
                    Utils.getMonthFromDate(sdate)+" | "+ event.getStart().split(":")[0]+Utils.getHrFormat()+"-";
            topic_date.setText(dtTxt);
            createEventCatgList(this,catgList,event.getCategory());
            textViewTicket.setText(getResources().getString(R.string.ticket_info));
           // getEventDetails(id);

        }


    }




    private void getEventDetails(String id) {

        LogUtil.createLog("Eventdetail By Id :",id);

        dlg.showDialog();
        Call<EventdetailResponse> call = mApiInterface.getEventDetailById(Constant.SELECTED_LANG,id);
        call.enqueue(new Callback<EventdetailResponse>() {
            @Override
            public void onResponse(Call<EventdetailResponse> call, Response<EventdetailResponse> response) {

                if (response.isSuccessful()) {
                    EventdetailResponse eventdetailResponse = response.body();
                    if(eventdetailResponse!=null&&eventdetailResponse.getStatus()){
                        EventResponseData eventResponseData = eventdetailResponse.getResponseData();
                        if(eventResponseData!=null) {
                            event = eventResponseData.getEventDetails();
                            EventDetailGateData eventDetailGateData = eventResponseData.getGateData();

                            if(eventDetailGateData!=null) {
                                gateModel = eventDetailGateData.getData();
                                audios = eventDetailGateData.getAudio();
                                galleries = eventDetailGateData.getGallery();
                            }

                            EventDetailPlaceData eventDetailPlaceData = eventResponseData.getPlaceData();
                            if(eventDetailPlaceData!=null) {
                                exhibitor = eventDetailPlaceData.getData();

                                ArrayList<Audio> audioArrayList = eventDetailPlaceData.getGateData().getAudio();
                                if (audios!=null&&audioArrayList != null && audioArrayList.size() > 0)
                                    audios.addAll(audioArrayList);

                                ArrayList<Gallery> galleryArrayList = eventDetailPlaceData.getGateData().getGallery();
                                if (galleries!=null&&galleryArrayList != null && galleryArrayList.size() > 0)
                                    galleries.addAll(galleryArrayList);
                            }

                            if(audios!=null&&audios.size()>0) {
                                audios = Utils.removeDuplicateAudio(audios);
                            }
                            fillterAudio(audios);


                        }

                    }

                }
                dlg.hideDialog();
            }

            @Override
            public void onFailure(Call<EventdetailResponse> call, Throwable t) {
          //      Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadDataInView() {

        try {
        if(event!=null){
            topicTitle.setText(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?event.getTitle():event.getTitle_de());
            textViewTopicSubTitle1.setText(event.getSubtitle());
            artist.setText(event.getSpeaker());

            favStatus = favDB.isFav(event.getId(),"event");
            rightFilterImg.setImageResource(favStatus?R.drawable.heart_filled:R.drawable.heart);
            if(event.getDate()!=null && event.getStart()!=null && event.getEnd()!=null) {
                String sdate = Utils.formatDate(event.getDate());
                dtTxt = Utils.getWeekNameFromDay(event.getDate()) + ", " +
                        Utils.getDaywithTHFormatFromDate(sdate) + " " +
                        Utils.getMonthFromDate(sdate) + " | " + Utils.getEventTime(event.getStart()) + Utils.getHrFormat() + " - "
                        + Utils.getEventTime(event.getEnd()) + Utils.getHrFormat();
                topic_date.setText(dtTxt);
            }
            createEventCatgList(this,catgList,event.getCategory());
            textViewTicket.setText(getResources().getString(R.string.ticket_info)+": "+event.getTicket());
            topicDesc.setText(event.getDescp());
        }

        if(exhibitor!=null){
            textViewaddrss.setText(exhibitor.getStreet()+", "+exhibitor.getCity()+", "+exhibitor.getZip());
            locationPlace = new LatLng(Double.parseDouble(exhibitor.getLatitude()), Double.parseDouble(exhibitor.getLongitude()));
        }

        if(audios!=null&&audios.size()>0){
            audioAdapter = new AudioAdapter(this,audios);
            audioAdapter.setOnItemClickListener(mItemClickListener2);
            recyclerview_audioguide.setAdapter(audioAdapter);
            audioCard.setVisibility(View.VISIBLE);
        }else {
            audioCard.setVisibility(View.GONE);
        }
        if(galleries!=null&&galleries.size()>0){
            galleryAdapter = new GalleryAdapter(this,galleries);
            eventGalleryView.setAdapter(galleryAdapter);
            galleryTxt.setVisibility(View.VISIBLE);
        }else{
            galleryTxt.setVisibility(View.GONE);
        }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);
              getEventDetails(id);
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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imageViewShare:

                if(event!=null)
                sentEmail(event);
                break;

            case R.id.mapFrame:

                if(locationPlace!=null)
             startActivity(new Intent(EventDetailActivity.this,MapsLoadActivity.class)
                     .putExtra("lat",locationPlace.latitude)
                     .putExtra("long",locationPlace.longitude)
                     .putExtra("titel",Constant.SELECTED_LANG.equalsIgnoreCase(Constant.LANG_ENG) ?  exhibitor.getPlaceName():exhibitor.getPlace_name_de()));
                break;
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;

            case R.id.imageViewRight:
                toggleHeart();

                break;
            case R.id.imageCalandar:
                HashMap<String,Long> calanderMap = null;
                try{
                    calanderMap  = Utils.getHashMapfromSharedPref(this);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                    if (calanderMap == null) {
                        calanderMap = new HashMap<>();
                    }
                    if (calanderMap != null) {
                        if (calanderMap.get(event.getId()) == null) {
                            String titl = Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?event.getTitle():event.getTitle_de();
                            long status = Utils.addEventToCalenderForTopicWeek(this, Utils.getMillisecFromDate(event.getDate()),
                                    Utils.getMillisecFromDate(event.getDate()),titl );
                            if (status != -1) {
                                calanderMap.put(event.getId(), status);
                                Utils.putHashMapIntoSharedPref(EventDetailActivity.this, calanderMap);
                            }
                            Utils.showToast(EventDetailActivity.this, getResources().getString(R.string.succesfully_added));
                        } else {
                            Utils.showToast(EventDetailActivity.this, getResources().getString(R.string.already_added));
                        }

                    }
                }

                break;
        }
    }

    private void toggleHeart() {

        if(favStatus){
            favDB.deleteFav(event.getId());
            Utils.showDisLikeToast(this);
        }else{
            favDB.createFavinfo(new FavModel(event.getTitle(),event.getTitle_de(),
                    event.getStart(),event.getDate(),event.getId(),"","","event",true));
            Utils.showLikeToast(this);
        }

        favStatus = favDB.isFav(event.getId(),"event");
        new Utils().animateHeartButton(rightFilterImg,favStatus);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        favDB.close();
        audioDB.close();
        dbAdapter.close();
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5,0,5,0);
                viewTxt.setLayoutParams(params);
                viewTxt.setText(catList[i].toString());
                viewTxt.setBackgroundColor(ctx.getResources().getColor(R.color.text_color_five));
                linearLayout.addView(viewTxt);
            }
        }
    }



    AudioAdapter.OnItemClickListener mItemClickListener2 = new AudioAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            switch (clickView.getId()){
                case R.id.imageViewDownLoad:
                    onButtonPressed(position,clickView.getTag().toString());
                    break;
                case R.id.spkr:

                    startActivity(new Intent(EventDetailActivity.this, AudioPlayerActivity.class).putExtra("data",audios.get(position)));

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
                            audio = AudioDownLoadManager.getInstance(EventDetailActivity.this).checkDownLoadStatusFromDownloadManager(audio);


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
                            AudioDownLoadManager.getInstance(EventDetailActivity.this).checkDownLoadStatusFromDownloadManager(audio);
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



    private void sentEmail(EventModel eventModel){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.event)+":"+(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?
                eventModel.getTitle():eventModel.getTitle_de())+"\n\n");
        stringBuilder.append(eventModel.getSpeaker()+"\n\n\n\n");

        stringBuilder.append(eventModel.getDescp()+"\n\n\n\n\n");

        stringBuilder.append(dtTxt+"\n");
        stringBuilder.append(eventModel.getTicket()+"\n");
        stringBuilder.append(getResources().getString(R.string.direction)+" : "+textViewaddrss.getText().toString()+"\n\n\n\n\n\n");
        stringBuilder.append(getResources().getString(R.string.mail_footer));

         /* Create the Intent */
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

/* Fill it with Data */
        emailIntent.setType("plain/text");
       // emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@r2017.org "});
         emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,Constant.SELECTED_LANG.equals(Constant.LANG_ENG)?
                 eventModel.getTitle():eventModel.getTitle_de());

         emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, stringBuilder.toString());

/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }



}
