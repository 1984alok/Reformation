package com.reformation.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reformation.home.AudioGuideActivity;
import com.reformation.home.EventDetailActivity;
import com.reformation.home.FaqActivity;
import com.reformation.home.R;
import com.reformation.home.SettingScreen;
import com.reformation.home.TopicWeekDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.ListIterator;

import adapter.HomeEventAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.EventModel;
import model.EventResponse;
import model.HomeMenuModelResponse;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.FontUtls;
import utils.LoadInPicasso;
import utils.Utils;

/**
 * Created by Alok on 26-03-2017.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    public HomeFragment(){}
    View view;
    ApiInterface mApiInterface;
    ArrayList<String> evenCatg = new ArrayList<>();
    ArrayList<EventModel> eventList = new ArrayList<>();
    private ImageView topicImgview,settingImg,favImg;
    private TextView topicTitle,topicDesc;
    private CustomProgresDialog dlg;
    private Context context;
    private HomeEventAdapter homeEventAdapter;
    private RecyclerView eventRecyclerView;
    private LinearLayoutManager horizontalLayoutManagaer;
    private LinearLayout menuLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RelativeLayout topic_of_weekDetail;
    TopicweekResponse model;
    View headerView;
    TextView headerTxt;
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private boolean pendingIntroAnimation;
    TopicweekResponse.TopicWeekModel topicWeekModel;


    private void startIntroAnimation() {

        int actionbarSize = Utils.dpToPx(56);
        headerView.setTranslationY(-actionbarSize);
        headerTxt.setTranslationY(-actionbarSize);
        favImg.setTranslationY(-actionbarSize);
        settingImg.setTranslationY(-actionbarSize);

        headerView.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        headerTxt.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        favImg.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500);
        settingImg.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null)
            view = inflater.inflate(R.layout.fragment_home, container, false);
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorPrimary, R.color.blue);
        topic_of_weekDetail = (RelativeLayout) view.findViewById(R.id.topic_of_weekDetail);
        headerView = view.findViewById(R.id.header);
        topicImgview =(ImageView)view.findViewById(R.id.topicImg);
        settingImg=(ImageView)view.findViewById(R.id.imageViewSetting);
        favImg=(ImageView)view.findViewById(R.id.imageViewFavourite);
        topicTitle =(TextView) view.findViewById(R.id.textViewTopicTitle);
        headerTxt=(TextView) view.findViewById(R.id.textViewHeaderTitle);
        topicDesc =(TextView) view.findViewById(R.id.textViewTopicDesc);
        context = getActivity();
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        eventRecyclerView = (RecyclerView)view.findViewById(R.id.horizontal_recycler_eventView);
        menuLayout = (LinearLayout) view.findViewById(R.id.horizontal_recycler_menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        Drawable drawable = context.getResources().getDrawable(R.drawable.line_devider);
        eventRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST,drawable));
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setFocusable(false);
        eventRecyclerView.setNestedScrollingEnabled(true);
        eventRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        settingImg.setOnClickListener(this);
        topic_of_weekDetail.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        dlg = CustomProgresDialog.getInstance(getActivity());
        getTopicWeek();
        getHomeMenu();
        getEventList();
    }

    private void getEventList() {
        dlg.showDialog();
        Call<EventResponse> call = mApiInterface.get7EventList(Constant.SELECTED_LANG);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    EventResponse model = response.body();
                    loadEventView(model);
                }

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
          //      Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadEventView(EventResponse model) {
        if(model!=null) {
            eventList = model.getResponseData();
            if (eventList != null) {
                homeEventAdapter = new HomeEventAdapter(context, eventList,Constant.EVENT_TOPIC_HOME_TYPE);
                homeEventAdapter.setOnItemClickListener(mItemClickListener);
                eventRecyclerView.setAdapter(homeEventAdapter);
            }
        }
    }

    private void getTopicWeek(){

        dlg.showDialog();
        Call<TopicweekResponse> call = mApiInterface.getTopicWeek(Constant.SELECTED_LANG);
        call.enqueue(new Callback<TopicweekResponse>() {
            @Override
            public void onResponse(Call<TopicweekResponse> call, Response<TopicweekResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                     model = response.body();
                    loadView(model);
                }

            }

            @Override
            public void onFailure(Call<TopicweekResponse> call, Throwable t) {
//                Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();
                Utils.showSnackBar(getActivity().getResources().getString(R.string.network_error_txt),view);

            }
        });
    }

    private void loadView(TopicweekResponse model) {
        topicWeekModel = model.getResponseData().get(0);
        if(topicWeekModel!=null){
           /* if(eventList!=null) {
                eventList = topicWeekModel.getEvent();
                homeEventAdapter = new HomeEventAdapter(context, eventList);
                eventRecyclerView.setAdapter(homeEventAdapter);
            }*/


            if(topicWeekModel.getHeaderPic()!=null) {
                Picasso.with(context).load(ApiClient.BASE_URL+topicWeekModel.getHeaderPic())
                        // .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.ic_photo_frame)
                        .into(topicImgview);

            }

            topicTitle.setText(topicWeekModel.getToWeekTitle());
            topicDesc.setText(topicWeekModel.getToWeekDes()!=null?topicWeekModel.getToWeekDes():getResources().getString(R.string.topic_desc));

            FontUtls.loadFont(context,"fonts/RobotoCondensed-Bold.ttf",topicTitle);
           // FontUtls.loadFont(context,"fonts/RobotoCondensed-Bold.ttf",topicDesc);


        }


    }

    private void getHomeMenu(){
        dlg.showDialog();
        Call<HomeMenuModelResponse> call = mApiInterface.getMenu(Constant.SELECTED_LANG);
        call.enqueue(new Callback<HomeMenuModelResponse>() {
            @Override
            public void onResponse(Call<HomeMenuModelResponse> call, Response<HomeMenuModelResponse> response) {
                if(response.isSuccessful()){
                    ArrayList< HomeMenuModelResponse.MenuModel> modelList = response.body().getResponseData();
                    if(modelList!=null){
                        addMenu(modelList);
                    }


                }
                dlg.hideDialog();

            }

            @Override
            public void onFailure(Call<HomeMenuModelResponse> call, Throwable t) {
                dlg.hideDialog();
            }
        });
    }

    private void addMenu(ArrayList<HomeMenuModelResponse.MenuModel> modelList) {
        TextView txtViewTitle;
        ImageView imgPic;
        ProgressBar progressBar;
        if (menuLayout.getChildCount() > 0) {
            menuLayout.removeAllViews();
        }
        for (HomeMenuModelResponse.MenuModel model : modelList) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.home_menu_list, null);
            if (itemView != null) {
            txtViewTitle = (TextView) itemView.findViewById(R.id.textViewHeaderTitle);
            imgPic = (ImageView) itemView.findViewById(R.id.homeMenuImg);
            progressBar = (ProgressBar) itemView.findViewById(R.id.dlg);
            txtViewTitle.setText(model.getTitle());
            FontUtls.loadFont(getActivity(), "fonts/Roboto-Bold.ttf", txtViewTitle);
            progressBar.setVisibility(View.VISIBLE);
            imgPic.setVisibility(View.GONE);
            if (model.getImage() != null) {
                LoadInPicasso.getInstance(context).loadPic(imgPic, progressBar, model.getImage());
            }

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) menuLayout.getLayoutParams();
            if (params != null) {
                params.setMargins(10, 20, 10, 20);
                itemView.setLayoutParams(params);
            }

            menuLayout.addView(itemView);
        }
    }
        if(menuLayout.getChildCount()>0){
            menuLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAudioPage(v,0,AudioGuideActivity.class);
                   // startActivity(new Intent(getActivity(),AudioGuideActivity.class));
                }
            });
            menuLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAudioPage(v,0, FaqActivity.class);
                }
            });
        }
    }

    private void startAudioPage(View v,int position,Class obj){
        Intent transitionIntent = new Intent(getActivity(), obj);
        TextView txtEventName = (TextView) v.findViewById(R.id.textViewHeaderTitle);

        View navigationBar = v.findViewById(android.R.id.navigationBarBackground);
        View statusBar = v.findViewById(android.R.id.statusBarBackground);
        Pair<View, String> titelPair = Pair.create((View) txtEventName, "header");
        Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
        Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
        ArrayList<Pair<View, String>> list = new ArrayList<>();

        list.add(titelPair);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list.add(navPair);
            list.add(statusPair);
        }

        //remove any views that are null
        for (ListIterator<Pair<View, String>> iter = list.listIterator(); iter.hasNext();) {
            Pair pair = iter.next();
            if (pair.first == null) iter.remove();
        }

        Pair<View, String>[] sharedElements = list.toArray(new Pair[list.size()]);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),sharedElements);
        ActivityCompat.startActivity(getActivity(), transitionIntent, options.toBundle());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewSetting:
                startActivity(new Intent(getActivity(),SettingScreen.class));
                break;
            case R.id.imageViewFavourite:
                break;
            case R.id.topic_of_weekDetail:
                final Intent intent = new Intent(getActivity(),TopicWeekDetailActivity.class).putExtra("Data",topicWeekModel);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshContent();
            }
        },2000);

    }


    private void refreshContent(){
        getTopicWeek();
        getHomeMenu();
        getEventList();
        mSwipeRefreshLayout.setRefreshing(false);
        }

    HomeEventAdapter.OnItemClickListener mItemClickListener = new HomeEventAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            new Utils().startEventDetailPage(view,position,getActivity(),EventDetailActivity.class,eventList.get(position));
        }
    };
}
