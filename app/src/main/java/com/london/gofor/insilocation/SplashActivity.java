package com.london.gofor.insilocation;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import common.ActionManager;
import common.UserInfo;
import database.DataHelper;
import fragment.LoginFragment;
import holder.BaseViewHolder;

/**
 * Created by mac on 15/5/4.
 */
public class SplashActivity extends Activity {
    private final String TAG = "SplashActivity";

    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "7479b1c66c0d";

    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "fcfaf1a5df51d9fe4e6121056f607341";

    private Button getSecuritycode;

    private static String UserId;

    public static String getUserId() {return UserId;}

    public static DataHelper dbHelper;

    private static enum MainPage {login, register};

    private ViewFlipper mFlipper;

    private Button register;

    private Button submit;

    private Button login;

    private Button loginback;

    private EditText userNameText;
    private EditText passwordText;
    private TextView errorMsg;

    private EditText regname;
    private EditText regpassword;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        initUi();
        mFlipper.setDisplayedChild(MainPage.login.ordinal());

    }

    public void initUi() {
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        dbHelper = new DataHelper(this);
        View view = LayoutInflater.from(this).inflate(R.layout.mainpage, null);
        setContentView(view);
        userNameText = (EditText) findViewById(R.id.accountEt);
        passwordText = (EditText) findViewById(R.id.pwdEt);
        errorMsg = (TextView) findViewById(R.id.error);
        mFlipper = (ViewFlipper) view.findViewById(R.id.flipper);
        register = (Button) view.findViewById(R.id.regBtn);
        login = (Button) view.findViewById(R.id.subBtn);
        submit = (Button) view.findViewById(R.id.submit);
        loginback = (Button) view.findViewById(R.id.loginback);
        regname = (EditText) view.findViewById(R.id.regname);
        regpassword = (EditText) view.findViewById(R.id.regpassword);
        getSecuritycode = (Button) view.findViewById(R.id.getSecuritycode);
        getSecuritycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

                            // 提交用户信息
//                            registerUser(country, phone);
                        }
                    }
                });
                registerPage.show(SplashActivity.this);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userNameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                new LoginSyncTask(userNameText, errorMsg, register).execute(id, password);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                gotoPage(MainPage.register);
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            new InsertDatabaseAsyncTask().execute(phone);
                        }
                    }
                });
                registerPage.show(SplashActivity.this);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加新用户到数据库
                String username = regname.getText().toString().trim();
                String password = regpassword.getText().toString().trim();
                new InsertDatabaseAsyncTask().execute(username, password);
            }
        });
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(MainPage.login);
            }
        });

    }

    public void gotoPage(MainPage page) {
        switch (page) {
            case login:
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));

                mFlipper.setDisplayedChild(page.ordinal());
//                mFlipper.showPrevious();
                break;
            case register:

                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                mFlipper.setDisplayedChild(page.ordinal());
//                mFlipper.showNext();
                break;

        }
    }

    public class InsertDatabaseAsyncTask extends AsyncTask {
        public InsertDatabaseAsyncTask() {
        }

        @Override
        protected void onPostExecute(Object o) {
            Intent intent = new Intent(SplashActivity.this, SetPasswordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", (UserInfo) o);
            intent.putExtras(bundle);
            startActivity(intent);
            super.onPostExecute(o);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (objects == null) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                String userId = objects[0].toString();
                UserInfo user = new UserInfo();
                user.setId(userId);
                user.setUsername("");
                user.setPassword("");
                Drawable image = getResources().getDrawable(R.drawable.a3u);
                byte[] icon = ActionManager.drawableToByte(image);
                user.setUsericon(icon);
                dbHelper.SaveUserInfo(user);
                UserId = userId;
                return user;
            }
        }
    }

    public class LoginSyncTask extends AsyncTask {

        private EditText username;
        private TextView textView;
        private Button register;

        public LoginSyncTask(EditText username, TextView textView, Button register) {
            this.username = username;
            this.textView = textView;
            this.register = register;
        }



        @Override
        protected Boolean doInBackground(Object[] objects) {
            if (objects == null) {
                Toast.makeText(getApplicationContext(), "请输入账号密码", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Log.d("TAG", "已输入");
                UserId = objects[0].toString();
                boolean flag = dbHelper.HaveUserTel(objects[0].toString(), objects[1].toString());
                Log.d("FLAG", flag + "");
                if (flag) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o == null) {
                // login exception
                return;
            }
            if (o instanceof Boolean) {
                boolean flag = (Boolean) o;
                if (flag) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    textView.setText("用户不存在或密码错误");
                }
            }
            super.onPostExecute(o);
        }
    }


}
