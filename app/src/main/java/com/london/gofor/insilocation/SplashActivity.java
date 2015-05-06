package com.london.gofor.insilocation;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import fragment.LoginFragment;
import holder.BaseViewHolder;

/**
 * Created by mac on 15/5/4.
 */
public class SplashActivity extends Activity {

    private static enum MainPage {login, register};

    private ViewFlipper mFlipper;

    private Button register;

    private Button submit;

    private Button login;

    private EditText userNameText;
    private EditText passwordText;
    private TextView errorMsg;

    class LoginHolder extends BaseViewHolder {

        public LoginHolder(Context context) {
            super(context);
        }

        private LinearLayout loginPanel;
        private TextView loginTitle;
        private TextView error;
        private EditText account;
        private EditText pwd;
        private Button subBtn;
        private Button regBtn;
        private Button submit;

        @Override
        public void initUi(View view) {
            loginPanel = (LinearLayout) view.findViewById(R.id.loginPanel);
            loginTitle = (TextView) view.findViewById(R.id.loginTitle);
            error = (TextView) view.findViewById(R.id.error);
            account = (EditText) view.findViewById(R.id.accountEt);
            pwd = (EditText) view.findViewById(R.id.pwdEt);
            subBtn = (Button) view.findViewById(R.id.subBtn);
            regBtn = (Button) view.findViewById(R.id.regBtn);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoPage(MainPage.register);
                }
            });
        }

        @Override
        public void setupView(Bundle bundle) {};

    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        initUi();
        mFlipper.setDisplayedChild(MainPage.login.ordinal());

    }

    public void initUi() {
        View view = LayoutInflater.from(this).inflate(R.layout.mainpage, null);
        setContentView(view);
        userNameText = (EditText) findViewById(R.id.accountEt);
        passwordText = (EditText) findViewById(R.id.pwdEt);
        errorMsg = (TextView) findViewById(R.id.error);
        mFlipper = (ViewFlipper) view.findViewById(R.id.flipper);
        register = (Button) view.findViewById(R.id.regBtn);
        login = (Button) view.findViewById(R.id.subBtn);
        submit = (Button) view.findViewById(R.id.submit);
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
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void gotoPage(MainPage page) {
        switch (page) {
            case login:
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                mFlipper.setDisplayedChild(page.ordinal());
                break;
            case register:
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                mFlipper.setDisplayedChild(page.ordinal());
                break;
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
//            dh = InsideLocationApplication.dbHelper;
//            if (dh.HaveUserName(objects[0].toString())) {
//                return true;
//            } else {
//                return false;
//            }
            return true;
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
