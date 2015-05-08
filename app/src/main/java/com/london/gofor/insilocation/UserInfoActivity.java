package com.london.gofor.insilocation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import android.provider.MediaStore;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;

import ImageCache.CacheableImageView;
import common.ActionManager;
import common.UserInfo;
import database.DataHelper;
import fragment.UserInfoSettingFragment;
import holder.BaseViewHolder;

/**
 * Created by Administrator on 2015/4/29.
 */
public class UserInfoActivity extends FragmentActivity {

    public static DataHelper dbHelper;
    private ViewFlipper mFlipper;
    private RelativeLayout goInfoSetting;
    private CacheableImageView mCacheableImageView;
    private TextView username;
    private TextView userId;
    private PopupWindow popWindow;

    private String Uid = null;
    private UserInfoHolder mUserInfoHolder;
    private UserInfoSettingFragment mUserInfoSetting;
    private LinearLayout infopre;

    private String Uname = null;
    private String UserId = null;
    private Drawable drawable = null;
    private String personaltoken = null;
    private Bundle toFragmentBundle = new Bundle();


    private static final int Request_photo_select = 1;

    private static final int Request_Camera_Upload = 0;

    private static final int Request_resize_image = 2;

    private static final String IMAGE_FILE_NAME = "UserHeader.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        dbHelper = SplashActivity.dbHelper;
        infopre = (LinearLayout) findViewById(R.id.infopre);
        infopre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlipper.showPrevious();
            }
        });
        mFlipper = (ViewFlipper) findViewById(R.id.userflipper);
        username = (TextView) findViewById(R.id.user_name);
        goInfoSetting = (RelativeLayout) findViewById(R.id.gotosetting);
        goInfoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlipper.showNext();
            }
        });
        username.setText(SplashActivity.getUname());
        userId = (TextView) findViewById(R.id.user_id);
        userId.setText(SplashActivity.getUserId());
        Uname = username.getText().toString();
        UserId = userId.getText().toString();
        Uid = userId.getText().toString();
        toFragmentBundle.putString("name", SplashActivity.getUname());
        toFragmentBundle.putString("userId", SplashActivity.getUserId());
        mCacheableImageView = (CacheableImageView) findViewById(R.id.user_icon);
        if (mUserInfoSetting == null) {
            mUserInfoSetting = new UserInfoSettingFragment();
            mUserInfoSetting.setArguments(toFragmentBundle);
        }
        new SelectUserInfoSyncTask().execute(Uname);
        mCacheableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "HasClicked!!");
                String[] string1 = new String[]{getString(R.string.select_from_photo), getString(R.string.take_photo)};
                String[] string2 = new String[]{"Select from photo", "Take photo"};
                popWindow = initPopWindow(string1, new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.EXTERNAL_CONTENT_URI);
                            Log.d("TAG", username.getText().toString());
                            Log.d("TAG", userId.getText().toString());
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.usersettingframelayout, mUserInfoSetting).commit();

    }

    public void initUI() {
        View view = LayoutInflater.from(this).inflate(R.layout.user, null);
        setContentView(view);
        mUserInfoHolder = new UserInfoHolder(this);
        mUserInfoHolder.initUi(view);
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

    public class UpdateUserInfoSyncTask extends AsyncTask {
        public UpdateUserInfoSyncTask() {
            super();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (objects == null) {
                return false;
            } else {
                String username = objects[0].toString();
                String password = objects[1].toString();
                Drawable icon = (Drawable) objects[2];
                UserInfo user = new UserInfo();
//                user.setId("1");
//                user.setToken("firstOne");
//                user.setTokenSecret("firstOneSceret");
                user.setUserIcon(icon);
//                user.setUserId("1");
                user.setUserName(username);
//                user.setPassword(password);
                dbHelper.UpdateUserInfo(user);
                return true;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    public class SelectUserInfoSyncTask extends AsyncTask {

        public SelectUserInfoSyncTask() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (objects == null) {
                return null;
            } else {
                String username = objects[0].toString();
                UserInfo userInfo = dbHelper.GetUserInfo(username);
                return userInfo.getUserIcon();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            mCacheableImageView.setImageDrawable((Drawable) o);
            super.onPostExecute(o);
        }
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
            String username = extras.getString("username");
            String userId = extras.getString("userId");
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            new UpdateUserInfoSyncTask().execute(Uname, Uid, drawable);
            mCacheableImageView.setImageDrawable(drawable);
        }
    }

    private PopupWindow initPopWindow(String[] arrays, OnItemClickListener listener) {
        BaseAdapter adapter = new ArrayAdapter<String>(UserInfoActivity.this, R.layout.simple_setting_items, arrays);
        return initPopWindow(adapter, listener);
    }

    private PopupWindow initPopWindow(BaseAdapter adapter, OnItemClickListener listener) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.list_internate_update, null);
        final PopupWindow popwindow = new PopupWindow(contentView);
        Log.d("popwindow", "has created!");
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        dismissPopupWindow();
        //if(popupWindow != null)
    }

    //判断PopupWindow是不是存在，存在就把它dismiss掉
    private void dismissPopupWindow() {

        if(popWindow != null){
            popWindow.dismiss();
            popWindow = null;
        }
    }

    class UserInfoHolder extends BaseViewHolder {
        public UserInfoHolder(Context context) {
            super(context);
        }
        private CacheableImageView mCacheableImageView;

        private TextView username;

        private TextView userId;

        private PopupWindow popWindow;

        @Override
        public void initUi(View view) {
            username = (TextView) findViewById(R.id.user_name);
            username.setText(SplashActivity.getUname());
            userId = (TextView) findViewById(R.id.user_id);
            userId.setText(SplashActivity.getUserId());
            mCacheableImageView = (CacheableImageView) view.findViewById(R.id.user_icon);
            new SelectUserInfoSyncTask().execute(Uname);
            mCacheableImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Click", "HasClicked!!");
                    String[] string1 = new String[] {getString(R.string.select_from_photo), getString(R.string.take_photo)};
                    String[] string2 = new String[] {"Select from photo", "Take photo"};
                    popWindow = initPopWindow(string1, new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.EXTERNAL_CONTENT_URI);
                                Log.d("TAG", username.getText().toString());
                                Log.d("TAG", userId.getText().toString());
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
        public void setupView(Bundle bundle) {};

        public void updateValue() {

        }
    }
}
