package com.reformation.home;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.fragment.Chapter_Audio;
import com.reformation.home.fragment.NearBy_Audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import adapter.FragAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import database.AudioDB;
import database.DBAdapter;
import model.Audio;
import model.AudioResponse;
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

public class AudioGuideActivity extends AppCompatActivity implements
        View.OnClickListener ,Chapter_Audio.OnFragmentInteractionListener,
        NearBy_Audio.OnNearByFragmentInteractionListener {
    ApiInterface apiInterface;
    FragAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private TextView topicHeader;
    private ImageView back;
    private CustomProgresDialog dlg;
    private Chapter_Audio chapter_audioFragment;
    private NearBy_Audio nearBy_audioFragment;
    ArrayList<Audio> audios = new ArrayList<>();
    public DBAdapter dbAdapter;
    public AudioDB audioDB;
    HashMap<String,Audio> downLoadMap = new HashMap<>();
    AudioDownLoadManager audioDownLoadManager;
    private Timer timerTask = null;
    ArrayList<Audio> downLoadList = new ArrayList<>();


    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getSupportFragmentManager());
        chapter_audioFragment = Chapter_Audio.newInstance(audios);
        nearBy_audioFragment = NearBy_Audio.newInstance(audios);
        adapter.addFragment(chapter_audioFragment,getResources().getString(R.string.chapter_audio));
        adapter.addFragment(nearBy_audioFragment,getResources().getString(R.string.near_by_audio));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_guide);
        if(isAndroid5()){
            windowTransition();
        }else{
            makeEnterTransition();
        }

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        audioDB = new AudioDB(this);
        audioDB.open();
        audioDownLoadManager = AudioDownLoadManager.getInstance(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        dlg = CustomProgresDialog.getInstance(this);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);

        back=(ImageView)findViewById(R.id.imageViewLeft);
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        topicHeader.setText(getResources().getString(R.string.audio_guide));

        back.setOnClickListener(this);


        if(Utils.isNetworkAvailable(this,viewPager)){
            getAudios();
        }



    }

    private void getAudios(){
        dlg.showDialog();
        Call<AudioResponse> call = apiInterface.getAudio(Constant.SELECTED_LANG);
        call.enqueue(new Callback<AudioResponse>() {
            @Override
            public void onResponse(Call<AudioResponse> call, Response<AudioResponse> response) {
                if(response.isSuccessful()&&response.code()==200){
                    AudioResponse audioResponse = response.body();
                    if(audioResponse!=null&&audioResponse.getStatus()){
                        audios = audioResponse.getResponseData();
                        fillterAudio(audios);

                    }
                }
                dlg.hideDialog();
            }

            @Override
            public void onFailure(Call<AudioResponse> call, Throwable t) {
                dlg.hideDialog();
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void fillterAudio(final ArrayList<Audio> audios) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                for (Audio audio:audios) {
                    Audio tempAudio = audioDB.getAudiinfo(audio.getId());
                    if(tempAudio!=null) {
                        audio.setDownloadProgress(tempAudio.getDownloadProgress());
                        audio.setDownloadId(tempAudio.getDownloadId());
                        audio.setDownloadStatus(tempAudio.getDownloadStatus());
                        audio.setSdcardPath(tempAudio.getSdcardPath());
                        audio.setSdcardPathDe(tempAudio.getSdcardPathDe());
                        audio = AudioDownLoadManager.getInstance(AudioGuideActivity.this).checkDownLoadStatusFromDownloadManager(audio);



                    }else{
                        audio.setDownloadProgress(0);
                        audio.setDownloadId(-1);
                        audio.setDownloadStatus(Constant.ACTION_NOT_DOWNLOAD_YET);
                        audio.setSdcardPath("");
                        audio.setSdcardPathDe("");
                    }
                    Location loc= new Location("GPS");
                    loc.setLatitude(audio.getLatitude()!=null?Double.parseDouble(audio.getLatitude()):0.0);
                    loc.setLongitude(audio.getLongitude()!=null?Double.parseDouble(audio.getLongitude()):0.0);
                    audio.setDist(Float.parseFloat(Utils.getDistance(Constant.appLoc,loc)));
                    checkAudioDownloadInfoAndUpdate(audio);

                }

                LogUtil.createLog("Fillter map",audios.get(0).getTitle()+""+audios.get(0).getId()+""+audios.get(0).getDownloadStatus());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
                if(downLoadMap.size()>0) {
                    startTimer();
                }
            }
        }.execute();



    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                getWindow().getEnterTransition().removeListener(this);
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
            case R.id.imageViewLeft:
                // super.onBackPressed();
                finish();
                break;

        }
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
        }else if(audio.getDownloadStatus()==Constant.ACTION_DOWNLOAD_RUNNING){
            downLoadMap.put(audio.getId(),audio);
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
                            AudioDownLoadManager.getInstance(AudioGuideActivity.this).checkDownLoadStatusFromDownloadManager(audio);
                            checkAudioDownloadInfoAndUpdate(audio);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (chapter_audioFragment != null) {
                                        chapter_audioFragment.refreshProgress(audio, pos);

                                    }
                                    if (nearBy_audioFragment != null) {
                                        nearBy_audioFragment.refreshProgress(audio, pos);
                                    }
                                }
                            });



                        }
                    }

                }
            }, 0, 200);
        }
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
    public void onFragmentInteraction(int pos,String tag) {
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
    }


    @Override
    public void onNearByFragmentInteraction(int pos,String tag) {
        if(tag.equals("start")) {
            initDownLoad(audios.get(pos));
        }else  if(tag.equals("delete")) {
            deleteAudio(audios.get(pos),pos);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioDB.close();
        dbAdapter.close();
    }
}
