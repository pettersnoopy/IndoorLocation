package com.london.gofor.insilocation;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import fragment.LoginFragment;

/**
 * Created by mac on 15/5/4.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.mainpage);
        LoginFragment login = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, login);
        transaction.commit();

    }

}
