package utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.london.gofor.insilocation.R;

/**
 * Created by Administrator on 2015/5/2.
 */
public class Utils {

    /**
     * 启动系统相机
     * @param path 相机照相完成后保存的路径
     * @author Rosicky
     */
    public static Uri Cache_path = null;
    public static void camera(Activity activity, int requestCode) {
        ContentValues values = new ContentValues();
        Cache_path = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intentCamera = new Intent(MediaStore.EXTRA_OUTPUT, Cache_path);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    public static void camera(Fragment fragment, int requestCode) {
        ContentValues values = new ContentValues();
        Cache_path = fragment.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Cache_path);
        fragment.startActivityForResult(intentCamera, requestCode);
    }
    public static final int NOTIFY_ID1 = 1001;

    public static void notifyMessage(Context context, String msg, Activity activity){
        //Notification builder;
        PendingIntent contentIntent = null;
        NotificationManager nm;
        // 发送通知需要用到NotificationManager对象
        nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 消息对象
        Intent notificationIntent = new Intent(context, activity.getClass());
        // PendingIntent.getActivity(Context context, int requestCode, Intent intent, int flags)
        // 用来获得一个挂起的PendingIntent，让该Intent去启动新的Activity来处理通知
        contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);


        //定义通知栏展现的内容信息
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, msg, when);
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND; // 调用系统自带声音
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.vibrate = new long[]{300, 500};
        notification.setLatestEventInfo(context, "Inside Location Chat", msg, contentIntent);

        nm.notify(NOTIFY_ID1, notification);
    }



}
