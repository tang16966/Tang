package trs.com.tang.bean;

import android.support.v4.app.Fragment;

public class DragMenu {
    private int icon;
    private int title;
    private Fragment fragment;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public DragMenu(int icon, int title, Fragment fragment) {
        this.icon = icon;
        this.title = title;
        this.fragment = fragment;
    }

    public DragMenu() {
    }
}
