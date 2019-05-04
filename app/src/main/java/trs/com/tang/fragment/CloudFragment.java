package trs.com.tang.fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;

import java.util.regex.Pattern;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import trs.com.tang.MainActivity;
import trs.com.tang.R;
import trs.com.tang.appconfig.LocalApp;
import trs.com.tang.utils.DownLoadNotification;
import trs.com.tang.utils.FormatUtils;
import trs.com.tang.view.ProgressWebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CloudFragment extends BaseFragment {
    private ProgressWebView webView;
    private DownLoadNotification downLoadNotification;
    private Handler handler;
    private int id = 1;

    public CloudFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cloud;
    }

    @Override
    protected void initView() {
        webView = view.findViewById(R.id.web_view);
    }

    @Override
    protected void initData() {
        initWebView();
        setWebSetting();
        downLoadNotification = new DownLoadNotification(getActivity());
        handler = new Handler();
        Aria.download(this).register();
    }

    @Override
    protected int getTitle() {
        return R.string.title_cloud;
    }

    @Override
    protected void setLeftImaig(ImageView ivLeft) {

    }

    @Override
    protected void setRightImaig(ImageView ivRight) {

    }

    private void setWebSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void initWebView() {
        webView.loadUrl(LocalApp.CLOUD_URL);
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
        //下载文件
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, final String contentDisposition, String mimetype, long contentLength) {
                new PromptDialog(getActivity()).showWarnAlert("文件大小:"
                                + FormatUtils.getdecimal(contentLength / 1024f / 1024)
                                + "MB 是否下载",
                        new PromptButton("取消", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {

                            }
                        }),
                        new PromptButton("确定", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                download(url, getFileName(contentDisposition));
                            }
                        }));

            }
        });

    }

    private void download(String url, String fileName) {
        Aria.download(this)
                .load(url)
                .setFilePath(LocalApp.storePath + "/" + fileName, true)
                .start();
        notification(fileName);
    }

    @Download.onTaskRunning
    protected void running(final DownloadTask task) {
        builder.setContentText(task.getPercent() + "%      "
                + FormatUtils.getdecimal(task.getSpeed() / 1024f / 1024)
                + "m/s");
        downLoadNotification.upDataNotification(id, builder);


    }

    @Download.onTaskComplete
    void taskComplete(final DownloadTask task) {
        //在这里处理任务完成的状态
        builder.setContentTitle("下载完成").setContentText(task.getDownloadPath());
        downLoadNotification.upDataNotification(id, builder);
        Toast.makeText(getContext(), "下载完成", Toast.LENGTH_SHORT).show();
    }

    private String getFileName(String contentDisposition) {
        Pattern p = Pattern.compile("\"");
        String[] split = p.split(contentDisposition);
        String fileName = "";
        for (String a :
                split) {
            fileName = a;
        }
        return fileName;
    }

    private NotificationCompat.Builder builder;

    private void notification(String fileName) {
        Intent intent = new Intent(getActivity(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        builder = new NotificationCompat.Builder(getContext())
                .setContentTitle("正在下载：" + fileName)
                .setContentText("%")
                .setSmallIcon(R.mipmap.logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent);
        id++;
        downLoadNotification.setNotification(id, builder);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.setWebViewClient(null);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroy();
    }

    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }


}
