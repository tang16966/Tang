package trs.com.tang.bean;

import org.litepal.crud.LitePalSupport;

public class UserInfo extends LitePalSupport {
    private String userName;
    private int userState;
    private int exitPosition;

    public UserInfo() {
    }

    public UserInfo(String userName, int userState, int exitPosition) {
        this.userName = userName;
        this.userState = userState;
        this.exitPosition = exitPosition;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
