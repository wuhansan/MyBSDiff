package com.yfsd.wl.mybsdiff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yfsd.wl.mybsdiff.utils.UriParseUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context =this;

        TextView version = findViewById(R.id.sample_text);

        version.setText(BuildConfig.VERSION_NAME);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms,200);
            }
        }

    }

    /**
     *
     * @param oldApk 旧版本的安装包
     * @param patch 差异包
     * @param output 合成的新版本的输出路径
     */
    public native void bsPatch(String oldApk, String patch ,String output);


    @SuppressLint("StaticFieldLeak")
    public void update(View view) {
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                //获取旧版本的路径
                String oldApk = getApplicationInfo().sourceDir;

                String patch = new File(Environment.getExternalStorageDirectory(),"patch").getAbsolutePath();

                String output= createNewApk().getAbsolutePath();

                bsPatch(oldApk,patch,output);
                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                //安装已经合成的apk

                if (file.exists()){
                    //安装已经合成的apk
                    UriParseUtils.installApk(context,file);
                }else {
                    Toast.makeText(context, "------- null ------", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }
    //创建合成之后的新版apk文件
    private File createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(),"bsdiff.apk");
        if (!newApk.exists()){
            try {
                newApk.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  newApk;
    }
}
