package com.reformation.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.reformation.home.fragment.DividerItemDecoration;
import com.viethoa.RecyclerViewFastScroller;
import com.viethoa.models.AlphabetItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

public class FaqActivity extends AppCompatActivity implements View.OnClickListener,SearchView.OnQueryTextListener{
    private TextView topicHeader;
    private ImageView back;

    private RecyclerView faq_recycler;
    public static ArrayList<QuestionModel> faqModelArrayList;
    private ArrayList<FaqModel> answerList;
    private FaqAdapter adapter;
    ApiInterface apiInterface;
    private CustomProgresDialog dlg;
    RecyclerViewFastScroller fastScroller;
    private List<AlphabetItem> mAlphabetItems;
    ArrayList<FaqModel> ansList;
    private SearchView mSearchView;

    protected void initialiseData() {

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < faqModelArrayList.size(); i++) {
            String name = faqModelArrayList.get(i).getTitle();
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {
        faq_recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FaqAdapter(this,faqModelArrayList);
        faq_recycler.setAdapter(adapter);
        Drawable drawable = getResources().getDrawable(R.drawable.line_devider3);
        faq_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,drawable));

        fastScroller.setRecyclerView(faq_recycler);
        fastScroller.setUpAlphabet(mAlphabetItems);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        mSearchView = (SearchView) findViewById(R.id.search);
        back=(ImageView)findViewById(R.id.imageViewLeft);
        topicHeader =(TextView)findViewById(R.id.textViewHeaderTitle);
        topicHeader.setText(getResources().getString(R.string.AtoZ));
        dlg = CustomProgresDialog.getInstance(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        back.setOnClickListener(this);
        faq_recycler = (RecyclerView) findViewById(R.id.faq_recycler);
        fastScroller = (com.viethoa.RecyclerViewFastScroller) findViewById(R.id.fast_scroller);
        faqModelArrayList = new ArrayList<>();

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getResources().getString(R.string.search));

        //setData();
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // faq_recycler.setLayoutManager(layoutManager);

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
                ansList = new ArrayList<>();
                ansList.add(answerList.get(i));
                QuestionModel model = new QuestionModel(answerList.get(i).getQues(),ansList);
                faqModelArrayList.add(model);
            }
            Collections.sort(faqModelArrayList, new Comparator<QuestionModel>(){
                public int compare(QuestionModel obj1, QuestionModel obj2) {
                    // ## Ascending order
                    return obj1.getTitle().compareToIgnoreCase(obj2.getTitle()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
                }
            });
            initialiseData();
            initialiseUI();

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }


    public void refresh(ArrayList<QuestionModel> groups) {
        adapter = new FaqAdapter(this,groups);
        faq_recycler.setAdapter(adapter);
        /// faqModelArrayList=groups;
        ///  adapter.notifyDataSetChanged();
    }
}
