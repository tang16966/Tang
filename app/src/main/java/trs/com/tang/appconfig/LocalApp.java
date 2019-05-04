package trs.com.tang.appconfig;

import android.os.Environment;

import java.io.File;

public class LocalApp {
    //0为初始权限，1为二级权限
    public static int STATUS = 0;

    //web的加载地址
    public static String URL = "";

    //web返回的地址
    public static String RETURN_URL = "";

    //网盘地址
    public static String CLOUD_URL = "http://www.sgxxs.cn";

    //文件路径
    public static String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Tang";
}
