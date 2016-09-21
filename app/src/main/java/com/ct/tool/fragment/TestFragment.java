package com.ct.tool.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.ct.tool.utils.FileHelper;

import java.io.File;

/**
 * Created by Cting on 2016/8/31.
 */
public class TestFragment extends ListFragment {

    private static final String TAG = "cting";
    public TestFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File file = new File(FileHelper.getZipPath());
        try {
            FileHelper.getZipStrFileContent(file, "theme-config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void switchAdbEnabled() {
        Settings.Global.putInt(getActivity().getContentResolver(), Settings.Global.ADB_ENABLED, 1);//no permission
    }


}
