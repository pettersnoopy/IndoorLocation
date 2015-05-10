package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import common.UserInfo;
import common.ActionManager;

/**
 * Created by mac on 15/5/4.
 */
public class DataHelper {

    // 数据库名称
    private static String DB_NAME = "insideLocationUsers.db";
    // 数据库版本
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION );
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    public UserInfo GetUserInfo(String name) {
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserInfo.USERNAME
        + "=?", new String[] {name}, null, null, null);
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex(UserInfo.ID));
            String username = cursor.getString(cursor.getColumnIndex(UserInfo.USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(UserInfo.PASSWORD));
            byte[] icon = cursor.getBlob(cursor.getColumnIndex(UserInfo.USERICON));
            String token = cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN));
            byte[] qrcode = cursor.getBlob(cursor.getColumnIndex(UserInfo.QRCode));
            String possion = cursor.getString(cursor.getColumnIndex(UserInfo.POSSION));
            String gender = cursor.getString(cursor.getColumnIndex(UserInfo.GENDER));
            String district = cursor.getString(cursor.getColumnIndex(UserInfo.DISTRICT));
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setUsername(username);
            userInfo.setPassword(password);
            userInfo.setUsericon(icon);
            userInfo.setToken(token);
            userInfo.setQrcode(qrcode);
            userInfo.setPossion(possion);
            userInfo.setGender(gender);
            userInfo.setDistrict(district);
            return userInfo;
        } else {
            return null;
        }
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public List<UserInfo> GetUserList(Boolean isSimple) {
        List<UserInfo> userList = new ArrayList<UserInfo>();
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, null , null, null,
                null, UserInfo. USERNAME + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
            UserInfo user = new UserInfo();
            if (!isSimple) {
                user.setUsername(cursor.getString(4));
                ByteArrayInputStream stream = new ByteArrayInputStream(cursor.getBlob(5));
                Drawable icon = Drawable.createFromStream(stream, "image");
                user.setUsericon(ActionManager.drawableToByte(icon));
            }
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // 判断users表中的是否包含某个UserID的记录
    public Boolean HaveUserInfo(String Id) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserInfo.ID
                + "=?", new String[]{Id}, null, null, null );
        b = cursor.moveToFirst();
        Log. e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

    //
    public Boolean HaveUserName(String UserName, String Password) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserInfo.USERNAME
                + "=? and " + UserInfo.PASSWORD + "=?", new String[]{UserName, Password}, null, null, null );
        b = cursor.moveToFirst();
        Log. e("HaveUserName", b.toString());
        cursor.close();

        return b;
    }

    public Boolean HaveUserTel(String tel, String password) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserInfo.ID
                + "=? and " + UserInfo.PASSWORD + "=?", new String[] {tel, password}, null, null, null);
        b = cursor.moveToFirst();
        cursor.close();
        return b;
    }

    // 更新users表的记录，根据UserId更新用户昵称和用户图标
    public int UpdateUserInfo(String userName, Bitmap userIcon, String Id) {
        ContentValues values = new ContentValues();
        values.put(UserInfo. USERNAME, userName);
        // BLOB类型
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        userIcon.compress(Bitmap.CompressFormat. PNG, 100, os);
        // 构造SQLite的Content对象，这里也可以使用raw
        values.put(UserInfo. USERICON, os.toByteArray());
        int id = db.update(SqliteHelper. TB_NAME, values, UserInfo.ID + "=?" , new String[]{Id});
        Log. e("UpdateUserInfo2", id + "");
        return id;
    }

    // 更新users表的记录
    public int UpdateUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put(UserInfo.ID, user.getId());
        values.put(UserInfo.USERNAME, user.getUsername());
        values.put(UserInfo.PASSWORD, user.getPassword());
        values.put(UserInfo.QRCode, user.getQrcode());
        values.put(UserInfo.POSSION, user.getPossion());
        values.put(UserInfo.GENDER, user.getGender());
        values.put(UserInfo.DISTRICT, user.getDistrict());
        values.put(UserInfo.USERICON, user.getUsericon());
        values.put(UserInfo.TOKEN, user.getToken());
        int id = db.update(SqliteHelper. TB_NAME, values, UserInfo.ID + "=?"
                , new String[] {user.getId()});
        Log. e("UpdateUserInfo", id + "success");
        return id;
    }

    // 添加users表的记录
    public Long SaveUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put(UserInfo.ID, user.getId());
        values.put(UserInfo.USERNAME, user.getUsername());
        values.put(UserInfo.PASSWORD, user.getPassword());
        values.put(UserInfo.QRCode, user.getQrcode());
        values.put(UserInfo.POSSION, user.getPossion());
        values.put(UserInfo.GENDER, user.getGender());
        values.put(UserInfo.DISTRICT, user.getDistrict());
        values.put(UserInfo.USERICON, user.getUsericon());
        values.put(UserInfo.TOKEN, user.getToken());
        Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);
        Log. e("SaveUserInfo", uid + "" + " " + user.getUsername() + " " + user.getPassword());

        return uid;
    }

    // 删除users表的记录
    public int DelUserInfo(String Id) {
        int id = db.delete(SqliteHelper. TB_NAME,
                UserInfo. ID + "=?", new String[]{Id});
        Log. e("DelUserInfo", id + "");
        return id;
    }

    public static UserInfo getUserByName(String userName,List<UserInfo> userList){
        UserInfo userInfo = null;
        int size = userList.size();
        for( int i=0;i<size;i++){
            if(userName.equals(userList.get(i).getUsername())){
                userInfo = userList.get(i);
                break;
            }
        }
        return userInfo;
    }
}
