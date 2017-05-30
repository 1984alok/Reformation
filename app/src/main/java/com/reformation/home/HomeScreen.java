package com.reformation.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.reformation.home.fragment.ExhibitorFragment;
import com.reformation.home.fragment.GateFragment;
import com.reformation.home.fragment.HomeFragment;
import com.reformation.home.fragment.MapFragment;
import com.reformation.home.fragment.ProgramFragment;

import bottombar.BottomBar;
import bottombar.OnTabReselectListener;
import bottombar.OnTabSelectListener;
import services.LocationFetchingService;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;

public class HomeScreen extends AppCompatActivity {
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_GATE = "gate";
    private static final String TAG_PROG = "program";
    private static final String TAG_EXHIBITOR = "exhibitor";
    private static final String TAG_MAP = "map";
    public static String CURRENT_TAG = TAG_HOME;
    private Handler mHandler;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        Utils.createCLAPPDirectory();
        mHandler = new Handler();
        /*if (savedInstanceState == null) {
            navItemIndex = R.id.tab_home;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment(navItemIndex);
        }*/
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
              //  messageView.setText(TabMessage.get(tabId, false));
                loadHomeFragment(tabId);
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
               // Toast.makeText(getApplicationContext(), TabMessage.get(tabId, true), Toast.LENGTH_LONG).show();
            }
        });
    }


        public void loadHomeFragment(final int tabId) {
            // Sometimes, when fragment has huge data, screen seems hanging
            // when switching between navigation menus
            // So using runnable, the fragment is loaded with cross fade effect
            // This effect can be seen in GMail app
            Runnable mPendingRunnable = new Runnable() {
                @Override
                public void run() {
                    // update the main content by replacing fragments
                    Fragment fragment = getFragment(tabId);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            };

            // If mPendingRunnable is not null, then add to the message queue
            if (mPendingRunnable != null) {
                mHandler.post(mPendingRunnable);
            }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            CURRENT_TAG = TAG_HOME;
          //  loadHomeFragment(R.id.tab_home);
            return;
    }



    private Fragment getFragment(int id) {
        switch (id){
            case R.id.tab_home:
                CURRENT_TAG = TAG_HOME;
                HomeFragment fragmentHome = new HomeFragment();
                return fragmentHome;
            case R.id.tab_gate:
                CURRENT_TAG = TAG_GATE;
                GateFragment frgGateFragment = new GateFragment();
                return  frgGateFragment;
            case R.id.tab_prog:
                CURRENT_TAG = TAG_PROG;
                ProgramFragment programFragment = new ProgramFragment();
                return programFragment;
            case R.id.tab_exibitor:
                CURRENT_TAG = TAG_EXHIBITOR;
                ExhibitorFragment  exhibitorFragment = new ExhibitorFragment();
                return exhibitorFragment;
            case R.id.tab_map:
                CURRENT_TAG = TAG_MAP;
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("LANG_CHANGE");
        registerReceiver(changeLangReciever,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeLangReciever);
    }

    private BroadcastReceiver changeLangReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            loadHomeFragment(R.id.tab_home);
        }
    };


    public void startScreen(Intent mIntent){
        startActivity(mIntent);
        overridePendingTransition(R.anim.slide_up,R.anim.stay);
    }



    public void setBootombar(){
        bottomBar.setDefaultTab(R.id.tab_prog);
    }



}
