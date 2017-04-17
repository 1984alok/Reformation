package com.reformation.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reformation.home.R;

import adapter.FragAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.Exhibitor;
import model.ExhibitorResponse;
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
public class ExhibitorFragment extends Fragment {


    View view;
    ApiInterface apiInterface;
    FragAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    private TextView topicHeader,gateTitle, gateDesc;
    private ImageView gateImgview;
    private CustomProgresDialog dlg;
    ProgressBar progressBar;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentExhibitorTab(),getResources().getString(R.string.exhibitors));
        adapter.addFragment(new FragmentOtherLocationTab(),getResources().getString(R.string.other_location));
        viewPager.setAdapter(adapter);
    }

    public ExhibitorFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null)
            view = inflater.inflate(R.layout.fragment_exhibitor, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(view!=null) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            topicHeader = (TextView) view.findViewById(R.id.textViewHeaderTitle);
            topicHeader.setText(getResources().getString(R.string.exhibitors));
            dlg = CustomProgresDialog.getInstance(getActivity());
            gateImgview = (ImageView) view.findViewById(R.id.homeMenuImg);
            gateTitle = (TextView) view.findViewById(R.id.textViewTopicTitle);
            gateDesc = (TextView) view.findViewById(R.id.textViewTopicDesc);
            progressBar = (ProgressBar) view.findViewById(R.id.dlg);
            getGateList();
        }
    }



    private void getGateList() {

        dlg.showDialog();
        String date = Utils.getCurrentDate();
        String time = Utils.getCurrentTime();
        Call<ExhibitorResponse> call = apiInterface.getExhibitorList(Constant.SELECTED_LANG,date,time,"Exhibitors");
        call.enqueue(new Callback<ExhibitorResponse>() {
            @Override
            public void onResponse(Call<ExhibitorResponse> call, Response<ExhibitorResponse> response) {
                dlg.hideDialog();
                if (response.isSuccessful()) {
                    ExhibitorResponse model = response.body();
                    loadExhibitor(model);
                }

            }

            @Override
            public void onFailure(Call<ExhibitorResponse> call, Throwable t) {
                Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    private void loadExhibitor(ExhibitorResponse model) {
        if (model != null) {

            Exhibitor gateModel = model.getResponseData().get(0);
            if (gateModel!=null
                    ) {
                if (gateModel.getHeaderPic() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    gateImgview.setVisibility(View.GONE);
                    LoadInPicasso.getInstance(getActivity()).loadPic(gateImgview, progressBar, gateModel.getHeaderPic());

                }

                gateTitle.setText(gateModel.getPlaceName());
                gateDesc.setText(gateModel.getDescp() != null ? gateModel.getDescp() : getResources().getString(R.string.topic_desc));

                FontUtls.loadFont(getActivity(), "fonts/RobotoCondensed-Bold.ttf", gateTitle);
                // FontUtls.loadFont(context, "fonts/RobotoCondensed-Bold.ttf", gateDesc);
            }
        }
    }
}


