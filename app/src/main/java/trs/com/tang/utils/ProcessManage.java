package trs.com.tang.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import trs.com.tang.bean.AppInfo;

import static android.content.Context.USAGE_STATS_SERVICE;
import static java.util.stream.Collectors.toCollection;

public class ProcessManage {

    private PackageManager packageManager;
    private List<UsageStats> appList;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProcessManage(Context context) {
        this.context = context;
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 1000, time);
        packageManager = context.getPackageManager();

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public List<AppInfo> getAppInfo() {
//        List<AppInfo> appInfos = new ArrayList<>();
//        PackageInfo packageInfo;
//        for (UsageStats us : appList) {
//            try {
//                if (isAppRunning(context, us.getPackageName())) {
//                    packageInfo = packageManager.getPackageInfo(us.getPackageName(), 0);
//                    if (isUserApp(packageInfo)) {
//                        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//                        appInfos.add(new AppInfo(
//                                applicationInfo.loadIcon(packageManager),
//                                (String) applicationInfo.loadLabel(packageManager),
//                                applicationInfo.packageName,
//                                false
//                        ));
//                    }
//                }
//
//
//            } catch (PackageManager.NameNotFoundException e) {
//                Log.d("Process:", "getAppInfo: 程序找不到");
//            }
//        }
//        return appInfos;
//
//    }

    public static void removeProcess(String processName) {
        RootUtil.execShell("kill -9 $(pidof " + processName + ")");
    }


    public List<AppInfo> getAppInfo(){
        List<AppInfo> appInfos = new ArrayList<>();
        PackageInfo packageInfo;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo rs : am.getRunningServices(Integer.MAX_VALUE)){
            try {
                packageInfo = packageManager.getPackageInfo(rs.process, 0);
                if (isUserApp(packageInfo)) {
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    appInfos.add(new AppInfo(
                            applicationInfo.loadIcon(packageManager),
                            (String) applicationInfo.loadLabel(packageManager),
                            applicationInfo.packageName,
                            false
                    ));
                }

            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        //还要去重

        return appInfos;
    }



    /**
     * 是否是系统软件或者是系统软件的更新软件
     *
     * @return
     */
    public boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }


}
