package trs.com.tang.fragment;



import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import trs.com.tang.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannFragment extends Fragment implements QRCodeView.Delegate {

    private View view;
    private ZXingView zx;
    private EditText et_result;
    private ImageView iv_flashlight;
    boolean isColse = false;
    private TextView xiangce;
    //剪切板管理器
    private ClipboardManager cmb;


    public ScannFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scann, container, false);
        initDrawer();
        initView();
        zx.setDelegate(this);
        initZx();
        initFlash();
        getImage();
        return view;
    }

    private void initFlash() {
        iv_flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isColse){
                    zx.closeFlashlight();
                    iv_flashlight.setImageResource(R.drawable.flashlight_stop);
                    isColse = false;
                }else {
                    zx.openFlashlight();
                    iv_flashlight.setImageResource(R.drawable.flashlight_start);
                    isColse = true;
                }
            }
        });
    }

    private void initView() {
        zx = view.findViewById(R.id.zx);
        et_result = view.findViewById(R.id.et_result);
        iv_flashlight = view.findViewById(R.id.iv_flashlight);
        xiangce = view.findViewById(R.id.xiangce);
        cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    private void getImage(){
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一定要穿fragment上下文
                ISNav.getInstance().toListActivity(ScannFragment.this, config, 666);
            }
        });
    }

    // 自定义图片加载器

    // 配置选项
    private ISListConfig config = new ISListConfig.Builder()
            // 是否多选, 默认true
            .multiSelect(false)
            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
            .rememberSelected(false)
            // “确定”按钮背景色
            .btnBgColor(Color.GRAY)
            // “确定”按钮文字颜色
            .btnTextColor(Color.BLUE)
            // 使用沉浸式状态栏
            .statusBarColor(Color.parseColor("#3F51B5"))
            // 返回图标ResId
            // 标题
            .title("图片")
            // 标题文字颜色
            .titleColor(Color.WHITE)
            // TitleBar背景色
            .titleBgColor(Color.parseColor("#999999"))
            // 裁剪大小。needCrop为true的时候配置
            .needCrop(false)
            // 第一个是否显示相机，默认true
            .needCamera(false)
            // 最大选择图片数量，默认9
            .maxNum(9)
            .build();

    private String tvResult = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        zx.startSpotAndShowRect();
        // 图片选择结果回调
        if (requestCode == 666 && resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                tvResult = path;
            }
            zx.decodeQRCode(tvResult);

        }
    }


    private void initZx() {
        // 打开后置摄像头开始预览，但是并未开始识别
        zx.startCamera();
        // 显示扫描框，并开始识别
        zx.startSpotAndShowRect();
    }

    private void initDrawer() {
        ImageView iv = view.findViewById(R.id.menu);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }




    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {
        zx.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        zx.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void openChrome(String result){
        if (result.length()>10){
            if (result.substring(0,4).equals("http")){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(result);
                intent.setData(content_url);
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
            }

        }
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result != null){
            et_result.setText("");
            et_result.setText(result);
            //复制到剪切板
            cmb.setPrimaryClip(ClipData.newPlainText("二维码", result));
            openChrome(result);
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }.start();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                zx.startSpot();
            }
        }
    };

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(getContext(), "请检查相机权限", Toast.LENGTH_SHORT).show();
    }



}
