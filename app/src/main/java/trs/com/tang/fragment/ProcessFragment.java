package trs.com.tang.fragment;


import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import trs.com.tang.R;
import trs.com.tang.bean.AppInfo;
import trs.com.tang.utils.ProcessManage;
import trs.com.tang.view.CircleRefreshLayout;
import trs.com.tang.view.DragItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessFragment extends BaseFragment {

    private ListView listView;
    private List<AppInfo> appInfos;
    private MyAdapter myAdapter;
    private ProcessManage processManage;
    private Handler handler;

    private CircleRefreshLayout refresh;


    public ProcessFragment() {
        // Required empty public constructor
    }





    @Override
    protected int getLayoutId() {
        return R.layout.fragment_process;
    }

    @Override
    protected void initView() {
        listView = view.findViewById(R.id.list_view);
        refresh = view.findViewById(R.id.refresh);
    }


    @Override
    protected void initData() {
        handler = new Handler();
        start();
        refresh.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {

            }

            @Override
            public void refreshing() {
                new Thread(){

                    @Override
                    public void run() {
                        processManage = new ProcessManage(getContext());
                        appInfos = processManage.getProcess();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter = new MyAdapter();
                                listView.setAdapter(myAdapter);
                                refresh.finishRefreshing();
                            }
                        },2000);
                        super.run();
                    }
                }.start();
            }
        });

    }

    @Override
    protected int getTitle() {
        return R.string.title_tools;
    }

    @Override
    protected void setLeftImaig(ImageView ivLeft) {

    }

    @Override
    protected void setRightImaig(ImageView ivRight) {
        ivRight.setImageResource(R.mipmap.clean);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanApp();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size();
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
            View view = View.inflate(getContext(), R.layout.item_appinfo, null);
            CheckBox cbSelect;
            ImageView ivIcon;
            TextView tvAppName;
            TextView tvProcessName;

            cbSelect = view.findViewById(R.id.cb_select);
            ivIcon = view.findViewById(R.id.iv_icon);
            tvAppName = view.findViewById(R.id.tv_app_name);
            tvProcessName = view.findViewById(R.id.tv_process_name);
            DragItem dragItem = view.findViewById(R.id.drag_item);

            final AppInfo appInfo = appInfos.get(position);

            dragItem.setOnDragDelect(new DragItem.OnDragDelect() {
                @Override
                public void delete() {
                    ProcessManage.removeProcess(appInfo.getProcessName());
                    appInfos.remove(position);
                    notifyDataSetChanged();
                }
            });
            cbSelect.setChecked(appInfo.isSelect());
            ivIcon.setImageDrawable(appInfo.getIcon());
            tvAppName.setText(appInfo.getAppName());
            tvProcessName.setText(appInfo.getProcessName());

            cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    appInfos.get(position).setSelect(isChecked);
                }
            });

            return view;
        }
    }

    private void start(){
        final PromptDialog promptDialog = new PromptDialog(getActivity());
        promptDialog.showLoading("正在加载");
        new Thread(){
            @Override
            public void run() {
                processManage = new ProcessManage(getContext());
                appInfos = processManage.getProcess();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter = new MyAdapter();
                        listView.setAdapter(myAdapter);
                        promptDialog.dismiss();
                    }
                },500);
                super.run();
            }
        }.start();
    }



    private void cleanApp(){
        for (int i = 0; i < appInfos.size(); i++) {
            AppInfo a = appInfos.get(i);
            if (a.isSelect()){
                ProcessManage.removeProcess(a.getProcessName());
                appInfos.remove(i);
                i--;
            }
        }
        myAdapter.notifyDataSetChanged();
    }

}























