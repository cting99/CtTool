package com.ct.tool.practice;

import android.text.TextUtils;

/**
 * Created by Cting on 2016/8/29.
 */
public abstract class TestCtItemData {
    public abstract boolean containKeyword(String lowerCaseKeyword);

    public TestCtItemData() {

    }

    protected boolean containKeyword(String text,String keyword) {
        return !TextUtils.isEmpty(text)
                && !TextUtils.isEmpty(keyword)
                && text.toLowerCase().contains(keyword.toLowerCase());
    }

    public String[] getQueryArea(){
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
