package com.ct.tool.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ct.tool.unicode.UnicodeReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Cting on 2016/8/19.
 */
public class FileHelper {

    private static final String TAG = "cting/File";
    private static final boolean DEBUG = false;

    public static final String DEFAULT_FILE_ANME = "cttool.txt";
    String mFileDir;
    String mFilePath;
    Context mContext;

    public FileHelper(Context context) {
        mContext = context;
        mFileDir = getExportDir();
    }

    private static String getExportDir() {
        return Environment.getExternalStorageDirectory() + "/CtTool/";
    }

    public String exportToFile(String fileName, ArrayList<Map<String, Object>> data) {

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
            String line;
            for (Map<String, Object> map : data) {
                line = (index++) + "\t" + unMap(map) + "\n";
                if(DEBUG)   Log.d(TAG, line);
                out.write(line.getBytes("GB2312"));
            }
            out.close();
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            String result = "export to: " + mFilePath + (flag ? " succeed" : " fail");
            return result;
//            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }

    }

    public static String unMap(Map<String,Object> map) {
        StringBuilder sb = new StringBuilder();
        Object obj;
        Set<String> keySet=map.keySet();
        for (String key : keySet) {
            obj = map.get(key);
//            Log.d(TAG, "obj:" + obj);
            if (obj instanceof String) {
                sb.append(obj).append("\t");
            }
        }
        return sb.toString();
    }

    //debug for qzf
    public static String getZipPath() {
        return getExportDir()+"Symphony.zip";
    }
    public static String getZipStrFileContent(File zipFile, String targetFileName) throws Exception {
        if (zipFile == null || targetFileName == null || "".equals(targetFileName))
            return null;
        ZipFile zip = new ZipFile(zipFile);

        ZipEntry zipEntry = zip.getEntry(targetFileName);
        StringBuffer contentStr = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new UnicodeReader(zip.getInputStream(zipEntry), "utf-8"));
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zip.getInputStream(zipEntry), "utf-8"));
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            if (temp.startsWith("\uFEFF")) {
                Log.d(TAG, "before:"+temp);
                temp = temp.replaceFirst("\uFEFF", "");
                Log.d(TAG, "after:"+temp);
            }
            contentStr.append(temp).append("\n");
        }
        bufferedReader.close();
        String str = contentStr.toString().trim();
        Log.d(TAG, "qinzhifeng,ZipHelper__getZipStrFileContent__" + str);
        return str;
    }
}
