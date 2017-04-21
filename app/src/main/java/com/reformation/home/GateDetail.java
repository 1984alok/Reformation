package com.reformation.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GateDetail extends AppCompatActivity implements View.OnClickListener {
    private ImageView topicImgview,leftImg,rightFilterImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_detail);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        leftImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewLeft:
                super.onBackPressed();
                break;

        }
    }
}
