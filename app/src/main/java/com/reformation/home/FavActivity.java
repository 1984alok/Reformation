package com.reformation.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.fragment.Chapter_Audio;
import com.reformation.home.fragment.EventFavFragment;
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

public class FavActivity extends AppCompatActivity implements View.OnClickListener {
    FragAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private TextView topicHeader;
    private ImageView back;
    private CustomProgresDialog dlg;

    private EventFavFragment eventFavFragment;


    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getSupportFragmentManager());
        eventFavFragment = EventFavFragment.newInstance(1);
        adapter.addFragment(eventFavFragment, getResources().getString(R.string.event));
       // adapter.addFragment(eventFavFragment, getResources().getString(R.string.exhibitors));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_guide);
        dlg = CustomProgresDialog.getInstance(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        back = (ImageView) findViewById(R.id.imageViewLeft);
        topicHeader = (TextView) findViewById(R.id.textViewHeaderTitle);
        topicHeader.setText(getResources().getString(R.string.favourites));

        back.setOnClickListener(this);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewLeft:
                // super.onBackPressed();
                finish();
                break;

        }
    }
}
