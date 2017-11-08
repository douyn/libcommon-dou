package com.dou.library.libcommon.app.activity.base_activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dou.library.libcommon.R;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * Created by Dou on 2016/8/25.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    /** dialog **/
    android.support.v7.app.AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //隐藏ActionBar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initParms(bundle);
        }

        initViewAndListener();

        doBusiness();


        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
    }

    /**
     * [业务逻辑]
     */
    protected abstract void doBusiness();

    /**
     * [处理Intent数据]
     */
    protected abstract void initParms(Bundle bundle);

    /**
     * [初始化UI和事件]
     */
    protected abstract void initViewAndListener();

    /**
     * [初始化布局]
     */
    protected abstract int getLayoutId();

    /**
     * [吐司]
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * [日志输出]
     */
    protected void showLog(String msg) {
        Logger.d(TAG, msg);
    }

    /**
     * [页面跳转]
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * [页面跳转]
     * @param clz
     * @param req_code
     */
    public void startActivity(Class<?> clz, int req_code) {
        startActivityForResult(new Intent(this, clz), req_code);
    }

    /**
     * [携带数据的页面跳转]
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls,
                                       int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
    }

    /**
     *  显示通知类dialog
     */
    protected void showConfirmDialog(String titile, String msg){
        showDialog(titile, msg, getApplicationContext().getResources().getString(R.string.ok)
                , null, null, null);
    }

    /**
     *  显示选择类dialog
     */
    protected void showSelectDialog(String title, String msg, DialogInterface.OnClickListener posListioner){
        showDialog(title, msg, getApplicationContext().getResources().getString(R.string.ok)
        , getApplicationContext().getResources().getString(R.string.cancel)
        , posListioner, null);
    }

    /**
     *  显示普通dialog, 点击dialog以外可以取消
     */
    public void showDialog(String title, String msg, String posText, String negText, DialogInterface.OnClickListener posListioner, DialogInterface.OnClickListener negListenrer){
        if(builder == null){
            builder = new android.support.v7.app.AlertDialog.Builder(this);
        }

        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(negText, negListenrer);
        builder.setPositiveButton(posText, posListioner);
        builder.show();
    }

    /**
     *  显示普通dialog，不可取消
     */
    private void showDialog(String title, String msg, String posText, String negText, DialogInterface.OnClickListener posListioner, DialogInterface.OnClickListener negListenre, boolean isCancelAble){
        if(builder == null){
            builder = new android.support.v7.app.AlertDialog.Builder(this);
        }

        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(negText, negListenre);
        builder.setPositiveButton(posText, posListioner);
        builder.setCancelable(isCancelAble);
        builder.show();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    protected void showWaitDialog(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void showWaitDialog(String message){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void hideWaitDialog(){
        if (progressDialog == null) {
            return;
        }

        progressDialog.dismiss();
    }

}
