package trs.com.tang;

import android.Manifest;

import android.content.Intent;

import android.os.Build;


import android.support.annotation.NonNull;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import trs.com.tang.bean.DragMenu;
import trs.com.tang.bean.UserInfo;
import trs.com.tang.fragment.CloudFragment;
import trs.com.tang.fragment.ProcessFragment;
import trs.com.tang.fragment.QRFragment;
import trs.com.tang.fragment.ServerFragment;
import trs.com.tang.fragment.TranslateFragment;
import trs.com.tang.fragment.VideoFragment;
import trs.com.tang.utils.RootUtil;


@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private ListView list_view;
    private FrameLayout content;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private UserInfo info;
    private boolean isRoot;
    private CloudFragment cloudFragment;

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
        //如果是游客登录
        if (info == null){
            info = new UserInfo();
            info.setExitPosition(0);
            info.setUserState(1);
        }
        dragMenus.add(new DragMenu(R.mipmap.translate,R.string.title_translate,new TranslateFragment()));
        dragMenus.add(new DragMenu(R.mipmap.qr_code,R.string.title_qr_code,new QRFragment()));
        dragMenus.add(new DragMenu(R.mipmap.video,R.string.title_video,new VideoFragment()));
        dragMenus.add(new DragMenu(R.mipmap.ip,R.string.title_ip,new ServerFragment()));
        dragMenus.add(new DragMenu(R.mipmap.tools,R.string.title_tools,new ProcessFragment()));
        if (info.getUserState() == 0){
            dragMenus.add(new DragMenu(R.mipmap.cloud,R.string.title_cloud,cloudFragment = new CloudFragment()));
        }
        dragMenus.add(new DragMenu(R.mipmap.esc_account,R.string.title_esc_account,null));

        isRoot = RootUtil.upgradeRootPermission(getPackageCodePath());

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
                if (!isRoot){
                    if (position == 3 || position == 4){
                        new PromptDialog(MainActivity.this).showAlertSheet(
                                "使用高级功能需要root",
                                true,
                                new PromptButton("确定", new PromptButtonListener() {
                                    @Override
                                    public void onClick(PromptButton promptButton) {

                                    }
                                }));
                        return;
                    }
                }
                fragmentManager.beginTransaction().replace(R.id.content,dragMenus.get(position).getFragment()).commit();
                info.setExitPosition(position);
                info.save();
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


    private long time;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (info.getUserState() == 0 && info.getExitPosition() == 5){
                if (cloudFragment.onBackPressed()){
                    return true;
                }
            }
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
