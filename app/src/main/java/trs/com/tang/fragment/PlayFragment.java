package trs.com.tang.fragment;


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import trs.com.tang.R;
import trs.com.tang.appconfig.LocalApp;
import trs.com.tang.view.ProgressWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {
    private View view;
    private String URL;
    private ProgressWebView webView;



    public PlayFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public PlayFragment(String url) {
        // Required empty public constructor
        this.URL = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_play, container, false);
        init();
        return view;
    }

    private void init() {
        initView();
        initWebView();
        setWebSetting();

    }


    private void setWebSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void initWebView() {
        webView.loadUrl(URL);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(String.valueOf(request.getUrl()));
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void initView() {
        webView = view.findViewById(R.id.web_view);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setFullScreen();
    }

    public void setFullScreen() {
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void cancelFullScreen() {
        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.setWebViewClient(null);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearCache(true);
        webView.destroy();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        cancelFullScreen();
    }




}
