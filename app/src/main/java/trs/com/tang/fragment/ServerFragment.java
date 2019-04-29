package trs.com.tang.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

import trs.com.tang.R;
import trs.com.tang.utils.IpUtil;

import static trs.com.tang.utils.RootUtil.execShell;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {
    private View view;
    private TextView tv_ip;
    private ImageView iv_wifi;
    private Switch sw_btn;
    private SharedPreferences sharedPreferences;

    public ServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_server, container, false);
        initDrawer();
        initView();
        init();
        return view;
    }

    private void init() {
        setIP();
        setWifi();
        setADB();
    }

    private void setADB() {
        sw_btn.setChecked(getData());
        sw_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveDate(isChecked);
                execShell("setprop service.adb.tcp.port 5555");
                if (!isChecked){
                    execShell("stop adbd");
                    setIP();
                }else {
                    execShell("start adbd");
                    setIP();
                }
            }
        });
    }

    private void setWifi() {
        iv_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    private void setIP() {
        String ip = IpUtil.getIpAddressString();
        if (getData()){
            tv_ip.setText("adb connect "+IpUtil.getIpAddressString()+":5555");
        }else {
            if (IpUtil.isIPv4Address(ip)){
                tv_ip.setText("本机IP:"+ip);
            }else {
                tv_ip.setText("本机未分配到IP地址");
            }
        }


    }

    private void initView() {
        tv_ip = view.findViewById(R.id.tv_ip);
        iv_wifi = view.findViewById(R.id.iv_wifi);
        sw_btn = view.findViewById(R.id.sw_btn);
        sharedPreferences = getActivity().getSharedPreferences("wifi",0);
    }

    private void saveDate(boolean isClose){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isClose",sw_btn.isChecked());
        editor.commit();
    }
    private boolean getData(){
        return sharedPreferences.getBoolean("isClose",false);
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



}
