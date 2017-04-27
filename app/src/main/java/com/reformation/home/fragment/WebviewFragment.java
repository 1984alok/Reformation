package com.reformation.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.reformation.home.R;

/**
 * Created by IMFCORP\alok.acharya on 27/4/17.
 */

public class WebviewFragment extends Fragment{

    WebView myWebView;
    final static String myBlogAddr = "http://android-er.blogspot.com";
    String myUrl;
    private ImageView leftImg;
    private TextView headerTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        myWebView = (WebView)view.findViewById(R.id.mywebview);
        headerTitle =(TextView)view.findViewById(R.id.textViewHeaderTitle);
        leftImg=(ImageView)view.findViewById(R.id.imageViewLeft);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());

        if(myUrl == null){
            myUrl = myBlogAddr;
        }
        myWebView.loadUrl(myUrl);

        leftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.
            }
        });

        return view;

    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            myUrl = url;
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

}
