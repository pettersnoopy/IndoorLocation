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
    public static final String USERICON = "userIcon";
    public static final String PASSWORD = "password";

    private String id;
    private String userId;
    private String token;
    private String tokenSecret;
    private String UserName;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Password;
    private Drawable userIcon;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setTokenSecret(String tokenSecret) { this.tokenSecret = tokenSecret; }

    public String getTokenSecret() { return this.tokenSecret; }

    public void setUserName(String userName) { this.UserName = userName; }

    public String getUserName() { return this.UserName; }

    public void setUserIcon(Drawable drawable) { this.userIcon = drawable; }

    public Drawable getUserIcon() {
        return this.userIcon;
    }
}
