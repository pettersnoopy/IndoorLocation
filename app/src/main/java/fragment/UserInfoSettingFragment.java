package fragment;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.london.gofor.insilocation.R;
import com.london.gofor.insilocation.SplashActivity;
import com.london.gofor.insilocation.UserInfoActivity;

import org.w3c.dom.Text;

import common.ActionManager;
import common.UserInfo;
import database.DataHelper;
import holder.BaseViewHolder;

/**
 * Created by mac on 15/5/8.
 */
public class UserInfoSettingFragment extends Fragment {

    public static DataHelper dbHelper = SplashActivity.dbHelper;
    private RelativeLayout headicon, name, phonenumber, QRcode, address, gender, district, personaltoken;
    private ImageView headimage;
    private TextView nametextview, phonetextview, personaltokenview;



    class UserInfoSettingHolder  {

        private Bundle bundle;
        public UserInfoSettingHolder(Bundle bundle) {
            this.bundle = bundle;
        }

        public void initUi(View view) {
            headicon = (RelativeLayout) view.findViewById(R.id.headicon);
            name = (RelativeLayout) view.findViewById(R.id.name);
            phonenumber = (RelativeLayout) view.findViewById(R.id.phonenumber);
            QRcode = (RelativeLayout) view.findViewById(R.id.QRcode);
            address = (RelativeLayout) view.findViewById(R.id.address);
            gender = (RelativeLayout) view.findViewById(R.id.gender);
            district = (RelativeLayout) view.findViewById(R.id.district);
            personaltoken = (RelativeLayout) view.findViewById(R.id.personaltoken);
            headimage = (ImageView) view.findViewById(R.id.headimage);
            nametextview = (TextView) view.findViewById(R.id.nametextview);

            phonetextview = (TextView) view.findViewById(R.id.phonetextview);
            personaltokenview = (TextView) view.findViewById(R.id.personaltokenview);
            String name1 = bundle.getString("name");
            String phone1 = bundle.getString("userId");
            String personal1 = bundle.getString("token");
            byte[] bytes = bundle.getByteArray("bitmap");
            Drawable image = ActionManager.byteToDrawable(bytes);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = bundle.getString("name");
                    if (name == null) name = "请输入名字";
                    final Dialog dialog = new Dialog(getActivity(), "名字", null, name);
                    dialog.addCancelButton("取消");
                    View.OnClickListener onAcceptButtonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new UpdateInfoAsyncTask(dialog.getContentEditView()).execute("name", bundle.getString("userId"));
                        }
                    };
                    dialog.setOnAcceptButtonClickListener(onAcceptButtonClickListener);
                    dialog.show();
                    String newname = dialog.getEdit();
                    // Save new name
                }
            });

            phonenumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = bundle.getString("userId");
                    if (phone == null) phone = "请输入您的手机号码";
                    Dialog dialog = new Dialog(getActivity(), "电话", phone, null);
                    dialog.addCancelButton("取消");
                    dialog.show();
                }
            });

            QRcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取二维码图片
                }
            });

            personaltoken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String personal = bundle.getString("token");
                    if (personal == null) personal = "请输入个性签名";
                    final Dialog dialog = new Dialog(getActivity(), "个人签名", null, personal);
                    dialog.addCancelButton("取消");
                    View.OnClickListener onAcceptButtonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new UpdateInfoAsyncTask(dialog.getContentEditView()).execute("token", bundle.getString("userId"));
                        }
                    };
                    dialog.setOnAcceptButtonClickListener(onAcceptButtonClickListener);
                    dialog.show();
                }
            });

            if (image != null) headimage.setImageDrawable(image);
            if (name1 != null) nametextview.setText(name1);
            if (phone1 != null) phonetextview.setText(phone1);
            if (personal1 != null) personaltokenview.setText(personal1);
            new SelectUserInfoAsyncTask().execute(phone1);
        }

        public void setupView(Bundle bundle) {};

    }

    public class UpdateInfoAsyncTask extends AsyncTask {
        private EditText editView;

        public UpdateInfoAsyncTask(EditText v) {
            this.editView = v;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (objects == null) {
                return null;
            }
            if (objects[0] instanceof String) {
                String tag = objects[0].toString();
                String id = objects[1].toString();
                UserInfo userInfo = dbHelper.SelectUser(id);
                switch (tag) {
                    case "id":
                        // id--电话号码，为数据库主键，禁止修改
                        break;
                    case "name":
                        userInfo.setUsername(editView.getText().toString());
                        dbHelper.UpdateUserInfo(userInfo);
                        // 更新用户名字
                        break;
                    case "QRcode":
                        // 更新用户二维码
                        break;
                    case "possion":
                        userInfo.setPossion(editView.getText().toString());
                        dbHelper.UpdateUserInfo(userInfo);
                        // 更新用户常用地址
                        break;
                    case "gender":
                        String gender = objects[2].toString();
                        userInfo.setGender(gender);
                        dbHelper.UpdateUserInfo(userInfo);
                        // 更新用户性别
                        break;
                    case "district":
                        userInfo.setDistrict(editView.getText().toString());
                        dbHelper.UpdateUserInfo(userInfo);
                        // 更新用户所在地区
                        break;
                    case "token":
                        userInfo.setToken(editView.getText().toString());
                        dbHelper.UpdateUserInfo(userInfo);
                        // 更新用户个人签名
                        break;
                }
            }
            return null;
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

    public class SelectUserInfoAsyncTask extends AsyncTask {

        public SelectUserInfoAsyncTask() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (objects == null) {
                return null;
            } else {
                String id = objects[0].toString();
                UserInfo userInfo = UserInfoActivity.dbHelper.GetUserInfo(id);
                nametextview.setText(userInfo.getUsername());
                personaltokenview.setText(userInfo.getToken());
                headimage.setImageDrawable(ActionManager.byteToDrawable(userInfo.getUsericon()));
                return userInfo.getUsericon();
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

    UserInfoSettingHolder mHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userinfosetting, null);
        Bundle bundle = this.getArguments();
        mHolder = new UserInfoSettingHolder(bundle);
        mHolder.initUi(view);
        return view;
    }
}
