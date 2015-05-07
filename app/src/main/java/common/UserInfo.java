package common;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by mac on 15/5/4.
 */
public class UserInfo implements Serializable {

    public static final String ID = "_id";
    public static final String USERID = "userId";
    public static final String TOKEN = "token";
    public static final String TOKENSECRET = "tokenSecret";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "passWord";
    public static final String USERICON = "userIcon";

    private String id;
    private String userId; // 用户id
    private String token;
    private String tokenSecret;
    private String userName;
    private String passWord;
    private Drawable userIcon;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Drawable getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Drawable userIcon) {
        this.userIcon = userIcon;
    }
}
