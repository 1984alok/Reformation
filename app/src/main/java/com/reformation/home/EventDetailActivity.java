package com.reformation.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import static utils.Utils.isAndroid5;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_PARAM_ID = "place_id";
    private ImageView topicImgview,leftImg,rightFilterImg;
    private TextView topicTitle,topicDesc,topic_date,topicHeader;
    FrameLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        if(isAndroid5()){
            windowTransition();
        }else{
            makeEnterTransition();
        }
        topicImgview =(ImageView)findViewById(R.id.homeMenuImg);
        leftImg=(ImageView)findViewById(R.id.imageViewLeft);
        rightFilterImg=(ImageView)findViewById(R.id.imageViewRight);
        topicTitle =(TextView)findViewById(R.id.textViewTopicTitle);
        topicDesc =(TextView)findViewById(R.id.textViewTopicDesc);
        topic_date =(TextView)findViewById(R.id.textViewTopicDate);
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);

        topicHeader.setText(getResources().getString(R.string.program_text));
        rightFilterImg.setImageResource(R.drawable.heart);
        leftImg.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        /*getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                mAddButton.animate().alpha(1.0f);
                getWindow().getEnterTransition().removeListener(this);
            }
        });*/
    }





    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
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
