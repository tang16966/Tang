package trs.com.tang.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import trs.com.tang.R;
import trs.com.tang.appconfig.AppAPI;
import trs.com.tang.bean.Translate;
import trs.com.tang.utils.MD5Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslateFragment extends Fragment {
    private View view;
    private TextView tv_clean,tv_massage,tv_result,tv_network,tv_network_rs;
    private EditText et_input;
    private Button bt_translate;
    private RequestQueue requestQueue;


    public TranslateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_translate, container, false);
        initDrawer();
        initView();
        init();
        return view;
    }

    private void init() {
        requestQueue = Volley.newRequestQueue(getContext());
        setClean();
        setButton();
    }

    private void setButton() {
        bt_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_input.getText().toString().equals("")){
                    tv_massage.setText("翻译中...");
                    //获取输入法
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //如果打开
                    if (imm.isActive()){
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    requestData(getUrl());
                }
            }
        });
    }

    private void setClean() {
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_input.getText().toString().equals("")){
                    tv_clean.setText("");
                }else {
                    tv_clean.setText(R.string.clean);
                }
            }
        });
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("");
            }
        });
    }

    private void initView() {
        tv_clean = view.findViewById(R.id.tv_clean);
        tv_massage = view.findViewById(R.id.tv_massage);
        et_input = view.findViewById(R.id.et_input);
        bt_translate = view.findViewById(R.id.bt_translate);
        tv_result = view.findViewById(R.id.tv_result);
        tv_network = view.findViewById(R.id.tv_network);
        tv_network_rs = view.findViewById(R.id.tv_network_rs);
    }


    private String getUrl(){
        String url = "";
        String appKey ="2311424c849c3b08";
        String query = et_input.getText().toString();
        String salt = String.valueOf(System.currentTimeMillis());
        String from = "zh-CHS";
        String to = "EN";
        String sign = MD5Util.md5(appKey + query + salt+ "mBG3W0ZfNZFjwoKOrl4e4jT0uyQ6ZW5A");
        return url+"?appKey="+appKey+"&q="+query+"&salt="+salt+"&from="+from+"&to="+to+"&sign="+sign;
    }

    private void requestData(String data){
        String url = AppAPI.WangYi;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        tv_massage.setText("");
                        parsingJson(jsonObject);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        tv_massage.setText("网络错误");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    //将字符数组转化为字符串
    private String getString(List<String> data){
        String str = "";
        for (String a : data){
            str += (a + "  ");
        }
        return str;
    }
    //将网络句子换行
    private String getBeanString(List<Translate.WebBean> data){
        String str = "";
        for (Translate.WebBean web : data){
            str += (web.getKey()+":\n");
            for (String s : web.getValue()){
                str += ("      "+s+"\n");
            }
            str += "\n";
        }
        return str;
    }


    private Translate translate;
    private Gson gson = new Gson();
    private void parsingJson(JSONObject jsonObject){
        try {
            translate = gson.fromJson(jsonObject.toString(),Translate.class);
            tv_result.setText("结果："+getString(translate.getTranslation()));
            if (translate.getWeb().size()>0){
                tv_network.setText("网络释义：");
                tv_network_rs.setText(getBeanString(translate.getWeb()));

            }else {
                tv_network.setText("");

            }


        }catch (Exception e){

        }

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
