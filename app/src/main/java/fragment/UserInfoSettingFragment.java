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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.london.gofor.insilocation.R;
import com.london.gofor.insilocation.UserInfoActivity;

import org.w3c.dom.Text;

import common.ActionManager;
import common.UserInfo;
import holder.BaseViewHolder;

/**
 * Created by mac on 15/5/8.
 */
public class UserInfoSettingFragment extends Fragment {

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
            String name = bundle.getString("name");
            String phone = bundle.getString("userId");
            String personal = bundle.getString("token");
            byte[] bytes = bundle.getByteArray("bitmap");
            Drawable image = ActionManager.byteToDrawable(bytes);
            if (image != null) headimage.setImageDrawable(image);
            if (name != null) nametextview.setText(name);
            if (phone != null) phonetextview.setText(phone);
            if (personal != null) personaltokenview.setText(personal);
            new SelectUserInfoSyncTask().execute(name);
        }

        public void setupView(Bundle bundle) {};

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
                UserInfo userInfo = UserInfoActivity.dbHelper.GetUserInfo(username);
                nametextview.setText(userInfo.getUserName());
                phonetextview.setText(userInfo.getUserId());
                personaltokenview.setText(userInfo.getToken());
                headimage.setImageDrawable(userInfo.getUserIcon());
                return userInfo.getUserIcon();
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
