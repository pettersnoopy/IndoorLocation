package common;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by mac on 15/5/4.
 */
public class UserInfo implements Serializable {

    public static final String ID = "_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "passWord";
    public static final String QRCode = "qrcode";
    public static final String POSSION = "possion";
    public static final String GENDER = "gender";
    public static final String DISTRICT = "district";
    public static final String USERICON = "usericon";
    public static final String TOKEN = "token";

    private String id;  // 用户id
    private String token;
    private String username;
    private byte[] qrcode;
    private String possion;
    private String gender;
    private String district;
    private String password;
    private byte[] usericon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getQrcode() {
        return qrcode;
    }

    public void setQrcode(byte[] qrcode) {
        this.qrcode = qrcode;
    }

    public String getPossion() {
        return possion;
    }

    public void setPossion(String possion) {
        this.possion = possion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getUsericon() {
        return usericon;
    }

    public void setUsericon(byte[] usericon) {
        this.usericon = usericon;
    }
}
