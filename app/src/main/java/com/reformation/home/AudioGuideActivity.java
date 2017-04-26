package com.reformation.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.fragment.Chapter_Audio;
import com.reformation.home.fragment.NearBy_Audio;

import java.util.ArrayList;

import adapter.FragAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Audio;
import model.AudioResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.TransitionAdapter;
import utils.Utils;

import static utils.Utils.isAndroid5;

public class AudioGuideActivity extends AppCompatActivity implements View.OnClickListener {
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


    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getSupportFragmentManager());
        chapter_audioFragment = Chapter_Audio.newInstance(audios);
    //    nearBy_audioFragment = NearBy_Audio.newInstance(audios);
        adapter.addFragment(chapter_audioFragment,getResources().getString(R.string.chapter_audio));
     //   adapter.addFragment(nearBy_audioFragment,getResources().getString(R.string.near_by_audio));
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
                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);

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
                super.onBackPressed();
                break;

        }
    }
}
