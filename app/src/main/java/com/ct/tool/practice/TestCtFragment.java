package com.ct.tool.practice;


import android.app.ListFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.ct.tool.R;

import java.util.ArrayList;

public abstract class TestCtFragment<A extends TestCtAdapter,D extends TestCtItemData>
        extends ListFragment implements SearchView.OnQueryTextListener {

    public static final String TAG = "cting/Fragment";
    public static final String LINE_BREAK = "\n";
    public static final String COLON = " : ";

    protected static final int MSG_LOAD_DATA = 0;
    protected static final int MSG_EXPORT = 1;
    protected Context mContext;
    public PackageManager mPkgMgr;

    protected abstract void createAppData(ArrayList<D> datas);
    protected abstract A createAdapter();
//    public abstract

    protected SearchView mSearchView;
    protected ListView mListView;
    protected A mAdapter;
    protected ArrayList<D> mDatas=new ArrayList<D>();
    TestExportToFileUtil mExportUtil;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    cleanData();
                    createAppData(mDatas);
                    mAdapter = createAdapter();
                    mAdapter.setDataInfo(mDatas);
                    setListAdapter(mAdapter);
                    break;
                case MSG_EXPORT:
                    mExportUtil.saveToFile(getFileName(), mDatas);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    protected boolean checkLifeCycle() {
        return false;
    }

    public TestCtFragment() {
        if (checkLifeCycle()) Log.i(TAG, "CtSimpleFragment");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(checkLifeCycle())   Log.i(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(checkLifeCycle())   Log.i(TAG, "onViewCreated");

        mContext = getActivity();
        mPkgMgr = mContext.getPackageManager();
        mExportUtil = new TestExportToFileUtil(mContext);

        setHasOptionsMenu(true);

        mListView = getListView();
        mListView.setTextFilterEnabled(true);

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    protected String getFileName() {
        String name = getActivity().getTitle().toString();
        return "applist_" + name + ".txt";
    }

    protected void cleanData() {
        if (mDatas != null) {
            mDatas.clear();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, "onQueryTextSubmit,query:" + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange,query:" + newText);
        if (mAdapter != null) {
            mAdapter.filter(newText);
        }
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(checkLifeCycle())   Log.i(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.apk_info_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export:
                mHandler.sendEmptyMessage(MSG_EXPORT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(checkLifeCycle())   Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkLifeCycle())   Log.i(TAG, "onCreate");
    }


    @Override
    public void onStart() {
        super.onStart();
        if(checkLifeCycle())   Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkLifeCycle())   Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(checkLifeCycle())   Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(checkLifeCycle())   Log.i(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(checkLifeCycle())   Log.i(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(checkLifeCycle())   Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(checkLifeCycle())   Log.i(TAG, "onDestroy");
    }

}
