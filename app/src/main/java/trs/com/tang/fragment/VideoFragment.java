package trs.com.tang.fragment;


import android.content.ClipboardManager;
import android.content.Context;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import trs.com.tang.R;
import trs.com.tang.appconfig.LocalApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment {
    private EditText et_txt;
    private Spinner sp_interface;
    private Button bt_parsing;
    private String[] data;
    private GridView gridView;
    private List<String[]> gridData;
    private ClipboardManager cmb;
    private PlayFragment playFragment;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        et_txt.setText(LocalApp.RETURN_URL);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        et_txt = view.findViewById(R.id.et_txt);
        sp_interface = view.findViewById(R.id.sp_interface);
        bt_parsing = view.findViewById(R.id.bt_parsing);
        gridView = view.findViewById(R.id.grid_view);
    }

    @Override
    protected void initData() {
        cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        initSpinnerData();
        setParsing();
        setGrid();
    }

    @Override
    protected int getTitle() {
        return R.string.title_video;
    }

    @Override
    protected void setLeftImaig(ImageView ivLeft) {

    }

    @Override
    protected void setRightImaig(ImageView ivRight) {

    }

    private void setGrid() {
        gridData = new ArrayList<>();
        gridData.add(new String[]{"爱奇艺", "https://www.iqiyi.com"});
        gridData.add(new String[]{"优酷", "https://www.youku.com"});
        gridData.add(new String[]{"芒果TV", "https://www.mgtv.com"});
        gridData.add(new String[]{"腾讯视频", "https://v.qq.com"});
        gridData.add(new String[]{"音乐Tai", "http://www.yinyuetai.com"});


        gridView.setAdapter(new GridAdapter());
    }

    private void setParsing() {
        bt_parsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFragment = new PlayFragment(getUrl());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, playFragment).addToBackStack(null).commit();
            }
        });

    }

    private void initSpinnerData() {
        data = new String[]{
                "https://jx.618g.com/?url="
        };
        List<String> list = new ArrayList<>();
        for (int i = 1; i < data.length + 1; i++) {
            list.add("接口" + i);
        }
        sp_interface.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list));
    }


    private String getUrl() {
        return data[sp_interface.getSelectedItemPosition()] + et_txt.getText();
    }


    private WebFragment webFragment = new WebFragment();

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gridData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.item_video, null);
            Button button = view.findViewById(R.id.btn);
            button.setText(gridData.get(position)[0]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalApp.URL = gridData.get(position)[1];
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, webFragment).addToBackStack(null).commit();
                }
            });
            return view;
        }
    }

}
