package com.london.gofor.insilocation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.materialdesign.widgets.Dialog;

import common.UserInfo;
import database.DataHelper;

/**
 * Created by mac on 15/5/10.
 */
public class SetPasswordActivity extends Activity {

    public static DataHelper dbHelper;
    private EditText password1, password2;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.setpassword);
        dbHelper = SplashActivity.dbHelper;
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pas1 = password1.getText().toString();
                String pas2 = password2.getText().toString();
                if (pas1 == null || pas2 == null) {
                    Dialog dialog = new Dialog(SetPasswordActivity.this, "密码设置错误", "请完整输入密码", null);
                    dialog.show();
                } else if (!pas1.equals(pas2)) {
                    Dialog dialog = new Dialog(SetPasswordActivity.this, "密码设置错误", "两次密码输入不一致", null);
                    dialog.show();
                } else {
                    UserInfo user = (UserInfo) bundle.getSerializable("user");
                    user.setPassword(pas1);
                    new setPasswordAsyncTask().execute(user);
                }

            }
        });

    }

    private class setPasswordAsyncTask extends AsyncTask {
        private setPasswordAsyncTask() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            UserInfo user = (UserInfo) objects[0];
            Integer i = dbHelper.UpdateUserInfo(user);
            if (i == null) return false;
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o instanceof Boolean) {
                Boolean flag = (Boolean) o;
                if (flag) {
                    Intent intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            super.onPostExecute(o);
        }
    }
}
