package trs.com.tang.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import trs.com.tang.R;


public abstract class BaseFragment extends Fragment {
    protected View view;
    private ImageView ivLeft;
    private TextView tvTxt;
    private ImageView ivRight;
    private DrawerLayout drawerLayout;



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        drawerLayout = getActivity().findViewById(R.id.drawer);
        init();
        return view;
    }

    private void init() {
        ivLeft = view.findViewById(R.id.iv_left);
        tvTxt = view.findViewById(R.id.tv_txt);
        ivRight = view.findViewById(R.id.iv_right);
        ivLeft.setImageResource(R.mipmap.menu);
        tvTxt.setText(getTitle());
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
                closeIMM();
            }
        });
        initView();
        initData();
        setLeftImaig(ivLeft);
        setRightImaig(ivRight);
    }

    protected void closeIMM(){
        //获取输入法
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果打开
        if (imm.isActive()){
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getTitle();

    protected abstract void setLeftImaig(ImageView ivLeft);

    protected abstract void setRightImaig(ImageView ivRight);

}
