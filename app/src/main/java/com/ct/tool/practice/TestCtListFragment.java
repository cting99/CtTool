package com.ct.tool.practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ct.tool.R;
import com.ct.tool.utils.LineBuilder;

import java.util.ArrayList;

/**
 * Created by Cting on 2016/8/30.
 */
public class TestCtListFragment extends TestCtFragment<TestCtListFragment.CtAdapter,TestCtListFragment.CtData> {

    @Override
    protected void createAppData(ArrayList<CtData> datas) {
        datas.add(new CtData("test secColumn"));
    }

    protected void addItem(String item) {
        mDatas.add(new CtData(item));
    }

    @Override
    protected CtAdapter createAdapter() {
        return new CtAdapter(getActivity());
    }


    public class CtAdapter extends TestCtAdapter<CtData, CtViewHolder> {

        public CtAdapter(Context context) {
            super(context);
        }

        @Override
        protected CtViewHolder newViewHolder(View convertView) {
            return new CtViewHolder(convertView);
        }

        @Override
        protected View newItemView() {
            return LayoutInflater.from(mContext).inflate(R.layout.fragment_list_simple_item,null);
        }
    }

    public class CtData extends TestCtItemData {
        String line;

        public CtData(String line) {
            this.line = line;
        }

        public CtData(int labelId, String value) {
            this(getString(labelId), value);
        }

        public CtData(String label, String value) {
            line = getActivity().getString(R.string.text_line_format_label_value, label, value);
        }

        @Override
        public boolean containKeyword(String lowerCaseKeyword) {
            return containKeyword(line,lowerCaseKeyword);
        }

        @Override
        public String toString() {
            return line;
        }
    }

    public class CtViewHolder extends TestCtItemViewHolder<CtData> {

        TextView lineText;
        public CtViewHolder(View convertView) {
            super(convertView);
        }

        @Override

        public void init(View convertView) {
            lineText = (TextView) convertView.findViewById(R.id.text);
        }

        @Override
        public void update(CtData simpleItemData, String queryStr) {
            setTextStringWithHighlight(lineText, simpleItemData.line, true, queryStr);
        }

    }

}
