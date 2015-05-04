package com.london.gofor.insilocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import database.DataHelper;

/**
 * Created by mac on 15/5/4.
 */
public class LoginActivity extends Activity {

    private final String TAG = "LoginActivity";
    private DataHelper dh;
    private Button register = null;
    private EditText userNameText = null;
    private EditText passwordText = null;
    private Button login = null;
    private TextView errorMsg = null;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.login);
        initUi();
    }




        public void initUi() {
            userNameText = (EditText) findViewById(R.id.accountEt);
            passwordText = (EditText) findViewById(R.id.pwdEt);
            errorMsg = (TextView) findViewById(R.id.error);
            register = (Button) findViewById(R.id.regBtn);
            login = (Button) findViewById(R.id.subBtn);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    errorMsg.setVisibility(View.GONE);
                    String username = userNameText.getText().toString().trim();
                    String password = passwordText.getText().toString().trim();
                    new LoginSyncTask(errorMsg, register).execute(username, password);

                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }



    private Bundle wrapUserInfo(String username, String password) {
        Bundle bundle = new Bundle();
        bundle.putString("login", username);
        bundle.putString("password", password);
        return bundle;
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    textView.setText("用户不存在或密码错误");
                }
            }
            super.onPostExecute(o);
        }
    }

}
