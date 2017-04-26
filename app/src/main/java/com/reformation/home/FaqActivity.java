package com.reformation.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.FaqAdapter;
import apihandler.ApiClient;
import apihandler.ApiInterface;
import model.FaqResponse;
import model.FaqModel;
import model.FaqResponse;
import model.QuestionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.CustomProgresDialog;
import utils.Utils;

public class FaqActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView topicHeader;
    private ImageView back;

    private RecyclerView faq_recycler;
    private ArrayList<QuestionModel> faqModelArrayList;
    private ArrayList<FaqModel> answerList;
    private FaqAdapter adapter;
    ApiInterface apiInterface;
    private CustomProgresDialog dlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        back=(ImageView)findViewById(R.id.imageViewLeft);
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        topicHeader.setText(getResources().getString(R.string.AtoZ));
        dlg = CustomProgresDialog.getInstance(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        back.setOnClickListener(this);
        faq_recycler = (RecyclerView) findViewById(R.id.faq_recycler);
        faqModelArrayList = new ArrayList<>();

        //setData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        faq_recycler.setLayoutManager(layoutManager);

        if(Utils.isNetworkAvailable(this,topicHeader)){
            getFaq();
        }
    }



    private void getFaq(){
        dlg.showDialog();
        Call<FaqResponse> call = apiInterface.getFaq(Constant.SELECTED_LANG);
        call.enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                if(response.isSuccessful()&&response.code()==200){
                    FaqResponse FaqResponse = response.body();
                    if(FaqResponse!=null&&FaqResponse.getStatus()){
                        answerList = FaqResponse.getResponseData();
                        doLOadData(answerList);

                    }
                }
                dlg.hideDialog();
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {
                dlg.hideDialog();
            }
        });
    }

    private void doLOadData(ArrayList<FaqModel> answerList) {
        if(answerList!=null&&answerList.size()>0){
            for (int i = 0; i < answerList.size() ; i++) {
           /*     String question = answerList.get(i).getQues();
                if(question.startsWith("A")){
                    question = "A - Info ";
                }else  if(question.startsWith("B")){
                    question = "B - Info ";
                }else  if(question.startsWith("C")){
                    question = "C - Info ";
                }else  if(question.startsWith("D")){
                    question = "D - Info ";
                }else  if(question.startsWith("E")){
                    question = "E - Info ";
                }else  if(question.startsWith("F")){
                    question = "F - Info ";
                }else  if(question.startsWith("G")){
                    question = "G - Info ";
                }else  if(question.startsWith("H")){
                    question = "H - Info ";
                }else  if(question.startsWith("I")){
                    question = "I - Info ";
                }else  if(question.startsWith("J")){
                    question = "J - Info ";
                }else  if(question.startsWith("K")){
                    question = "K - Info ";
                }else  if(question.startsWith("L")){
                    question = "L - Info ";
                }else  if(question.startsWith("M")){
                    question = "M - Info ";
                }else  if(question.startsWith("N")){
                    question = "N - Info ";
                }else  if(question.startsWith("O")){
                    question = "O - Info ";
                }else  if(question.startsWith("P")){
                    question = "P - Info ";
                }else  if(question.startsWith("Q")){
                    question = "Q - Info ";
                }else  if(question.startsWith("R")){
                    question = "R - Info ";
                }else  if(question.startsWith("S")){
                    question = "S - Info ";
                }else  if(question.startsWith("T")){
                    question = "T - Info ";
                }else  if(question.startsWith("U")){
                    question = "U - Info ";
                }else  if(question.startsWith("V")){
                    question = "V - Info ";
                }else  if(question.startsWith("W")){
                    question = "W - Info ";
                }else  if(question.startsWith("X")){
                    question = "X - Info ";
                }else  if(question.startsWith("Y")){
                    question = "Y - Info ";
                }else  if(question.startsWith("Z")){
                    question = "Z - Info ";
                }*/
                QuestionModel model = new QuestionModel(answerList.get(i).getQues(),answerList);
                faqModelArrayList.add(model);
            }
            adapter = new FaqAdapter(this,faqModelArrayList);
            faq_recycler.setAdapter(adapter);
        }
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
