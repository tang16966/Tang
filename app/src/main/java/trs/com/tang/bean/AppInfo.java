package trs.com.tang.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable icon;
    private String appName;
    private String processName;
    private boolean select;

    public AppInfo() {
    }

    public AppInfo(Drawable icon, String appName, String processName, boolean select) {
        this.icon = icon;
        this.appName = appName;
        this.processName = processName;
        this.select = select;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
