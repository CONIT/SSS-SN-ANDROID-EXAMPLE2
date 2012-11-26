/*
 * Copyright (C) 2012 CONIT Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.conit.sss.sn.ex2.activity;

import jp.co.conit.sss.sn.ex2.R;
import jp.co.conit.sss.sn.ex2.fragment.UserDataFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * SamuraiNotificationのプッシュ通知に含まれているuserdata情報を表示する機能を提供するActivityです。
 * 
 * @author conit
 */
public class UserDataActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userdata);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String option = bundle.getString("option");

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            UserDataFragment f = (UserDataFragment) fm.findFragmentById(R.id.frame_userdata);
            if (f == null) {
                UserDataFragment fragment = UserDataFragment.newInstance(option);
                transaction.replace(R.id.frame_userdata, fragment);
                transaction.commit();
            }
        }
    }

}
