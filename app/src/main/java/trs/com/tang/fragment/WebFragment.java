package trs.com.tang.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import trs.com.tang.R;
import trs.com.tang.appconfig.LocalApp;
import trs.com.tang.view.ProgressWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {
    private View view;
    private ProgressWebView webView;
    private ImageView tick;

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web, container, false);
        init();
        return view;
    }

    private void init() {
        initView();
        initDrawer();
        initWebView();
        setWebSetting();
        setTick();
    }

    private void setTick() {
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalApp.RETURN_URL = webView.getUrl();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setWebSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void initWebView() {
        webView.loadUrl(LocalApp.URL);
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
        tick = view.findViewById(R.id.tick);
    }

    private void initDrawer() {
        ImageView iv = view.findViewById(R.id.menu);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()){
                    webView.goBack();
                }else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
    }
}
