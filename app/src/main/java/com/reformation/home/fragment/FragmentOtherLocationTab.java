package com.reformation.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reformation.home.ExhibitorDetailActivity;
import com.reformation.home.R;

import java.util.ArrayList;

import adapter.ExhibitorTabAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Exhibitor;
import model.ExhibitorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.OnLoadListener;
import utils.Utils;

/**
 * Created by IMFCORP\alok.acharya on 17/4/17.
 */
public class FragmentOtherLocationTab extends Fragment implements OnLoadListener {
    ApiInterface mApiInterface;
    private CustomProgresDialog dlg;
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManagaer;
    private ArrayList<Exhibitor> exhibitorArrayList;
    private View view;
    public Exhibitor exhibitor;
    public ExhibitorResponse exhibitorResponse;
    private ExhibitorTabAdapter exhibitorTabAdapter;

    OnLoadListener mOnLoadListener;

    private static final String ARG_PARAM2 = "param2";

    private String gateId = "";
    
    
    public FragmentOtherLocationTab(){}


    // TODO: Rename and change types and number of parameters
    public static FragmentOtherLocationTab newInstance(String gateId) {
        FragmentOtherLocationTab fragment = new FragmentOtherLocationTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2,gateId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gateId = getArguments().getString(ARG_PARAM2);


        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null)
            view = inflater.inflate(R.layout.fragment_exhibitor_tab_item, container, false);
        context = getActivity();
        dlg = CustomProgresDialog.getInstance(context);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyClerViewPlace);

        layoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

       // recyclerView.setFocusable(false);
       // recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManagaer);

        getGateList(Constant.TAG_OTHER_LOCATION);
        return view;
    }



    public void getGateList(String tag) {
        dlg.showDialog();
        String date = Utils.getCurrentDate();
        String time = Utils.getCurrentTime();
        Call<ExhibitorResponse> call = (gateId.equals("") ? mApiInterface.getExhibitorList(Constant.SELECTED_LANG,date,time,tag)
                : mApiInterface.getExhibitorListGateRelated(Constant.SELECTED_LANG,date,time,tag,gateId));

        call.enqueue(new Callback<ExhibitorResponse>() {
            @Override
            public void onResponse(Call<ExhibitorResponse> call, Response<ExhibitorResponse> response) {
                dlg.hideDialog();
                if (response.isSuccessful()&&response.code()==200) {
                    exhibitorResponse = response.body();
                    loadExhibitor(exhibitorResponse);
                }

            }

            @Override
            public void onFailure(Call<ExhibitorResponse> call, Throwable t) {
                //  Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadExhibitor(ExhibitorResponse model) {
        if (model != null) {
            exhibitorArrayList = model.getResponseData();
            if(exhibitorArrayList!=null&&exhibitorArrayList.size()>0){
                exhibitorTabAdapter = new ExhibitorTabAdapter(getActivity(),exhibitorArrayList);
                exhibitorTabAdapter.setOnItemClickListener(onItemClickListener);
                recyclerView.setAdapter(exhibitorTabAdapter);
                onLoad(model,exhibitorTabAdapter.getHeightOfView());

            }

        }
    }


    ExhibitorTabAdapter.OnItemClickListener onItemClickListener = new ExhibitorTabAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View clickView, View view, int position) {
            Exhibitor exhibitor = exhibitorArrayList.get(position);
            startActivity(new Intent(getActivity(), ExhibitorDetailActivity.class).putExtra("DATA",exhibitor));
        }
    };


    public void setOnLoadListener(final OnLoadListener mOnLoadListener) {
        this.mOnLoadListener = mOnLoadListener;
    }



    @Override
    public void onLoad(ExhibitorResponse model,int height) {
        if(mOnLoadListener!=null){
            mOnLoadListener.onLoad(model,height);
        }
    }
}
