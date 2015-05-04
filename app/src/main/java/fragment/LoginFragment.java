package fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.london.gofor.insilocation.MainActivity;
import com.london.gofor.insilocation.R;
import com.london.gofor.insilocation.InsideLocationApplication;

import org.w3c.dom.Text;

import database.DataHelper;

/**
 * Created by mac on 15/5/4.
 */
public class LoginFragment extends Fragment {

    private final String TAG = "LoginActivity";
    private DataHelper dh;

    protected class LoginViewHolder {
        private Button register = null;
        private EditText userNameText = null;
        private EditText passwordText = null;
        private Button login = null;
        private TextView errorMsg = null;

        public void initUi(View view) {
            userNameText = (EditText) view.findViewById(R.id.accountEt);
            passwordText = (EditText) view.findViewById(R.id.pwdEt);
            errorMsg = (TextView) view.findViewById(R.id.error);
            register = (Button) view.findViewById(R.id.regBtn);
            login = (Button) view.findViewById(R.id.subBtn);

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
                    
                }
            });
        }

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
            dh = InsideLocationApplication.dbHelper;
            if (dh.HaveUserName(objects[0].toString())) {
                return true;
            } else {
                return false;
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
                    Context context = getActivity();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else {
                    textView.setText("用户不存在或密码错误");
                }
            }
            super.onPostExecute(o);
        }
    }

}
