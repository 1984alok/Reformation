package com.reformation.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import adapter.AudioAdapter;
import adapter.AudioDetailAdapter;
import adapter.GalleryAdapter;
import adapter.HomeEventAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
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
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;


public class ExhibitorDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView leftImg,
            favouriteImage,imageViewShare;
    private TextView topicTitle,topicDesc,topic_period,topic_ticketInfo,topic_OpeningInfo,topicHeader,textViewaddrss;

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


    LatLng location;
    String id = "";
    DBAdapter dbAdapter;
    FavDB favDB;
    boolean favStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_detail);

        dlg = CustomProgresDialog.getInstance(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
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

        textViewaddrss =(TextView)findViewById(R.id.textViewMap);

        imageViewShare=(ImageView)findViewById(R.id.imageViewShare);
        catgList=(LinearLayout) findViewById(R.id.catgList);
        mapFrame=(RelativeLayout) findViewById(R.id.mapFrame);

        eventGalleryView = (RecyclerView)findViewById(R.id.horizontal_recycler_eventView);
        recyclerview_audioguide = (RecyclerView)findViewById(R.id.recyclerview_audioguide);
        recyclerview_eventList = (RecyclerView)findViewById(R.id.recyclerview_more_events);

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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favDB.close();
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

                            loadDataInView();

                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<ExhibitorDetailResponseById> call, Throwable t) {
                Log.d("onFailure ::", t.getMessage());
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
                recyclerview_audioguide.setAdapter(audioAdapter);
            }
            if(galleries!=null&&galleries.size()>0){
                galleryAdapter = new GalleryAdapter(this,galleries);
                eventGalleryView.setAdapter(galleryAdapter);
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


    /*AudioAdapter.OnItemClickListener mItemClickListener2 = new AudioAdapter.OnItemClickListener(){
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
    };*/

   /* private void onButtonPressed(int position, String s) {
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
                        if (chapter_audioFragment != null) {
                            chapter_audioFragment.refresh(pos);

                        }
                        if (nearBy_audioFragment != null) {
                            nearBy_audioFragment.refresh(pos);
                        }
                    }
                });
            }
        }
    }*/
}
