package com.ct.tool.practice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by Cting on 2016/6/2.
 */
public abstract class TestCtAdapter<D extends TestCtItemData,V extends TestCtItemViewHolder>
        extends android.widget.BaseAdapter implements Filterable {

    protected static final String TAG = "cting/ApkInfoListAdp";

    protected abstract V newViewHolder(View convertView);
    protected abstract View newItemView();

    protected Context mContext;
    protected CtFilter mFilter;
    protected String mQueryStr;
    protected ArrayList<D> mDatas;
    protected ArrayList<D> mOriginalDatas;

    public TestCtAdapter(Context context){
        mContext = context;
        mFilter = new CtFilter();
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder;
        if (convertView == null) {
            convertView = newItemView();
            viewHolder = newViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (V) convertView.getTag();
        }
        viewHolder.update((D) getItem(position),mQueryStr);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void filter(String filterText) {
        Log.d(TAG, "fliter:" + filterText);
        mQueryStr = filterText;
        mFilter.filter(filterText);
    }

    public void setDataInfo(ArrayList<D> datas){
        mDatas = datas;
        mOriginalDatas = null;
        notifyDataSetChanged();
    }

    private class CtFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering " + constraint);
            FilterResults results = new FilterResults();

            if (mOriginalDatas == null) {
                mOriginalDatas = new ArrayList<D>(mDatas);
            }

            ArrayList<D> list = new ArrayList<>(mOriginalDatas);
            if (TextUtils.isEmpty(constraint)) {
                results.values = list;
                results.count = list.size();
            }else{
                final ArrayList<D> newList = new ArrayList<D>();
                String lowerCaseKeyword = constraint.toString().toLowerCase();
                for(D itemData:list){
                    if(itemData.containKeyword(lowerCaseKeyword)){
                        newList.add(itemData);
                    }
                }
                results.values = newList;
                results.count = newList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "publishResults " + constraint);
            mDatas = (ArrayList<D>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
