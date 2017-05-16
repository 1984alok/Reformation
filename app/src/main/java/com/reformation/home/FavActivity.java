package com.reformation.home;

import android.annotation.TargetApi;
import android.content.Intent;
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
import com.reformation.home.fragment.PlaceFavFragment;

import java.util.ArrayList;

import adapter.FragAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import database.DBAdapter;
import model.Audio;
import model.AudioResponse;
import model.EventModel;
import model.Exhibitor;
import model.FavModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.TransitionAdapter;
import utils.Utils;

import static utils.Utils.isAndroid5;

public class FavActivity extends AppCompatActivity implements View.OnClickListener,
        EventFavFragment.OnListFragmentInteractionListener,PlaceFavFragment.OnListPlaceFragmentInteractionListener{
    FragAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private TextView topicHeader;
    private ImageView back;
    private CustomProgresDialog dlg;

    private EventFavFragment eventFavFragment;
       private PlaceFavFragment placeFavFragment;



    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getSupportFragmentManager());
        eventFavFragment = EventFavFragment.newInstance("event",1);
        placeFavFragment = PlaceFavFragment.newInstance("place",1);
        adapter.addFragment(eventFavFragment, getResources().getString(R.string.event));
        adapter.addFragment(placeFavFragment, getResources().getString(R.string.exhibitors));
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

    @Override
    public void onListFragmentInteraction(FavModel item) {
        EventModel model = new EventModel();
        model.setId(item.getId());
        model.setTitle(item.getName());
        model.setTitle_de(item.getName_de());
        model.setDate(item.getDate());
        model.setStart(item.getStart());
        startActivity(new Intent(this,EventDetailActivity.class).putExtra("DATA",model));
    }


    @Override
    public void onListPlaceFragmentInteraction(FavModel item) {
        Exhibitor model = new Exhibitor();
        model.setId(item.getId());
        model.setPlaceName(item.getName());
        model.setPlace_name_de(item.getName_de());
        model.setAddress(item.getAddrss());
        startActivity(new Intent(this,ExhibitorDetailActivity.class).putExtra("DATA",model));
    }
}
