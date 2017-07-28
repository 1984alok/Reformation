package com.reformation.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import adapter.HomeEventAdapter;
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
    private GridLayoutManager horizontalLayoutManagaer;
    AniversaryAdapter aniversaryAdapter;
    ImageView leftImg,rightImg;
    TextView headerTxt,settingTextViewEmailCont,settingTextViewImprint,settingTextViewMobNo,settingTextViewJublle,
            settingTextViewVisitorInfo,settingTextViewAudioGuide;
    Button langEng,langGerman;

    String logOneLink  = "https://www.deutschebahn.com/de/nachhaltigkeit/verantwortung_gesellschaft/Sponsoring/12632278/partnerschaft_reformationsjahr.html?hl=Luther";
    String logoTwoLink = "https://r2017.org/partner/volkswagen";
    ArrayList<AnniversaryModelResponse.ResponseModel> dataList;


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
        settingTextViewEmailCont = (TextView) findViewById(R.id.settingTextViewEmailCont);
        settingTextViewVisitorInfo = (TextView) findViewById(R.id.settingTextViewVisitorInfo);
        settingTextViewAudioGuide = (TextView) findViewById(R.id.settingTextViewAudioGuide);
        settingTextViewMobNo= (TextView) findViewById(R.id.settingTextViewMobNo);
        settingTextViewImprint = (TextView) findViewById(R.id.settingTextViewImprint);
        settingTextViewJublle= (TextView) findViewById(R.id.settingTextViewJublle);
        headerTxt.setText(getResources().getString(R.string.setting));
        dlg = (ProgressBar) findViewById(R.id.dlg);
        horizontalLayoutManagaer
                = new GridLayoutManager(this,2);
        Drawable drawable = getResources().getDrawable(R.drawable.line_devider_two);
        anRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,drawable));
        anRecyclerView.setHasFixedSize(true);
        anRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        leftImg.setOnClickListener(this);
        langGerman.setOnClickListener(this);
        langEng.setOnClickListener(this);
        settingTextViewAudioGuide.setOnClickListener(this);
        settingTextViewVisitorInfo.setOnClickListener(this);
        settingTextViewEmailCont.setOnClickListener(this);
        settingTextViewMobNo.setOnClickListener(this);
        settingTextViewImprint.setOnClickListener(this);
        settingTextViewJublle.setOnClickListener(this);
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
                        dataList = resp.getResponseData();
                        if(dataList!=null&&dataList.size()>0){
                            aniversaryAdapter = new AniversaryAdapter(SettingScreen.this,dataList);
                            aniversaryAdapter.setOnItemClickListener(mItemClickListener);
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
            case R.id.settingTextViewVisitorInfo:
                startActivity(new Intent(SettingScreen.this,FaqActivity.class));
                overridePendingTransition(R.anim.from_middle,R.anim.to_middle);
            break;
            case R.id.settingTextViewAudioGuide:
                startActivity(new Intent(SettingScreen.this,AudioGuideActivity.class));
                overridePendingTransition(R.anim.from_middle,R.anim.to_middle);
            break;
            case R.id.settingTextViewMobNo:
                contactClick();
                break;
            case R.id.settingTextViewImprint:
                showImprintAlert();
                break;
            case R.id.settingTextViewJublle:
                showAnniversaryAlert();
                break;
            case R.id.settingTextViewEmailCont:
                /* Create the Intent */
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

/* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"info@r2017.org "});
               // emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
               // emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

/* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                break;
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

    private void showImprintAlert() {
        new AlertDialog.Builder(this)
                .setView(LayoutInflater.from(this).inflate(R.layout.imprint,null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
        .create().show();
    }


    private void showAnniversaryAlert() {

        View view = LayoutInflater.from(this).inflate(R.layout.anniversary_popup,null);
        view.findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(logOneLink));
                startActivity(i);

            }
        });
        view.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(logoTwoLink));
                startActivity(i);


            }
        });
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }


    public void contactClick(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "004934916434700"));
        startActivity(intent);
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



    AniversaryAdapter.OnItemClickListener mItemClickListener = new AniversaryAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View clickView, View view, int position) {
            if(dataList!=null){
                AnniversaryModelResponse.ResponseModel model = dataList.get(position);
                String uri = model.getExternalLink();
                LogUtil.createLog("External link",uri);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);

            }

        }
    };
}
