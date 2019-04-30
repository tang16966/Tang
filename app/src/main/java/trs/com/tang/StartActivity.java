package trs.com.tang;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import trs.com.tang.appconfig.AppAPI;
import trs.com.tang.appconfig.LocalApp;

public class StartActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    boolean isAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initWindow();
        init();

    }



    private void init() {
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("user", 0);
        getUser();


    }

    private void initWindow() {
        //如果api大于或等于19就支持沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void getUser() {
        String name = sharedPreferences.getString("name", "");
        String pwd = sharedPreferences.getString("pwd", "");
        isAuto = sharedPreferences.getBoolean("isAuto", false);
        if (isAuto){
            login(name, pwd);
        }else {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        }


    }

    private void login(String name, String pwd) {
        String url = AppAPI.LoginAddress;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", name);
            jsonObject.put("user_pwd", pwd);

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
                            if (jsonObject.getString("result").equals("S")) {
                                startActivity(new Intent(StartActivity.this, MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));
                        finish();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
