package com.reformation.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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
import android.widget.TextView;

import com.reformation.home.EventDetailActivity;
import com.reformation.home.GateDetail;
import com.reformation.home.R;
import com.reformation.home.TopicWeekDetailActivity;

import java.util.ArrayList;
import java.util.ListIterator;

import adapter.GateAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.EventModel;
import model.GateModel;
import model.GateResponsModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.FontUtls;
import utils.LoadInPicasso;

/**
 * Created by Alok on 26-03-2017.
 */
public class GateFragment extends Fragment {

    public GateFragment() {
    }

    View view;
    ApiInterface mApiInterface;
    private ImageView gateImgview, leftImg, rightImg;
    private TextView gateTitle, gateDesc, gateHeader;
    private CustomProgresDialog dlg;
    ProgressBar progressBar;
    private Context context;
    private GateAdapter gateAdapter;
    private RecyclerView gateRecyclerView;
    private LinearLayoutManager layoutManagaer;
    ArrayList<GateModel> gateList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_gate, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gateImgview = (ImageView) view.findViewById(R.id.homeMenuImg);
        progressBar = (ProgressBar) view.findViewById(R.id.dlg);
        leftImg = (ImageView) view.findViewById(R.id.imageViewLeft);
        rightImg = (ImageView) view.findViewById(R.id.imageViewRight);
        gateTitle = (TextView) view.findViewById(R.id.textViewTopicTitle);
        gateDesc = (TextView) view.findViewById(R.id.textViewTopicDesc);
        gateHeader = (TextView) view.findViewById(R.id.textViewHeaderTitle);
        leftImg.setVisibility(View.GONE);
        rightImg.setVisibility(View.GONE);
        gateHeader.setText(getResources().getString(R.string.gate));
        context = getActivity();
        dlg = CustomProgresDialog.getInstance(context);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        gateRecyclerView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_eventView);

        layoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        gateRecyclerView.setFocusable(false);
        gateRecyclerView.setNestedScrollingEnabled(false);
        gateRecyclerView.setHasFixedSize(true);
        gateRecyclerView.setLayoutManager(layoutManagaer);

        progressBar.setVisibility(View.GONE);
        gateImgview.setVisibility(View.VISIBLE);
        gateImgview.setImageResource(R.drawable.gate);
        FontUtls.loadFont(context, "fonts/RobotoCondensed-Bold.ttf", gateTitle);
        getGateList();
    }

    private void getGateList() {

        dlg.showDialog();
        Call<GateResponsModel> call = mApiInterface.getGateList(Constant.SELECTED_LANG);
        call.enqueue(new Callback<GateResponsModel>() {
            @Override
            public void onResponse(Call<GateResponsModel> call, Response<GateResponsModel> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    GateResponsModel model = response.body();
                    loadGate(model);
                }

            }

            @Override
            public void onFailure(Call<GateResponsModel> call, Throwable t) {
                Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadGate(GateResponsModel model) {

        if (model != null) {

            gateList = model.getResponseData();
            if(gateList!=null){
                gateAdapter = new GateAdapter(context,gateList);
                gateAdapter.setOnItemClickListener(onItemClickListener);
                gateRecyclerView.setAdapter(gateAdapter);
            }
           // FontUtls.loadFont(context, "fonts/RobotoCondensed-Bold.ttf", gateDesc);
        }
    }



    GateAdapter.OnItemClickListener onItemClickListener = new GateAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View clickView, View view, int position) {
            startGetDetailPage(view,gateList.get(position));
        }
    };


    public void startGetDetailPage(View v,GateModel  gateModel){
        Intent transitionIntent = new Intent(getActivity(), GateDetail.class);
        transitionIntent.putExtra("DATA",gateModel);

      //  View catagPlaceHolder = v.findViewById(R.id.card_view);
        TextView txtEventName = (TextView) v.findViewById(R.id.gateName);
       // TextView txtEventTime = (TextView) v.findViewById(R.id.textViewTopicDesc);

        View navigationBar = ((Activity)getActivity()).findViewById(android.R.id.navigationBarBackground);
        View statusBar =((Activity)getActivity()).findViewById(android.R.id.statusBarBackground);

       // Pair<View, String> holderPair = Pair.create((View) catagPlaceHolder, "headerPic");
        Pair<View, String> titelPair = Pair.create((View) txtEventName, "txtGateName");
       // Pair<View, String> timePair = Pair.create((View) txtEventTime, "txtGateDesc");
        Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
        Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
        ArrayList<Pair<View, String>> list = new ArrayList<>();

       // list.add(holderPair);
        list.add(titelPair);
      //  list.add(timePair);

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

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity)getActivity()),sharedElements);
        ActivityCompat.startActivity(((Activity)getActivity()), transitionIntent, options.toBundle());
    }

}
