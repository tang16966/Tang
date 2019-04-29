package trs.com.tang;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

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
    private List<Fragment> fragments;


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
        fragments = new ArrayList<>();
        fragments.add(new ServerFragment());
        fragments.add(new QRFragment());
        fragments.add(new VideoFragment());
        fragments.add(new TranslateFragment());
        fragmentManager.beginTransaction().replace(R.id.content,fragments.get(3)).commit();
    }

    private void initData() {
        icons = new int[]{
                R.mipmap.ip,
                R.mipmap.qr_code,
                R.mipmap.video,
                R.mipmap.translate,
                R.mipmap.esc_account
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
                if (position == 4){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return;
                }
                fragmentManager.beginTransaction().replace(R.id.content,fragments.get(position)).commit();

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

    private long time;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (fragmentManager.getBackStackEntryCount()>0){
                fragmentManager.popBackStack();
            }else {
                if (System.currentTimeMillis() - time > 1500){
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    time = System.currentTimeMillis();
                }else {
                    finish();
                }
            }
        }
        return true;
    }
}
