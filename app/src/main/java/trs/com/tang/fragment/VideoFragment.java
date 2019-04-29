package trs.com.tang.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
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
public class VideoFragment extends Fragment {
    private View view;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        initDrawer();
        initView();
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        et_txt.setText(LocalApp.RETURN_URL);
    }

    private void init() {
        cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        initSpinnerData();
        setParsing();
        setGrid();
    }

    private void setGrid() {
        gridData = new ArrayList<>();
        gridData.add(new String[]{"爱奇艺","https://www.iqiyi.com"});
        gridData.add(new String[]{"优酷","https://www.youku.com"});
        gridData.add(new String[]{"芒果TV","https://www.mgtv.com"});
        gridData.add(new String[]{"腾讯视频","https://v.qq.com"});
        gridData.add(new String[]{"音乐Tai","http://www.yinyuetai.com"});


        gridView.setAdapter(new GridAdapter());
    }

    private void setParsing() {
        bt_parsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cmb.setPrimaryClip(ClipData.newPlainText("二维码", getUrl()));
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                Uri content_url = Uri.parse(getUrl());
//                intent.setData(content_url);
//                startActivity(Intent.createChooser(intent, "请选择浏览器"));
                playFragment = new PlayFragment(getUrl());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content,playFragment).addToBackStack(null).commit();
            }
        });

    }

    private void initSpinnerData() {
        data = new String[]{
            "http://69p.top/?url=",
                "http://74t.top/?url=",
                "http://mimijiexi.top/?url=",
                "http://55jx.top/?url=",
                "http://playx.top/?url=",
                "http://nitian9.com/?url=",
                "http://19g.top/?url=",
                "http://607p.com/?url=",
                "http://52088.online/?url=",
                "http://bofang.online/?url=",
                "http://play1.online/?url=",
                "http://ckplay.online/?url=",
                "http://api.baiyug.vip/?url=",
                "http://880kan.com/?url=",
                "http://59uv.com/?url="
        };
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            list.add("接口"+i);
        }
        sp_interface.setAdapter(new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list));
    }

    private void initView() {
        et_txt = view.findViewById(R.id.et_txt);
        sp_interface = view.findViewById(R.id.sp_interface);
        bt_parsing = view.findViewById(R.id.bt_parsing);
        gridView = view.findViewById(R.id.grid_view);
    }

    private String getUrl(){
        return data[sp_interface.getSelectedItemPosition()]+et_txt.getText();
    }

    private void initDrawer() {
        ImageView iv = view.findViewById(R.id.menu);
        final DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private WebFragment webFragment = new WebFragment();
    private class GridAdapter extends BaseAdapter{

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
            View view = View.inflate(getContext(),R.layout.item_video,null);
            Button button = view.findViewById(R.id.btn);
            button.setText(gridData.get(position)[0]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalApp.URL = gridData.get(position)[1];
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content,webFragment).addToBackStack(null).commit();
                }
            });
            return view;
        }
    }

}
