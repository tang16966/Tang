package trs.com.tang.fragment;


import android.os.Build;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import android.view.View;

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
public class WebFragment extends BaseFragment {
    private ProgressWebView webView;

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web;
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


    @Override
    protected void initView() {
        webView = view.findViewById(R.id.web_view);
    }

    @Override
    protected void initData() {
        initWebView();
        setWebSetting();
    }

    @Override
    protected int getTitle() {
        return R.string.title_web;
    }

    @Override
    protected void setLeftImaig(ImageView ivLeft) {
        ivLeft.setImageResource(R.mipmap.left);
        ivLeft.setOnClickListener(new View.OnClickListener() {
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
    protected void setRightImaig(ImageView ivRight) {
        ivRight.setImageResource(R.mipmap.tick);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalApp.RETURN_URL = webView.getUrl();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.setWebViewClient(null);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearCache(true);
        webView.destroy();
    }
}
