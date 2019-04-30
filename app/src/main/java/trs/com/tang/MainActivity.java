package trs.com.tang;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;

import android.os.Build;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import trs.com.tang.bean.DragMenu;
import trs.com.tang.bean.UserInfo;
import trs.com.tang.fragment.ProcessFragment;
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
    private UserInfo info;

    private List<DragMenu> dragMenus;
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;



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
        initListView();
        initPermission();

    }



    private void initData() {
        dragMenus = new ArrayList<>();
        List<UserInfo> all = LitePal.findAll(UserInfo.class);
        for (UserInfo a : all){
            info = a;
        }
        if (info == null){
            info = new UserInfo();
            info.setExitPosition(0);
            info.setUserState(1);
        }
        dragMenus.add(new DragMenu(R.mipmap.ip,R.string.title_ip,new ServerFragment()));
        dragMenus.add(new DragMenu(R.mipmap.qr_code,R.string.title_qr_code,new QRFragment()));
        dragMenus.add(new DragMenu(R.mipmap.video,R.string.title_video,new VideoFragment()));
        dragMenus.add(new DragMenu(R.mipmap.translate,R.string.title_translate,new TranslateFragment()));
        if (info.getUserState() == 0){
            dragMenus.add(new DragMenu(R.mipmap.tools,R.string.title_tools,new ProcessFragment()));
        }
        dragMenus.add(new DragMenu(R.mipmap.esc_account,R.string.title_esc_account,null));

    }

    private void initListView() {
        list_view.setAdapter(new MyAdapter());
        fragmentManager.beginTransaction().replace(R.id.content,dragMenus.get(info.getExitPosition()).getFragment()).commit();
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == dragMenus.size()-1){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return;
                }
                info.setExitPosition(position);
                info.save();
                fragmentManager.beginTransaction().replace(R.id.content,dragMenus.get(position).getFragment()).commit();
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
            return dragMenus.size();
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
            image.setImageResource(dragMenus.get(position).getIcon());
            txt.setText(dragMenus.get(position).getTitle());
            return view;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
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
                    return false;
                }else {
                    System.exit(0);
                }
            }
        }
        return true;
    }
}
