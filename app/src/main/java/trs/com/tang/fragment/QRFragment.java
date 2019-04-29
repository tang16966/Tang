package trs.com.tang.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import trs.com.tang.R;
import trs.com.tang.utils.ImgUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRFragment extends BaseFragment {

    private EditText et_txt;
    private Button button;
    private ImageView image;
    private Bitmap bitmap;

    public QRFragment() {
        // Required empty public constructor
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qr;
    }

    @Override
    protected void initView() {
        et_txt = view.findViewById(R.id.et_txt);
        button = view.findViewById(R.id.button);
        image = view.findViewById(R.id.image);
        bitmap = null;
    }

    @Override
    protected void initData() {
        initClick();
    }

    @Override
    protected int getTitle() {
        return R.string.title_qr_code;
    }

    @Override
    protected void setLeftImaig(ImageView ivLeft) {
    }

    @Override
    protected void setRightImaig(ImageView ivRight) {
        ivRight.setImageResource(R.mipmap.scann_qr);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ScannFragment())
                        .addToBackStack(null).commit();

            }
        });
    }

    private void initClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_txt.getText().toString().equals("")) {
                    image.setImageBitmap(bitmap = getBitmap(null));
                }
                //获取输入法
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //如果打开
                if (imm.isActive()){
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //生产bitmap
                if (!et_txt.getText().toString().equals("")) {
                    Bitmap logo_bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo, null);
                    image.setImageBitmap(bitmap = getBitmap(logo_bitmap));
                }
                return true;
            }
        });


        //保存图片
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveImage();
                return true;
            }
        });
    }

    private void saveImage() {
        if (bitmap != null) {
            if (ImgUtils.saveImageToGallery(getContext(), bitmap)){
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //生产二维码
    private Bitmap getBitmap(Bitmap logo) {
        if (logo == null) {
            return QRCodeEncoder.syncEncodeQRCode(et_txt.getText().toString().trim(), 500);
        } else {
            return QRCodeEncoder.syncEncodeQRCode(et_txt.getText().toString().trim(),
                    500, Color.BLACK, logo);
        }

    }


}
