package com.reformation.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FaqActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView topicHeader;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        back=(ImageView)findViewById(R.id.imageViewLeft);
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        topicHeader.setText(getResources().getString(R.string.AtoZ));
        back.setOnClickListener(this);
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
