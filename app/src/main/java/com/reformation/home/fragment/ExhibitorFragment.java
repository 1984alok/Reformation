package com.reformation.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reformation.home.R;

import adapter.FragAdapter;
import model.Exhibitor;
import model.ExhibitorResponse;
import utils.FontUtls;
import utils.LoadInPicasso;
import utils.LogUtil;
import utils.OnLoadListener;

import static com.reformation.home.R.id.dlg;

/**
 * Created by Alok on 26-03-2017.
 */
public class ExhibitorFragment extends Fragment implements OnLoadListener{


    View view;
    FragAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    LinearLayout frame;
    private TextView topicHeader,gateTitle, gateDesc;
    private ImageView gateImgview,leftImg;
    ProgressBar progressBar;
    public   ExhibitorResponse model;
    FragmentExhibitorTab fragmentExhibitorTab;
    FragmentOtherLocationTab fragmentOtherLocationTab;
    int count = 0;
    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragAdapter(getChildFragmentManager());
        fragmentExhibitorTab = new FragmentExhibitorTab();
        fragmentOtherLocationTab= new FragmentOtherLocationTab();
        adapter.addFragment(fragmentExhibitorTab,getResources().getString(R.string.exhibitors));
        adapter.addFragment(fragmentOtherLocationTab,getResources().getString(R.string.other_location));
        viewPager.setAdapter(adapter);
    }



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
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        frame = (LinearLayout) view.findViewById(R.id.frame);
        setupViewPager(viewPager);
        fragmentExhibitorTab.setOnLoadListener(this);
        fragmentOtherLocationTab.setOnLoadListener(this);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        topicHeader = (TextView) view.findViewById(R.id.textViewHeaderTitle);
        topicHeader.setText(getResources().getString(R.string.exhibitors));
        gateImgview = (ImageView) view.findViewById(R.id.homeMenuImg);
        leftImg=(ImageView)view.findViewById(R.id.imageViewLeft);
        gateTitle = (TextView) view.findViewById(R.id.textViewTopicTitle);
        gateDesc = (TextView) view.findViewById(R.id.textViewTopicDesc);
        progressBar = (ProgressBar) view.findViewById(dlg);
        leftImg.setVisibility(View.GONE);
    }





    public void loadExhibitor(ExhibitorResponse model,int height) {
        if (model != null&&model.getResponseData()!=null) {
            int count = model.getResponseData().size();
            increaseSizeofFrame(count,height);
            /*Exhibitor gateModel = model.getResponseData().get(0);
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
            }*/
        }
    }

    private void increaseSizeofFrame(int count,int height) {
        LogUtil.createLog("exh size", "" + height);

        if(this.count<count) {
            this.count=count;
            int size = height * count;

            LinearLayout.LayoutParams prms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, size);
            frame.setLayoutParams(prms);

            LogUtil.createLog("exh size frame", "" + frame.getHeight());
        }
    }

    @Override
        public void onLoad(ExhibitorResponse model,int height) {
            loadExhibitor(model,height);
        }



}


