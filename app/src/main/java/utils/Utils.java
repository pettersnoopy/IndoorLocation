package utils;

import android.app.Activity;
import android.content.ContentValues;
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
//
//    public static void uploadFile (final Activity context, String path, OnDoingTaskListener listener) {
//        if (FileUtil.isUploadable(path)) {
//            Bundle bundle = new Bundle();
//            bundle.putString(ConstValues.PATH, path);
//            bundle.putLong(ConstValues.FOLDER_ID, context.getFolderID());
//            context.showLoading();
//            ActionManager.getInstance().startAction(context, ActionManager.ACTION_UPLOAD, bundle, listener);
//        } else {
//            Utils.showToast(context, context.getString(R.string.invalidfile_cannotupload));
//        }
//    }
//
//    public static void uploadUserPic (final BaseActivity context, String path, OnDoingTaskListener listener) {
//        if (FileUtil.isUploadable(path)) {
//            Bundle bundle = new Bundle();
//            bundle.putString(ConstValues.PATH, path);
//            bundle.putLong(ConstValues.FOLDER_ID, context.getFolderID());
//            context.showLoading();
//            ActionManager.getInstance().startAction(context, ActionManager.ACTION_UPLOAD_PIC, bundle, listener);
//        } else {
//            Utils.showToast(context, context.getString(R.string.invalidfile_cannotupload));
//        }
//    }



}
