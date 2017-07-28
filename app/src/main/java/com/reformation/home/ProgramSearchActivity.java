package com.reformation.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import adapter.ExpandableListAdapter;
import adapter.HomeEventAdapter;
import adapter.TopicOverviewAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import apihandler.NetworkStatus;
import model.EventModel;
import model.EventResponse;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;

public class ProgramSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    // ExpandableListAdapter listAdapter;
    //ExpandableListView expListView;
    // List<String> listDataHeader = new ArrayList<>();
    // HashMap<String, ArrayList<EventModel>> listDataChild = new HashMap<>();
    private CustomProgresDialog dlg;
    ApiInterface mApiInterface;
    // ArrayList<TopicweekResponse.TopicWeekModel> topicOvrvwList;
    private SearchView mSearchView;
    TextView textViewCancel;
    ArrayList<EventModel> eventList = new ArrayList<>();
    private HomeEventAdapter homeEventAdapter;
    private RecyclerView eventRecyclerView;
    private LinearLayoutManager LayoutManagaer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_overlay_exhibitor);

        // get the listview
        ///  expListView = (ExpandableListView) findViewById(R.id.expandListView);
        dlg = CustomProgresDialog.getInstance(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mSearchView = (SearchView) findViewById(R.id.searchview);
        textViewCancel= (TextView) findViewById(R.id.textViewCancel);
        eventRecyclerView = (RecyclerView)findViewById(R.id.ecycler_eventView);
        LayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        int searchTextId = getResources().getIdentifier ("android:id/search_src_text", null, null);
        EditText searchBox=((EditText)mSearchView.findViewById (searchTextId));
        if(searchBox!=null) {
            searchBox.setTextColor(getResources().getColor(R.color.grey));
            searchBox.setHintTextColor(getResources().getColor(R.color.grey));
        }

        int searchCloseId = getResources().getIdentifier ("android:id/search_close_btn", null, null);
        ImageView searchClose = (ImageView) mSearchView.findViewById (searchCloseId);
//change color
        searchClose.setColorFilter (Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
// change image
        searchClose.setImageResource(R.drawable.close_white);



        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getResources().getString(R.string.search));

        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(LayoutManagaer);
        homeEventAdapter = new HomeEventAdapter(this, eventList,Constant.EVENT_SEARCH);
        homeEventAdapter.setOnItemClickListener(mItemClickListener);
        eventRecyclerView.setAdapter(homeEventAdapter);

        //   listAdapter = new ExpandableListAdapter(ProgramSearchActivity.this, listDataHeader, listDataChild,expListView);
        //   expListView.setAdapter(listAdapter);

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




    private void loadEventView(EventResponse model) {
        hideKeyBoard();
        if(model!=null) {
            ArrayList<EventModel> getList = model.getResponseData();
            if (getList != null && getList.size()>0) {
                if (eventList.size()>0) {
                    eventList.clear();
                }
                eventList=getList;
                homeEventAdapter = new HomeEventAdapter(this, eventList,Constant.EVENT_SEARCH);
                homeEventAdapter.setOnItemClickListener(mItemClickListener);
                eventRecyclerView.setAdapter(homeEventAdapter);
            }else{
                if (eventList != null) {
                    eventList.clear();
                    homeEventAdapter = new HomeEventAdapter(ProgramSearchActivity.this, eventList, Constant.EVENT_SEARCH);
                    eventRecyclerView.setAdapter(homeEventAdapter);
                }
                Utils.showToast(ProgramSearchActivity.this,getResources().getString(R.string.no_record_found));
            }
        }else{
            if (eventList != null) {
                eventList.clear();
                homeEventAdapter = new HomeEventAdapter(ProgramSearchActivity.this, eventList, Constant.EVENT_SEARCH);
                eventRecyclerView.setAdapter(homeEventAdapter);
            }
            Utils.showToast(ProgramSearchActivity.this,getResources().getString(R.string.no_record_found));
        }

    }


    HomeEventAdapter.OnItemClickListener mItemClickListener = new HomeEventAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            new Utils().startEventDetailPage(view,position,ProgramSearchActivity.this,EventDetailActivity.class,eventList.get(position));

        }
    };



    public void getSearch(String txt){

        dlg.showDialog();
        Call<EventResponse> call = mApiInterface.searchTopicweek(Constant.SELECTED_LANG,txt.trim());
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    EventResponse model = response.body();
                    loadEventView(model);
                }else{
                    if (eventList != null) {
                        eventList.clear();
                        homeEventAdapter = new HomeEventAdapter(ProgramSearchActivity.this, eventList, Constant.EVENT_SEARCH);
                        eventRecyclerView.setAdapter(homeEventAdapter);
                    }
                    Utils.showToast(ProgramSearchActivity.this,getResources().getString(R.string.no_record_found));
                }
                hideKeyBoard();

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                //      Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();
                hideKeyBoard();

            }

        });
        /*dlg.showDialog();
        Call<TopicweekResponse> call = mApiInterface.searchTopicweek(Constant.SELECTED_LANG,txt.trim());
        call.enqueue(new Callback<TopicweekResponse>() {
            @Override
            public void onResponse(Call<TopicweekResponse> call, Response<TopicweekResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()) {
                    TopicweekResponse model = response.body();
                    if (model != null) {
                        topicOvrvwList = model.getResponseData();
                        if (topicOvrvwList != null && topicOvrvwList.size() > 0) {
                            if (listDataChild != null)
                                listDataChild.clear();
                            if (listDataHeader != null)
                                listDataHeader.clear();
                            for (int i = 0; i < topicOvrvwList.size(); i++) {

                                listDataHeader.add(topicOvrvwList.get(i).getToWeekTitle());
                                listDataChild.put(topicOvrvwList.get(i).getToWeekTitle(), topicOvrvwList.get(i).getEvent());
                            }

                            listAdapter.notifyDataSetChanged();


                        }else{
                            if (listDataChild != null)
                                listDataChild.clear();
                            if (listDataHeader != null)
                                listDataHeader.clear();

                            listAdapter.notifyDataSetChanged();
                            Utils.showToast(ProgramSearchActivity.this,getResources().getString(R.string.no_record_found));
                        }
                    }
                }else{
                    if (listDataChild != null)
                        listDataChild.clear();
                    if (listDataHeader != null)
                        listDataHeader.clear();

                    listAdapter.notifyDataSetChanged();
                    Utils.showToast(ProgramSearchActivity.this,getResources().getString(R.string.no_record_found));
                }
                hideKeyBoard();

            }

            @Override
            public void onFailure(Call<TopicweekResponse> call, Throwable t) {
                // Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();

            }
        });*/
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        hideKeyBoard();
        if (!TextUtils.isEmpty(query)) {
            // expListView.clearTextFilter();
            if(NetworkStatus.getInstance().isConnected(this)) {
                LogUtil.createLog("alok", query);
                getSearch(query);
            }else{
                Utils.showToast(this,getResources().getString(R.string.network_error_txt));
            }
        } else {
            //expListView.setFilterText(query.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }



    public void hideKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
