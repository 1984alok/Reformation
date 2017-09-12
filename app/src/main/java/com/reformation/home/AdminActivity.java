package com.reformation.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import apihandler.ApiClient;
import apihandler.ApiInterface;

public class AdminActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

    }
    public void click(View v){
        finish();
    }
}
