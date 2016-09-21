package com.ct.tool.base;

import android.content.Context;

import com.ct.tool.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Cting on 2016/9/1.
 */
public class CtSimpleAdapterIml {


    public static interface Columns {
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_LABEL = "label";
    }

    public static CtSimpleAdapter TypeSingleText(Context context, List<? extends Map<String, ?>> data) {
        return new CtSimpleAdapter(
                context,
                data,
                R.layout.fragment_list_simple_item,
                new String[]{Columns.COLUMN_LABEL},
                new int[]{R.id.text,});
    }

    public static CtSimpleAdapter TypeSingleTextWithIcon(Context context, List<? extends Map<String, ?>> data) {
        return new CtSimpleAdapter(
                context,
                data,
                R.layout.fragment_list_simple_single_text_with_icon,
                new String[]{Columns.COLUMN_ICON, Columns.COLUMN_LABEL},
                new int[]{R.id.icon, R.id.text,});
    }
}
