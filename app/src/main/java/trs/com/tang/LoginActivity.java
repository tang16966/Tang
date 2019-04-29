package trs.com.tang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;
import trs.com.tang.appconfig.AppAPI;
import trs.com.tang.appconfig.LocalApp;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_title;
    private Button button;
    private EditText et_name,et_pwd;
    private CheckBox cb_save,cb_auto;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWindow();
        initView();
        init();

    }

    private void init() {
        sharedPreferences = getSharedPreferences("user",0);
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        setDialog();
        setButton();
        getUser();

    }

    private void setDialog() {
        promptDialog = new PromptDialog(this);
    }

    private void setButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
                promptDialog.showLoading("正在登录");
                login(sharedPreferences.getString("name",""),
                        sharedPreferences.getString("pwd",""));
            }
        });
    }

    private void getUser(){
        String name = sharedPreferences.getString("name","");
        String pwd = sharedPreferences.getString("pwd","");
        boolean isSave = sharedPreferences.getBoolean("isSave",false);
        boolean isAuto = sharedPreferences.getBoolean("isAuto",false);
        if (isSave){
            et_name.setText(name);
            et_pwd.setText(pwd);
            cb_save.setChecked(isSave);
            cb_auto.setChecked(isAuto);
        }
    }

    //登录
    private void login(String name,String pwd){
        String url = AppAPI.LoginAddress;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name",name);
            jsonObject.put("user_pwd",pwd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("result").equals("S")){
                                LocalApp.STATUS = jsonObject.getInt("status");
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                promptDialog.dismiss();
                                finish();
                            }else {
                                promptDialog.showError("账号或密码错误");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        promptDialog.showError("网络错误");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    //保存密码
    private void saveUser(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",et_name.getText().toString().trim());
        editor.putString("pwd",et_pwd.getText().toString().trim());
        editor.putBoolean("isSave",cb_save.isChecked());
        editor.putBoolean("isAuto",cb_auto.isChecked());
        editor.commit();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        button = findViewById(R.id.button);
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        cb_save = findViewById(R.id.cb_save);
        cb_auto = findViewById(R.id.cb_auto);
    }

    private void initWindow() {
        //如果api大于或等于19就支持沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


}
