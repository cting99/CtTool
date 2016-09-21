package com.ct.tool.base;


import android.app.ListFragment;
import android.content.ContentResolver;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ct.tool.R;
import com.ct.tool.utils.FileHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CtSimpleFragment extends ListFragment implements SearchView.OnQueryTextListener {

    public static final String TAG = "cting/Fragment";

    protected static final int MSG_LOAD_DATA_FINISHED = 0;
    protected static final int MSG_EXPORT_FINISHED = 1;

    protected Context mContext;
    public PackageManager mPkgMgr;
    public ContentResolver mResolver;

    protected SearchView mSearchView;
    protected ListView mListView;
    protected CtSimpleAdapter mAdapter;
    protected ArrayList<Map<String, Object>> mDatas = new ArrayList<Map<String, Object>>();
    FileHelper mFileHelper;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA_FINISHED:
                    onDataLoadFinish();
                    break;
                case MSG_EXPORT_FINISHED:
                    String result = (String) msg.obj;
                    onExportFinsied(result);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    Runnable mLoadDataRunnable = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    Runnable mExportFileRunnable = new Runnable() {
        @Override
        public void run() {
            exportFile();
        }
    };

    public CtSimpleFragment() {
        if (checkLifeCycle()) Log.i(TAG, "CtSimpleFragment");
    }

    protected boolean checkLifeCycle() {
        return false;
    }

    protected void createDataMap() {
        addMap("Simple list fragment,support search & export");
    }

    protected CtSimpleAdapter createAdapter() {
        return CtSimpleAdapterIml.TypeSingleText(mContext, mDatas);
    }

    protected void addMap(String label) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CtSimpleAdapterIml.Columns.COLUMN_LABEL, label);
        mDatas.add(map);
    }

    protected String getFileName() {
        String name = getActivity().getTitle().toString();
        return "list_" + name + ".txt";
    }

    protected void cleanData() {
        if (mDatas != null) {
            mDatas.clear();
        }
    }

    //load data
    private void startLoadData() {
        new Thread(mLoadDataRunnable).start();
    }

    private void loadData() {
        cleanData();
        Log.i(TAG, "loadData start<" + getFileName());
        createDataMap();
        Log.i(TAG, "loadData end  >");
        mHandler.removeMessages(MSG_LOAD_DATA_FINISHED);
        mHandler.sendEmptyMessage(MSG_LOAD_DATA_FINISHED);
    }

    private void onDataLoadFinish() {
        Log.i(TAG, "set adapter start<");
        mAdapter = createAdapter();
        setListAdapter(mAdapter);
        Log.i(TAG, "set adapter end  >");
    }

    //export file
    private void startExport() {
        new Thread(mExportFileRunnable).start();
    }

    private void exportFile() {
        Log.i(TAG, "export start<");
        String result = mFileHelper.exportToFile(getFileName(), mDatas);
        Log.i(TAG, "export end  >");
        mHandler.removeMessages(MSG_EXPORT_FINISHED);
        mHandler.obtainMessage(MSG_EXPORT_FINISHED, result).sendToTarget();
    }

    private void onExportFinsied(String result) {
        Log.i(TAG, "export finish:" + result);
        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (checkLifeCycle()) Log.i(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (checkLifeCycle()) Log.i(TAG, "onViewCreated");

        mContext = getActivity();
        mPkgMgr = mContext.getPackageManager();
        mResolver = mContext.getContentResolver();
        mFileHelper = new FileHelper(mContext);

        setHasOptionsMenu(true);

        mListView = getListView();
        mListView.setTextFilterEnabled(true);

        startLoadData();
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
            mAdapter.startQuery(newText);
        }

        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (checkLifeCycle()) Log.i(TAG, "onCreateOptionsMenu");
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
                startExport();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (checkLifeCycle()) Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkLifeCycle()) Log.i(TAG, "onCreate");
    }


    @Override
    public void onStart() {
        super.onStart();
        if (checkLifeCycle()) Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkLifeCycle()) Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (checkLifeCycle()) Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (checkLifeCycle()) Log.i(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (checkLifeCycle()) Log.i(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (checkLifeCycle()) Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (checkLifeCycle()) Log.i(TAG, "onDestroy");
    }

}
