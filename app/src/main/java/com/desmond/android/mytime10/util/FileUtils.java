package com.desmond.android.mytime10.util;

import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by gk on 15/9/25.
 */
public class FileUtils {
    public final static String sd_mt_items_FileName = Environment.getExternalStorageDirectory() + "/mt-items.txt";
    public final static String sd_mt_record_tiems_FileName = Environment.getExternalStorageDirectory() + "/mt-recordTimes.txt";

    // todo tasks
    public final static String sd_mt_todo_daily_FileName = Environment.getExternalStorageDirectory() + "/mt-tasks-daily.txt";
    public final static String sd_mt_todo_weekly_FileName = Environment.getExternalStorageDirectory() + "/mt-tasks-weekly.txt";
    public final static String sd_mt_todo_monthly_FileName = Environment.getExternalStorageDirectory() + "/mt-tasks-monthly.txt";

    public static String readFileSdcard(String path) {
        String str = "";
        try {
            FileInputStream localFileInputStream = new FileInputStream(path);
            byte[] arrayOfByte = new byte[localFileInputStream.available()];
            localFileInputStream.read(arrayOfByte);
            str = getString(arrayOfByte, "UTF-8");
            localFileInputStream.close();
            return str;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public static void writeFileSdcard(String path, String paramString2) {
        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(path, true);
            localFileOutputStream.write(paramString2.getBytes());
            localFileOutputStream.flush();
            localFileOutputStream.close();
        } catch (Exception localException) {
                localException.printStackTrace();
        }

    }

    public static void delete(String path) {
        File f = new File(path);
        if(f.exists()) {
            f.delete();
        }
    }

    public static void writeFileSdcardNew(String path, String paramString2)
    {
        try
        {
            FileOutputStream localFileOutputStream = new FileOutputStream(path);
            localFileOutputStream.write(paramString2.getBytes());
            localFileOutputStream.flush();
            localFileOutputStream.close();
            return;
        }
        catch (Exception localException)
        {
            while (true)
                localException.printStackTrace();
        }
    }

    public static String getString(final byte[] data, final String charset) {

        try {
            return new String(data, 0, data.length, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(data, 0, data.length);
        }
    }
}
