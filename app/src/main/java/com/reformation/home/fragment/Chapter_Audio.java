package com.reformation.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.AudioPlayerActivity;
import com.reformation.home.EventDetailActivity;
import com.reformation.home.R;
import com.reformation.home.TopicWeekDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.AudioAdapter;
import adapter.AudioDetailAdapter;
import adapter.HomeEventAdapter;
import model.Audio;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Chapter_Audio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Chapter_Audio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chapter_Audio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private AudioDetailAdapter audioAdapter;
    private ArrayList<Audio> audios;
    private RecyclerView recyclerview_audioguide;
    private LinearLayoutManager audiLayoutManager;
    TextView noData;
    private View view;







    public Chapter_Audio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Chapter_Audio.
     */
    // TODO: Rename and change types and number of parameters
    public static Chapter_Audio newInstance(ArrayList<Audio> audios) {
        Chapter_Audio fragment = new Chapter_Audio();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,audios);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            audios = (ArrayList<Audio>) getArguments().getSerializable(ARG_PARAM1);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null)
        view =  inflater.inflate(R.layout.fragment_chapter__audio, container, false);
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int pos,String tag) {
        if (mListener != null) {
            mListener.onFragmentInteraction(pos,tag);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        audiLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview_audioguide = (RecyclerView)view.findViewById(R.id.recyclerview_audioguide);
        recyclerview_audioguide.setItemAnimator(null);
        recyclerview_audioguide.setLayoutManager(audiLayoutManager);
        noData = (TextView)view.findViewById(R.id.nodataTxt);
        if(audios!=null&&audios.size()>0){
            audioAdapter = new AudioDetailAdapter(getActivity(),audios);
            audioAdapter.setOnItemClickListener(mItemClickListener);
            recyclerview_audioguide.setAdapter(audioAdapter);
            noData.setVisibility(View.GONE);
            recyclerview_audioguide.setVisibility(View.VISIBLE);
        }else{
            noData.setText(getResources().getString(R.string.no_record_found));
            noData.setVisibility(View.VISIBLE);
            recyclerview_audioguide.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshProgress(Audio audio,int pos) {
        LogUtil.createLog("Download prgrss",""+audio.getDownloadProgress());
        audios.get(pos).setDownloadProgress(audio.getDownloadProgress());
        audioAdapter.notifyItemChanged(pos);


    }

    public void refresh(int pos) {
        audioAdapter.notifyItemChanged(pos);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int pos,String tag);
    }


    AudioDetailAdapter.OnItemClickListener mItemClickListener = new AudioDetailAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
           switch (clickView.getId()){
                case R.id.downloadRelativeLayout:
                   // onButtonPressed(position);
                    break;
                case R.id.imageViewDownLoad:
                    onButtonPressed(position,clickView.getTag().toString());
                    break;
               case R.id.spkr:
                  startActivity(new Intent(getActivity(), AudioPlayerActivity.class).putExtra("data",audios.get(position)));
                   break;
               case R.id.mapFrame:
                   /* if(view.findViewById(R.id.imageViewDownLoad).getTag().equals("delete")){
                        LogUtil.createLog("delete",""+view.findViewById(R.id.imageViewDownLoad).getTag());
                    }*/
                   break;
            }

        }
    };
}
