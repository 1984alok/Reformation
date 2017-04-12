package com.reformation.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import adapter.GateAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
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
        gateRecyclerView.setFocusable(false);
        gateRecyclerView.setNestedScrollingEnabled(false);
        layoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        gateRecyclerView.setHasFixedSize(true);
        gateRecyclerView.setLayoutManager(layoutManagaer);
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

            GateModel gateModel = model.getResponseData().get(0);
            if (gateModel.getHeaderImage() != null) {
                progressBar.setVisibility(View.VISIBLE);
                gateImgview.setVisibility(View.GONE);
                LoadInPicasso.getInstance(context).loadPic(gateImgview, progressBar, gateModel.getHeaderImage());

            }
            ArrayList<GateModel> gateList = model.getResponseData();
            if(gateList!=null){
                gateAdapter = new GateAdapter(context,gateList);
                gateRecyclerView.setAdapter(gateAdapter);
            }

            gateTitle.setText(gateModel.getTitle());
            gateDesc.setText(gateModel.getDescription() != null ? gateModel.getDescription() : getResources().getString(R.string.topic_desc));

            FontUtls.loadFont(context, "fonts/RobotoCondensed-Bold.ttf", gateTitle);
           // FontUtls.loadFont(context, "fonts/RobotoCondensed-Bold.ttf", gateDesc);
        }
    }
}
