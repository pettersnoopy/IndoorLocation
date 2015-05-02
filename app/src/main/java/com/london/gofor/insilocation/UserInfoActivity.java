package com.london.gofor.insilocation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView;
import android.provider.MediaStore;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.io.File;

import ImageCache.CacheableImageView;
import utils.Utils;

/**
 * Created by Administrator on 2015/4/29.
 */
public class UserInfoActivity extends Activity {

    private CacheableImageView mCacheableImageView;

    private static final int Request_photo_select = 1;

    private static final int Request_Camera_Upload = 0;

    private static final int Request_resize_image = 2;

    private static final String IMAGE_FILE_NAME = "UserHeader.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
        mCacheableImageView = (CacheableImageView) findViewById(R.id.user_icon);
//        mCacheableImageView.setImageDrawable(R.drawable.usericon);
        mCacheableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "HasClicked!!");
                String[] string1 = new String[] {getString(R.string.select_from_photo), getString(R.string.take_photo)};
                String[] string2 = new String[] {"Select from photo", "Take photo"};
                PopupWindow popWindow = initPopWindow(string1, new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.EXTERNAL_CONTENT_URI);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(intent, Request_photo_select);
                        } else {
                            if (isSdcardExisting()) {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                                cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                startActivityForResult(cameraIntent, Request_Camera_Upload);
                            }
                        }
                    }
                });
                popWindow.showAsDropDown(mCacheableImageView);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case Request_photo_select:
                    resizeImage(data.getData());
                    break;
                case Request_Camera_Upload:
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        Toast.makeText(UserInfoActivity.this, "未找到存储卡，请插入sd卡！",
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                case Request_resize_image:
                    if (data != null)
                        getImageToView(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public Uri getImageUri() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME));
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Request_resize_image);
    }

    public void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            mCacheableImageView.setImageDrawable(drawable);
        }
    }

    private PopupWindow initPopWindow(String[] arrays, OnItemClickListener listener) {
        BaseAdapter adapter = new ArrayAdapter<String>(UserInfoActivity.this, R.layout.simple_setting_items, arrays);
        return initPopWindow(adapter, listener);
    }

    private PopupWindow initPopWindow(BaseAdapter adapter, OnItemClickListener listener) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.list_internate_update, null);
//      final PopupWindow popwindow = new PopupWindow(getLayoutInflater().inflate(R.layout.list_internate_update, null),
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        final PopupWindow popwindow = new PopupWindow(contentView);

        View view = this.getLayoutInflater().inflate(R.layout.list_internate_update, null);
        final PopupWindow popwindow = new PopupWindow(contentView);
        Log.d("popwindow", "has created!");

//        popwindow.setWidth((int) getResources().getDimension(R.dimen.picture_popwindow_width));
        popwindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popwindow.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        ListView listview = (ListView) contentView.findViewById(R.id.list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(listener);
        popwindow.setFocusable(true);
//        popwindow.setOutsideTouchable(true);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popwindow != null && popwindow.isShowing()) {
                    popwindow.dismiss();
                }
                return false;
            }
        });
        return popwindow;
    }
}
