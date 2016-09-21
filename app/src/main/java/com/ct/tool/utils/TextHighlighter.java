/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.ct.tool.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Highlights the text in a text field.
 */
public class TextHighlighter {
    private final String TAG = TextHighlighter.class.getSimpleName();
    private final static boolean DEBUG = false;

    private CharacterStyle mTextStyleSpan;
    private ForegroundColorSpan mColorSpan;

    public TextHighlighter() {
        mTextStyleSpan = new StyleSpan(Typeface.BOLD);
        mColorSpan = new ForegroundColorSpan(Color.RED);
    }

    public static boolean containKeyword(String text,String keyword) {
        return !TextUtils.isEmpty(text)
                && !TextUtils.isEmpty(keyword)
                && text.toLowerCase().contains(keyword.toLowerCase());
    }

    public void updateHighliteInText(TextView view, String text, String keyword) {
        if(TextUtils.isEmpty(keyword)||TextUtils.isEmpty(text) || view==null){
            return;
        }
        view.setText(applyHighlight(text,keyword));
    }

    public SpannableString applyHighlight(String text, String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(mTextStyleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(mColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    public SpannableString applyHighlights(String text, String[] keyword) {
        SpannableString s = new SpannableString(text);
        for (int i = 0; i < keyword.length; i++) {
            applyHighlight(text, keyword[i]);
        }
        return s;
    }


}
