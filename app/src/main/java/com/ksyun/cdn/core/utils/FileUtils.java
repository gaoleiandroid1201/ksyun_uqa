package com.ksyun.cdn.core.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final String LOG_TAG = "FileUtils";

    public FileUtils() { /* cannot be instantiated */
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "//" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
    public boolean deleteSDFile(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 写入内容到SD卡中的txt文本中 str为内容
     */
    public static void writeSDFile(byte[] content, String fileName, boolean isAppend) {
//        try {
//            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName, isAppend);
//            fw.write(str);
//            FileOutputStream os = new FileOutputStream(f);
//            DataOutputStream out = new DataOutputStream(os);
//            out.writeShort(2);
//            out.writeUTF("");
//            System.out.println(out);
//            fw.flush();
//            fw.close();
//            System.out.println(fw);
//        } catch (Exception e) {
//        }
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
        if (content == null) {
            throw new NullPointerException("bytes is null");
        }
        if (fileName == null) {
            throw new NullPointerException("file is null");
        }

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public String readSDFileSec(String fileName) {
        String res = "";
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");//选择合适的编码，如果不调整中文会乱码
            fin.close();//关闭资源
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String makePath(String path1, String path2) {
        if (path1.endsWith(File.separator))
            return path1 + path2;

        return path1 + File.separator + path2;
    }

    public static String getSdDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static File getCachePath(Context context) {
        String cachepath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            cachepath = String.format("%s/android/data/%s/cache", Environment
                    .getExternalStorageDirectory().getAbsolutePath(), context
                    .getApplicationInfo().packageName);
        } else {
            cachepath = context.getCacheDir().getAbsolutePath();
        }
        File cacheFile = new File(cachepath);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return cacheFile;
    }

    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1, filename.length());
        }
        return "";
    }

    public static String getNameFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(0, dotPosition);
        }
        return "";
    }

    public static String getPathFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(0, pos);
        }
        return "";
    }

    public static String getNameFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(pos + 1);
        }
        return "";
    }

    // return new file path if successful, or return null
    public static String copyFile(String src, String dest) {
        File file = new File(src);
        if (!file.exists() || file.isDirectory()) {
            Log.v(LOG_TAG, "copyFile: file not exist or is directory, " + src);
            return null;
        }

        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            File destPlace = new File(dest);
            if (!destPlace.exists()) {
                if (!destPlace.mkdirs())
                    return null;
            }

            String destPath = makePath(dest, file.getName());
            File destFile = new File(destPath);
            int i = 1;
            while (destFile.exists()) {
                String destName = getNameFromFilename(file.getName())
                        + " " + i++ + "."
                        + getExtFromFilename(file.getName());
                destPath = makePath(dest, destName);
                destFile = new File(destPath);
            }

            if (!destFile.createNewFile())
                return null;

            fi = new FileInputStream(file);
            fo = new FileOutputStream(destFile);
            final int count = 1024 * 100;
            byte[] buffer = new byte[count];

            int read = 0;
            while ((read = fi.read(buffer, 0, count)) != -1) {
                fo.write(buffer, 0, read);
            }

            return destPath;
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "copyFile: file not found, " + src);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "copyFile: " + e.toString());
        } finally {
            try {
                if (fi != null)
                    fi.close();
                if (fo != null)
                    fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static class SDCardInfo {
        public long total;
        public long free;
    }

    public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 312842725517781500L;

        {
            add("text/plain");
            add("text/plain");
            add("application/pdf");
            add("application/msword");
            add("application/vnd.ms-excel");
            add("application/vnd.ms-excel");
        }
    };

    public static String sZipFileMimeType = "application/zip";

    public static int CATEGORY_TAB_INDEX = 0;
    public static int SDCARD_TAB_INDEX = 1;
    public static int REMOTE_TAB_INDEX = 2;
}
