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

package jp.co.conit.sss.sn.ex2.entitiy;

/**
 * プッシュ通知のモデルクラスです。
 * 
 * @author conit
 */
public class PushMessage {

    private String mMid;

    private String mTicker;

    private String mTitle;

    private String mText;

    private String mUserData;

    private String mDateStr;

    public PushMessage() {
    }

    public String getUserData() {
        return mUserData;
    }

    public void setUserData(String userData) {
        this.mUserData = userData;
    }

    public String getMid() {
        return mMid;
    }

    public void setMid(String mid) {
        this.mMid = mid;
    }

    public String getTicker() {
        return mTicker;
    }

    public void setTicker(String ticker) {
        this.mTicker = ticker;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDateStr() {
        return mDateStr;
    }

    public void setDateStr(String dateStr) {
        this.mDateStr = dateStr;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    @Override
    public String toString() {
        return "PushMessage [mMid=" + mMid + ", mTicker=" + mTicker + ", mTitle=" + mTitle
                + ", mText=" + mText + "]";
    }

}
