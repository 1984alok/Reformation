package com.reformation.home.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reformation.home.R;

import java.util.ArrayList;

import adapter.AudioAdapter;
import adapter.AudioDetailAdapter;
import model.Audio;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearBy_Audio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearBy_Audio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearBy_Audio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AudioDetailAdapter audioAdapter;
    private ArrayList<Audio> audios;
    private RecyclerView recyclerview_audioguide;
    private LinearLayoutManager audiLayoutManager;
    TextView noData;
    private View view;
    private OnFragmentInteractionListener mListener;

    public NearBy_Audio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NearBy_Audio.
     */
    // TODO: Rename and change types and number of parameters
    public static NearBy_Audio newInstance(ArrayList<Audio> audios) {
        NearBy_Audio fragment = new NearBy_Audio();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, audios);
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
        if(view==null)
            view =  inflater.inflate(R.layout.fragment_chapter__audio, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        audiLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview_audioguide = (RecyclerView)view.findViewById(R.id.recyclerview_audioguide);
        recyclerview_audioguide.setLayoutManager(audiLayoutManager);
        noData = (TextView)view.findViewById(R.id.nodataTxt);
        if(audios!=null&&audios.size()>0){
            audioAdapter = new AudioDetailAdapter(getActivity(),audios);
            recyclerview_audioguide.setAdapter(audioAdapter);
            noData.setVisibility(View.GONE);
            recyclerview_audioguide.setVisibility(View.VISIBLE);
        }else{
            noData.setText(getResources().getString(R.string.no_record_found));
            noData.setVisibility(View.VISIBLE);
            recyclerview_audioguide.setVisibility(View.GONE);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
