package com.ct.tool.practice;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Cting on 2016/8/19.
 */
public class TestExportToFileUtil<D extends TestCtItemData> {

    private static final String TAG = "cting/Export";

    public static final String DEFAULT_FILE_ANME = "cttool.txt";
    String mFileDir;
    String mFilePath;
    Context mContext;

    public TestExportToFileUtil(Context context) {
        mContext = context;
        mFileDir = getExportDir();
    }

    private String getExportDir() {
        return Environment.getExternalStorageDirectory() + "/CtTool/";
    }

    public void saveToFile(String fileName, ArrayList<D> array) {

        int index = 0;
        if (TextUtils.isEmpty(fileName)) {
            fileName = DEFAULT_FILE_ANME;
        }

        File fileDir = new File(mFileDir);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        mFilePath = mFileDir + fileName;
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean flag = false;
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            String line = "";
            for (D d : array) {
                line = (index++) + "\t" + d.toString() + "\n";
                Log.d(TAG, line);
                out.write(line.getBytes("GB2312"));
            }
            out.close();
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(mContext, "export to: " + mFilePath + (flag ? " succeed" : " fail"), Toast.LENGTH_LONG).show();
        }

    }

}
