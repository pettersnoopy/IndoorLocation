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

import common.UserInfo;
import database.DataHelper;
import fragment.LoginFragment;
import holder.BaseViewHolder;

/**
 * Created by mac on 15/5/4.
 */
public class SplashActivity extends Activity {
    private final String TAG = "SplashActivity";

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userNameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                new LoginSyncTask(errorMsg, register).execute(username, password);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPage(MainPage.register);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加新用户到数据库
                String username = regname.getText().toString().trim();
                String password = regpassword.getText().toString().trim();
                new InsertDatabaseSyncTask().execute(username, password);
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

    public class InsertDatabaseSyncTask extends AsyncTask {
        public InsertDatabaseSyncTask() {
        }

        @Override
        protected void onPostExecute(Object o) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
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
                String username = objects[0].toString();
                String password = objects[1].toString();
                UserInfo user = new UserInfo();
                user.setId("1");
                user.setToken("firstOne");
                user.setTokenSecret("firstOneSceret");
                user.setUserIcon(null);
                user.setUserId("1");
                user.setUserName(username);
                user.setPassword(password);
                dbHelper.SaveUserInfo(user);
            }
            return null;
        }
    }

    public class LoginSyncTask extends AsyncTask {

        private TextView textView;
        private Button register;

        public LoginSyncTask(TextView textView, Button register) {
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
                boolean flag = dbHelper.HaveUserName(objects[0].toString(), objects[1].toString());
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
