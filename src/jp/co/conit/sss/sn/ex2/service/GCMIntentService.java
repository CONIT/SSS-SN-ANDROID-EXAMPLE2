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

package jp.co.conit.sss.sn.ex2.service;

import static jp.co.conit.sss.sn.ex2.service.RedrawService.REGIST_FAILD_GCM;
import static jp.co.conit.sss.sn.ex2.service.RedrawService.UNREGIST_FAILD;
import static jp.co.conit.sss.sn.ex2.service.RedrawService.UNREGIST_SUCCCEEDED;
import static jp.co.conit.sss.sn.ex2.util.SNApiUtil.SENDER_ID;
import jp.co.conit.sss.sn.ex2.R;
import jp.co.conit.sss.sn.ex2.util.IntentUtil;
import jp.co.conit.sss.sn.ex2.util.PrefrerencesUtil;
import jp.co.conit.sss.sn.ex2.util.StringUtil;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * GCMからのレスポンスをハンドリングするサービスです。
 * 
 * @author conit
 */
public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onError(Context context, String errorId) {
        String registId = PrefrerencesUtil.getString(context, "registration_id", "");

        if (StringUtil.isEmpty(registId)) {
            IntentUtil.startRedrawService(context, REGIST_FAILD_GCM);
        } else {
            IntentUtil.startRedrawService(context, UNREGIST_FAILD);
        }

    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String snTicker = intent.getExtras().getString("sn_ticker");
        String snTitle = intent.getExtras().getString("sn_title");
        String snText = intent.getExtras().getString("sn_text");

        Intent serviceIntent = intent;
        serviceIntent.setClass(context, SendMessageIdIntentService.class);
        int time = (int) System.currentTimeMillis() * 1000;
        PendingIntent pendingIntent = PendingIntent.getService(context, time, serviceIntent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_status).setTicker(snTicker)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(snTitle)
                .setContentText(snText).setContentIntent(pendingIntent).build();

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(time, notification);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.d(TAG, "DeletedMessages count:" + total);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Intent intentService = new Intent();
        intentService.setClass(context, RegistService.class);
        intentService.putExtra("registration_id", registrationId);
        context.startService(intentService);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        PrefrerencesUtil.setString(getApplicationContext(), "regist_id", "");
        IntentUtil.startRedrawService(context, UNREGIST_SUCCCEEDED);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.d(TAG, "errorId:" + errorId);
        return super.onRecoverableError(context, errorId);
    }

}
