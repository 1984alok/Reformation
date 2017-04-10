package com.reformation.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import apihandler.ApiClient;
import apihandler.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;

public class AudioGuideActivity extends AppCompatActivity {
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_guide);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getAudio(Constant.SELECTED_LANG);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Audio Response",response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
