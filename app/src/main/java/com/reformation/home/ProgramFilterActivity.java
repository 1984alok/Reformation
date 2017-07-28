package com.reformation.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import adapter.EventCatAdapter;
import adapter.HomeEventAdapter;
import adapter.PlaceCatAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.EvenTCatg;
import model.EventModel;
import model.EventPlaceCatg;
import model.EventResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.LogUtil;
import utils.Utils;

public class ProgramFilterActivity extends AppCompatActivity implements OnDateSelectedListener{

    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat df = DateFormat.getDateInstance();
    @BindView(R.id.resetFiltter)
    TextView resetFiltter;
    @BindView(R.id.textViewFilterDate)
    TextView textViewFilterDate;
    @BindView(R.id.textViewselectall)
    TextView textViewselectall;
    @BindView(R.id.textViewFilterCatg)
    TextView textViewFilterCatg;
    @BindView(R.id.catgRecyclerview)
    RecyclerView catgRecyclerview;
    @BindView(R.id.buttonProgramFilter)
    Button buttonProgramFilter;
    @BindView(R.id.calLayout)
    LinearLayout calLayout;
    @BindView(R.id.buttonClose)
    ImageView buttonClose;


    private ApiInterface mApiInterface;
    private CustomProgresDialog dlg;
    private ReformationApplication reformationApplication ;
    private  ArrayList<EvenTCatg.ResponseDatum> catList;
    LinearLayoutManager linearLayoutManager;
    EventCatAdapter placeCatAdapter;
    String selectDate = "";
    Set<String> dateSelectionSet;
    List<CalendarDay> calendarDaydate;
    String selectCatg = "";
    ArrayList<String> filterbaleCatlist = new ArrayList<>();
    ArrayList<EventModel> eventList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_overlay_header);
        ButterKnife.bind(this);
        widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        widget.setOnDateChangedListener(this);
        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());
        linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        dlg = CustomProgresDialog.getInstance(this);
        reformationApplication = (ReformationApplication)getApplicationContext();
        catgRecyclerview.setLayoutManager(linearLayoutManager);
        getSelectedDatesString();
        textViewFilterDate.setText(selectDate);
        buttonProgramFilter.setTag("0");

        /*Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();*/

        loadCatg();



    }


    private void loadCatg(){
        catList = reformationApplication.getEventCat();
        if(catList!=null && catList.size()>0){
            placeCatAdapter = new EventCatAdapter(this,catList);
            catgRecyclerview.setAdapter(placeCatAdapter);
            placeCatAdapter.setOnItemClickListener(fillterItemClickListner);
            selectAllCatg();
        }else{
            getEventCatgListFromServer();
        }
    }

    private void getEventCatgListFromServer() {

        dlg.showDialog();
        Call<EvenTCatg> call = mApiInterface.getEventCatg(Constant.SELECTED_LANG);
        call.enqueue(new Callback<EvenTCatg>() {
            @Override
            public void onResponse(Call<EvenTCatg> call, Response<EvenTCatg> response) {

                if (response.isSuccessful()) {
                    if(response.body()!=null) {
                        EvenTCatg model = response.body();
                        if(model!=null&& model.getStatus()){
                            ArrayList<EvenTCatg.ResponseDatum> dataModel = model.getResponseData();
                            if(dataModel!=null&&dataModel.size()>0) {
                                for (int i = 0; i < dataModel.size(); i++) {
                                    dataModel.get(i).setChecked(true);
                                    filterbaleCatlist.add(dataModel.get(i).getTitle());
                                }
                                reformationApplication.setEventCat(dataModel);
                                loadCatg();
                            }
                            else
                                Utils.showToast(ProgramFilterActivity.this,getResources().getString(R.string.no_record_found));
                        }else{
                            Utils.showToast(ProgramFilterActivity.this,getResources().getString(R.string.no_record_found));
                        }
                    }

                }
                dlg.hideDialog();

            }

            @Override
            public void onFailure(Call<EvenTCatg> call, Throwable t) {
                // Log.d("onFailure ::", t.getMessage());
                if (dlg != null)
                    dlg.hideDialog();

            }
        });
    }

    EventCatAdapter.OnItemClickListener fillterItemClickListner = new EventCatAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View clickView, View view, int position) {
            if(catList.get(position).isChecked())
                deSelect(catList.get(position));
            else {
                catList.get(position).setChecked(true);
                filterbaleCatlist.add(catList.get(position).getTitle());
            }
            placeCatAdapter.notifyDataSetChanged();

        }
    };

    @OnClick(R.id.textViewselectall)
    public void selectAllCatg(){
        if (catList!=null&catList.size()>0){
            textViewselectall.setText(getResources().getString(R.string.all_selected));
            for (int i = 0; i < catList.size(); i++) {
                if(!catList.get(i).isChecked())
                    catList.get(i).setChecked(true);
                filterbaleCatlist.add(catList.get(i).getTitle());

            }
            placeCatAdapter.notifyDataSetChanged();
        }
    }

    public void deSelect(EvenTCatg.ResponseDatum responseDatum){
        textViewselectall.setText(getResources().getString(R.string.selectall));
        responseDatum.setChecked(false);
        filterbaleCatlist.remove(responseDatum.getTitle());
    }

    @OnClick(R.id.textViewFilterDate)
    void showCalHideCatag(){
        textViewFilterDate.setVisibility(View.GONE);
        calLayout.setVisibility(View.VISIBLE);
        textViewselectall.setVisibility(View.GONE);
        textViewFilterCatg.setVisibility(View.VISIBLE);
        catgRecyclerview.setVisibility(View.GONE);
    }


    @OnClick(R.id.textViewFilterCatg)
    void showCatagHideCal(){
        createDateString();
        textViewFilterDate.setVisibility(View.VISIBLE);
        calLayout.setVisibility(View.GONE);
        textViewselectall.setVisibility(View.VISIBLE);
        textViewFilterCatg.setVisibility(View.GONE);
        catgRecyclerview.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.buttonClose)
    void closeActivity(){
        finish();
    }



    @OnClick(R.id.resetFiltter)
    void resetActivity(){
        finish();
        startActivity(getIntent());

    }



    @OnClick(R.id.buttonProgramFilter)
    void takeOver(){

        if(buttonProgramFilter.getTag()=="0") {
            createDateString();
            if (filterbaleCatlist.size() > 0) {

                for (int i = 0; i < filterbaleCatlist.size(); i++) {
                    if (!selectCatg.equalsIgnoreCase("")) {
                        selectCatg = selectCatg + "," + filterbaleCatlist.get(i);
                    } else
                        selectCatg = filterbaleCatlist.get(i);
                }

            }

            LogUtil.createLog("selectCatg ::::", selectCatg + ":::::" + selectDate);

            loadFilterDataFromServer(selectCatg, selectDate);
        }else{

            Intent intent = new Intent(ProgramFilterActivity.this,FilterResultActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",eventList);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }

    }


    private void loadFilterDataFromServer(String selectCatg, String selectDate) {

        dlg.showDialog();
        Call<EventResponse> call = mApiInterface.filterTopicweek(Constant.SELECTED_LANG,selectDate.trim(),selectCatg.trim());
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                dlg.hideDialog();
                if(response.isSuccessful()){
                    EventResponse model = response.body();
                    if(model!=null)
                    eventList = model.getResponseData();
                    if(eventList!=null){
                        if(eventList.size()>0){
                            buttonProgramFilter.setText(eventList.size()+" "+getResources().getString(R.string.prg_found));
                            buttonProgramFilter.setTag("1");
                            return;
                        }
                    }
                }
                   // Utils.showToast(ProgramFilterActivity.this,getResources().getString(R.string.no_record_found));
                buttonProgramFilter.setText("0 "+getResources().getString(R.string.prg_found));
                buttonProgramFilter.setTag("0");

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                //      Log.d("onFailure ::",t.getMessage());
                if(dlg!=null)
                    dlg.hideDialog();
                buttonProgramFilter.setText("0 "+getResources().getString(R.string.prg_found));
                buttonProgramFilter.setTag("0");
            }

        });
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        getSelectedDatesString();
            /*if (!selectDate.equalsIgnoreCase("") && prevdateSelect != null) {
                if (isDateSelectedGreaterThanCurrentDate()) {
                    selectDate = selectDate + "," + getSelectedDatesString();
                } else {
                    selectDate = getSelectedDatesString() + "," + selectDate;
                }
            } else {
                selectDate = getSelectedDatesString();
                isDateSelectedGreaterThanCurrentDate();
            }

            if (selectDate.contains(",")) {
                String[] dates = selectDate.split(",");
                if (dates.length > 0) {

                    textViewFilterDate.setText(Utils.formatEvenrtDate(dates[0]) + "-" + Utils.formatEvenrtDate(dates[dates.length - 1]));
                }
            } else {
                textViewFilterDate.setText(selectDate);
            }*/

        /*boolean status = isDateSelectedGreaterThanCurrentDate();
        LogUtil.createLog("status :: ",""+isDateSelectedGreaterThanCurrentDate());*/
        // isDateSelectedGreaterThanCurrentDate();
    }


    private void createDateString(){
        dateSelectionSet = new TreeSet<>();
        for (int i = 0; i < calendarDaydate.size(); i++) {
            dateSelectionSet.add(FORMATTER.format(calendarDaydate.get(i).getDate()));

            if (!selectDate.equalsIgnoreCase("")) {
                selectDate = selectDate + "," + FORMATTER.format(calendarDaydate.get(i).getDate());
            } else {
                selectDate = FORMATTER.format(calendarDaydate.get(i).getDate());
            }

        }

        List<String> list = new ArrayList<String>(dateSelectionSet);

        if(list!=null) {
            if (list.size() > 1) {

                textViewFilterDate.setText(Utils.formatEvenrtDate(list.get(0)) + "-" + Utils.formatEvenrtDate(list.get(list.size() - 1)));

            } else if (list.size() == 0) {
                textViewFilterDate.setText((Utils.formatEvenrtDate(list.get(0))));
            }
        }

    }





    private void getSelectedDatesString() {
        calendarDaydate = widget.getSelectedDates();
        if (calendarDaydate == null) {
            return;
        }
    }

    /*private boolean isDateSelectedGreaterThanCurrentDate()  {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        try {
            strDate = sdf.parse(getSelectedDatesString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(prevdateSelect==null){
            prevdateSelect = strDate;
            return false;
        }else{

            return strDate.getTime()>prevdateSelect.getTime();
        }
    }*/
}
