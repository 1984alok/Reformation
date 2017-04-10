package com.reformation.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reformation.home.fragment.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Locale;

import adapter.AniversaryAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.AnniversaryModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.LogUtil;
import utils.Utils;

public class SettingScreen extends AppCompatActivity implements View.OnClickListener{

    ApiInterface  apiInterface;
    ProgressBar dlg;
    RecyclerView anRecyclerView;
    private LinearLayoutManager horizontalLayoutManagaer;
    AniversaryAdapter aniversaryAdapter;
    ImageView leftImg,rightImg;
    TextView headerTxt;
    Button langEng,langGerman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _init();
    }


    private void _init(){
        setContentView(R.layout.activity_setting_screen);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        anRecyclerView = (RecyclerView)findViewById(R.id.anniversaryRecycleview);
        rightImg = (ImageView) findViewById(R.id.imageViewRight);
        leftImg = (ImageView) findViewById(R.id.imageViewLeft);
        langEng = (Button) findViewById(R.id.langEngBtn);
        langGerman = (Button) findViewById(R.id.langDeBtn);
        rightImg.setVisibility(View.INVISIBLE);
        headerTxt = (TextView) findViewById(R.id.textViewHeaderTitle);
        headerTxt.setText(getResources().getString(R.string.setting));
        dlg = (ProgressBar) findViewById(R.id.dlg);
        horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        Drawable drawable = getResources().getDrawable(R.drawable.anniversary_devider);
        anRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST,drawable));
        anRecyclerView.setHasFixedSize(true);
        anRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        leftImg.setOnClickListener(this);
        langGerman.setOnClickListener(this);
        langEng.setOnClickListener(this);
        LogUtil.createLog("lang ",Constant.SELECTED_LANG);
        if(Constant.SELECTED_LANG.equals(Constant.LANG_ENG)){
            callEngBtn();
        }else if(Constant.SELECTED_LANG.equals(Constant.LANG_GERMAN)){
            callDEBtn();
        }

        doCallAnniversary();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.locale == Locale.ENGLISH) {
            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
        } else if (newConfig.locale == Locale.GERMAN){
            Toast.makeText(this, "French", Toast.LENGTH_SHORT).show();
        }
    }

    private void doCallAnniversary(){
        dlg.setVisibility(View.VISIBLE);
        anRecyclerView.setVisibility(View.GONE);
        Call<AnniversaryModelResponse> call = apiInterface.getAnniversaryPartner(Constant.LANG_ENG);
        call.enqueue(new Callback<AnniversaryModelResponse>() {
            @Override
            public void onResponse(Call<AnniversaryModelResponse> call, Response<AnniversaryModelResponse> response) {
                if(response.isSuccessful()){
                    AnniversaryModelResponse resp = response.body();
                    if(resp.getStatusCode().equalsIgnoreCase(Constant.SATUS_TRUE)){
                        ArrayList<AnniversaryModelResponse.ResponseModel> dataList = resp.getResponseData();
                        if(dataList!=null&&dataList.size()>0){
                            aniversaryAdapter = new AniversaryAdapter(SettingScreen.this,dataList);
                            anRecyclerView.setAdapter(aniversaryAdapter);
                            dlg.setVisibility(View.GONE);
                            anRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AnniversaryModelResponse> call, Throwable t) {

            }
        });
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;
            case R.id.langEngBtn:
                callEngBtn();
                Constant.SELECTED_LANG = Constant.LANG_ENG;
                Utils.changeLang(SettingScreen.this,Constant.SELECTED_LANG);
                _init();
                sendBroadcast(new Intent("LANG_CHANGE"));
                break;
            case R.id.langDeBtn:
                callDEBtn();
                Constant.SELECTED_LANG = Constant.LANG_GERMAN;
                Utils.changeLang(SettingScreen.this,Constant.SELECTED_LANG);
                _init();
                sendBroadcast(new Intent("LANG_CHANGE"));
                break;
        }
    }

    private void callEngBtn() {
        langEng.setFocusable(true);
        langEng.setPressed(true);
        langEng.setSelected(true);
        langEng.setTextColor(getResources().getColor(R.color.white));
        langGerman.setFocusable(false);
        langGerman.setPressed(false);
        langGerman.setSelected(false);
        langGerman.setTextColor(getResources().getColor(R.color.light_grey));

    }
    private void callDEBtn(){
        langEng.setFocusable(false);
        langEng.setPressed(false);
        langEng.setSelected(false);
        langEng.setTextColor(getResources().getColor(R.color.light_grey));
        langGerman.setFocusable(true);
        langGerman.setPressed(true);
        langGerman.setSelected(true);
        langGerman.setTextColor(getResources().getColor(R.color.white));

    }
}
