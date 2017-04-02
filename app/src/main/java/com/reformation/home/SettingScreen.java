package com.reformation.home;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import utils.Utils;

public class SettingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        Utils.showAlert(this,"HI","HELLO",onClickListener);
    }


    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Utils.showToast(SettingScreen.this,"alok");
        }
    };
}
