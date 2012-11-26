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

package jp.co.conit.sss.sn.ex2.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.co.conit.sss.sn.ex2.entitiy.SNParam;
import jp.co.conit.sss.sn.ex2.entitiy.SNServerResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * SamuraiNotificationのAPIの実行するユーティリティクラスです。
 * 
 * @author conit
 */
public final class SNApiUtil {

    public static final String SENDER_ID = "103726821175";

    public static final String SN_TOKEN = "059ba1f42301a910d08b57328afbfa208e2b8172";

    private static final String SN_DOMAIN = "https://test-sss-api.conit.jp/v2/android/";

    private static final String SN_DEVICES = "devices/";

    private static final String SN_UNREGISTER = "unregister/";

    private static final String SN_MESSAGES = "messages/";

    private SNApiUtil() {

    }

    /**
     * SamuraiNotificationのdevicesAPIを使用します。
     * 
     * @param snParam
     * @return
     */
    public static SNServerResult devices(SNParam snParam) {

        SNServerResult result = new SNServerResult();
        String url = SN_DOMAIN + SN_DEVICES;

        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        if (!StringUtil.isEmpty(snParam.getLang())) {
            postData.add(new BasicNameValuePair("lang", snParam.getLang()));
        }
        if (!StringUtil.isEmpty(snParam.getTags())) {
            postData.add(new BasicNameValuePair("tags", snParam.getTags()));
        }
        if (!StringUtil.isEmpty(snParam.getMid())) {
            postData.add(new BasicNameValuePair("mid", snParam.getMid()));
        }

        postData.add(new BasicNameValuePair("token", snParam.getToken()));
        postData.add(new BasicNameValuePair("device_token", snParam.getDeviceToken()));

        return post(url, postData, result);
    }

    /**
     * SamuraiNotificationのunregisterAPIを使用します。
     * 
     * @param devicetoken
     * @return
     */
    public static SNServerResult unregister(String devicetoken) {

        SNServerResult result = new SNServerResult();
        String url = SN_DOMAIN + SN_UNREGISTER;

        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("token", SN_TOKEN));
        postData.add(new BasicNameValuePair("device_token", devicetoken));

        return post(url, postData, result);
    }

    /**
     * SamuraiNotificationのmessagesAPIを使用し、プッシュ通知の履歴を取得します。<br>
     * 取得した履歴は新着順にソートされた形式で取得されます。
     * 
     * @param tag
     * @return
     */
    public static SNServerResult messages(String tag) {

        SNServerResult result = new SNServerResult();

        StringBuilder sb = new StringBuilder();
        sb.append(SN_DOMAIN);
        sb.append(SN_MESSAGES);
        sb.append("?token=");
        sb.append(SN_TOKEN);
        sb.append("&lang=");
        Locale locale = Locale.getDefault();
        sb.append(locale.toString());
        if (!StringUtil.isEmpty(tag)) {
            sb.append("&tags=");
            sb.append(tag);
        }
        return get(sb.toString(), result);
    }

    /**
     * GET処理を実行します。<br>
     * Exceptionが発生した場合はスタックトレースを表示します。<br>
     * HTTPリクエストがSUCCESSではない場合は、SNServerResultに設定して返却します。
     * 
     * @param url
     * @param result
     * @return
     */
    private static SNServerResult get(String url, SNServerResult result) {

        try {
Log.d("SN", "GET url:"+url);
            HttpClient httpCli = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpCli.execute(get);
            int status = response.getStatusLine().getStatusCode();
            result.mHttpStatus = status;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBodyText = EntityUtils.toString(entity);
                entity.consumeContent();
                httpCli.getConnectionManager().shutdown();
                result.mResponseString = responseBodyText;
Log.d("SN", "GET response:"+responseBodyText);                
            }
        } catch (Exception e) {
            result.mCauseException = e;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * POST処理を実行します。<br>
     * Exceptionが発生した場合はスタックトレースを表示します。<br>
     * HTTPリクエストがSUCCESSではない場合は、SNServerResultに設定して返却します。
     * 
     * @param url
     * @param postData
     * @param result
     * @return
     */
    private static SNServerResult post(String url, List<NameValuePair> postData,
            SNServerResult result) {
Log.d("SN", "POST url:"+url);
for (NameValuePair nvp:postData) {
Log.d("SN", "POST NameValuePair:"+"KEY:"+nvp.getName()+"   VALUE:"+nvp.getValue());    
}

        try {
            HttpClient httpCli = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(postData, "utf-8"));

            HttpResponse response = httpCli.execute(post);
            int status = response.getStatusLine().getStatusCode();
            result.mHttpStatus = status;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBodyText = EntityUtils.toString(entity);
                entity.consumeContent();
                httpCli.getConnectionManager().shutdown();
                result.mResponseString = responseBodyText;
Log.d("SN", "POST response:"+responseBodyText);                
            }
        } catch (Exception e) {
            result.mCauseException = e;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SamuraiNotificationのdevicesAPIで使用するパラメータを生成します。
     * 
     * @param type
     * @return
     */
    public static SNParam generateSNPraram() {
        SNParam snParam = new SNParam();
        snParam.setToken(SN_TOKEN);
        return snParam;
    }

}
