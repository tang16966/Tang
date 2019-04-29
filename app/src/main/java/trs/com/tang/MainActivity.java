package trs.com.tang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import trs.com.tang.fragment.QRFragment;
import trs.com.tang.fragment.ServerFragment;
import trs.com.tang.fragment.TranslateFragment;
import trs.com.tang.fragment.VideoFragment;


@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private ListView list_view;
    private FrameLayout content;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;

    private int[] icons;
    private int[] titles;

    private QRFragment qrFragment;
    private TranslateFragment translateFragment;
    private ServerFragment serverFragment;
    private VideoFragment videoFragment;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        initWindow();
        init();
    }

    private void initPermission() {
        MainActivityPermissionsDispatcher.getPerWithPermissionCheck(this);
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        drawer = findViewById(R.id.drawer);
        content = findViewById(R.id.content);
        list_view = findViewById(R.id.list_view);
        initData();
        initFragment();
        initListView();
        initPermission();

    }



    private void initFragment() {
        qrFragment = new QRFragment();
        translateFragment = new TranslateFragment();
        serverFragment = new ServerFragment();
        videoFragment = new VideoFragment();
        fragmentManager.beginTransaction().replace(R.id.content,translateFragment).commit();
    }

    private void initData() {
        icons = new int[]{
                R.drawable.ip,
                R.drawable.qr_code,
                R.drawable.video,
                R.drawable.translate,
                R.drawable.esc_account
        };
        titles = new int[]{
                R.string.title_ip,
                R.string.title_qr_code,
                R.string.title_video,
                R.string.title_translate,
                R.string.title_esc_account
        };
    }

    private void initListView() {
        list_view.setAdapter(new MyAdapter());
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        fragmentManager.beginTransaction().replace(R.id.content,serverFragment).commit();
                        break;
                    case 1:
                        fragmentManager.beginTransaction().replace(R.id.content,qrFragment).commit();
                        break;
                    case 2:
                        fragmentManager.beginTransaction().replace(R.id.content,videoFragment).commit();
                        break;
                    case 3:
                        fragmentManager.beginTransaction().replace(R.id.content,translateFragment).commit();
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                        break;
                }
                //获取输入法
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //如果打开
                if (imm.isActive()){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                drawer.closeDrawer(Gravity.START);
            }
        });
    }

    private void initWindow() {
        //如果api大于或等于19就支持沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getPer() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return icons.length;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this,R.layout.item_main_left,null);
            ImageView image = view.findViewById(R.id.image);
            TextView txt = view.findViewById(R.id.txt);
            image.setImageResource(icons[position]);
            txt.setText(titles[position]);
            return view;
        }
    }

}
