package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import common.UserInfo;

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

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public List<UserInfo> GetUserList(Boolean isSimple) {
        List<UserInfo> userList = new ArrayList<UserInfo>();
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, null , null, null,
                null, UserInfo. ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
            UserInfo user = new UserInfo();
            user.setId(cursor.getString(0));
            user.setUserId(cursor.getString(1));
            user.setToken(cursor.getString(2));
            user.setTokenSecret(cursor.getString(3));
            if (!isSimple) {
                user.setUserName(cursor.getString(4));
                ByteArrayInputStream stream = new ByteArrayInputStream(cursor.getBlob(5));
                Drawable icon = Drawable.createFromStream(stream, "image");
                user.setUserIcon(icon);
            }
            userList.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userList;
    }

    // 判断users表中的是否包含某个UserID的记录
    public Boolean HaveUserInfo(String UserId) {
        Boolean b = false;
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserInfo.USERID
                + "=?", new String[]{UserId}, null, null, null );
        b = cursor.moveToFirst();
        Log. e("HaveUserInfo", b.toString());
        cursor.close();
        return b;
    }

    //
    public Boolean HaveUserName(String UserName, String Password) {
        Boolean b = false;
        Log.d("UserName", UserName);
        Log.d("Password", Password);
        Cursor cursor = db.query(SqliteHelper. TB_NAME, null, UserInfo.USERNAME
                + "=? and " + UserInfo.PASSWORD + "=?", new String[]{UserName, Password}, null, null, null );
        b = cursor.moveToFirst();
        Log. e("HaveUserName", b.toString());
        cursor.close();

        return b;
    }

    // 更新users表的记录，根据UserId更新用户昵称和用户图标
    public int UpdateUserInfo(String userName, Bitmap userIcon, String UserId) {
        ContentValues values = new ContentValues();
        values.put(UserInfo. USERNAME, userName);
        // BLOB类型
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        userIcon.compress(Bitmap.CompressFormat. PNG, 100, os);
        // 构造SQLite的Content对象，这里也可以使用raw
        values.put(UserInfo. USERICON, os.toByteArray());
        int id = db.update(SqliteHelper. TB_NAME, values, UserInfo.USERID + "=?" , new String[]{UserId});
        Log. e("UpdateUserInfo2", id + "");
        return id;
    }

    // 更新users表的记录
    public int UpdateUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put(UserInfo. USERID, user.getUserId());
        values.put(UserInfo. TOKEN, user.getToken());
        values.put(UserInfo. TOKENSECRET, user.getTokenSecret());
        int id = db.update(SqliteHelper. TB_NAME, values, UserInfo.USERID + "="
                + user.getUserId(), null);
        Log. e("UpdateUserInfo", id + "");
        return id;
    }

    // 添加users表的记录
    public Long SaveUserInfo(UserInfo user) {
        ContentValues values = new ContentValues();
        values.put(UserInfo. USERID, user.getUserId());
        values.put(UserInfo. TOKEN, user.getToken());
        values.put(UserInfo. TOKENSECRET, user.getTokenSecret());
        values.put(UserInfo. USERNAME, user.getUserName());
        values.put(UserInfo. PASSWORD, user.getPassword());
        values.put(UserInfo. USERICON, drawableToByte(user.getUserIcon()));
        Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);
        Log. e("SaveUserInfo", uid + "" + " " + user.getUserName() + " " + user.getPassword());

        return uid;
    }
    public  synchronized  byte[] drawableToByte(Drawable drawable) {

        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }
        return null;
    }

    // 添加users表的记录
    public Long SaveUserInfo(UserInfo user, byte[] icon) {
        ContentValues values = new ContentValues();
        values.put(UserInfo. USERID, user.getUserId());
        values.put(UserInfo. USERNAME, user.getUserName());
        values.put(UserInfo. TOKEN, user.getToken());
        values.put(UserInfo. TOKENSECRET, user.getTokenSecret());
        if(icon!= null){
            values.put(UserInfo. USERICON, icon);
        }
        Long uid = db.insert(SqliteHelper. TB_NAME, UserInfo.ID, values);
        Log. e("SaveUserInfo", uid + "");
        return uid;
    }

    // 删除users表的记录
    public int DelUserInfo(String UserId) {
        int id = db.delete(SqliteHelper. TB_NAME,
                UserInfo. USERID + "=?", new String[]{UserId});
        Log. e("DelUserInfo", id + "");
        return id;
    }

    public static UserInfo getUserByName(String userName,List<UserInfo> userList){
        UserInfo userInfo = null;
        int size = userList.size();
        for( int i=0;i<size;i++){
            if(userName.equals(userList.get(i).getUserName())){
                userInfo = userList.get(i);
                break;
            }
        }
        return userInfo;
    }
}
