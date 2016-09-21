package com.ct.tool.practice;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ct.tool.utils.TextHighlighter;

/**
 * Created by Cting on 2016/8/23.
 */
public abstract class TestCtItemViewHolder<D extends TestCtItemData> {

    private static final String TAG = "cting/TestCtItemViewHolder";
    TextHighlighter mTextHighlighter;

    public abstract void init(View convertView);
    public abstract void update(D d, String queryStr);

    public TestCtItemViewHolder(View convertView) {
        mTextHighlighter = new TextHighlighter();
        init(convertView);
    }


    protected void updateHightlight(TextView textView,String highlight){
        if(TextUtils.isEmpty(highlight) || textView==null){
            return;
        }
        String originText = textView.getText().toString();
        if(TextUtils.isEmpty(originText)){
            return;
        }
        mTextHighlighter.updateHighliteInText(textView,originText,highlight);
    }

    protected void setTextStringWithHighlight(TextView textView, String value, boolean showHighLight, String queryStr){
//        Log.d(TAG, "setTextStringWithHighlight:" + textView + "," + value);
        if(textView!=null){
            textView.setText(value);
            if(showHighLight){  // refer to TestCtItemData$getQueryArea()
                updateHightlight(textView,queryStr);
            }
        }
    }

    protected void setTextStringWithHighlight(TextView textView, String value){
//        Log.d(TAG, "setTextStringWithHighlight:" + textView + "," + value);
        setTextStringWithHighlight(textView,value,false,null);
    }

}
