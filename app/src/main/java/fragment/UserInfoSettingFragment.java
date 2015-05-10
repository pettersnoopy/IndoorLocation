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

import com.gc.materialdesign.widgets.Dialog;
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
            final String name1 = bundle.getString("name");
            final String phone1 = bundle.getString("userId");
            final String personal1 = bundle.getString("token");
            byte[] bytes = bundle.getByteArray("bitmap");
            Drawable image = ActionManager.byteToDrawable(bytes);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity(), "名字", null, name1);
                    dialog.addCancelButton("取消");
                    dialog.show();
                    String newname = dialog.getEdit();
                    // Save new name
                }
            });

            phonenumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity(), "名字", null, phone1);
                    dialog.addCancelButton("取消");
                    dialog.show();
                }
            });

            personaltoken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity(), "名字", null, personal1);
                    dialog.addCancelButton("取消");
                    dialog.show();
                }
            });

            if (image != null) headimage.setImageDrawable(image);
            if (name1 != null) nametextview.setText(name1);
            if (phone1 != null) phonetextview.setText(phone1);
            if (personal1 != null) personaltokenview.setText(personal1);
            new SelectUserInfoSyncTask().execute(name1);
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
                nametextview.setText(userInfo.getUsername());
                phonetextview.setText(userInfo.getId());
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
