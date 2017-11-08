package com.dou.library.libcommon.app.activity.base_swipebackactivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dou.library.libcommon.R;
import com.dou.library.libcommon.app.appmanager.AppManager;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/13.
 */

public abstract class BaseSwipeBackAppcompatActivity extends AppCompatActivity implements SwipeBackActivityBase {
    /** swipeback helper **/
    private SwipeBackActivityHelper mHelper;

    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();

    /** dialog **/
    android.support.v7.app.AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    /** toolbar **/
    Toolbar mToolBar;
    ToolbarHelper mToolbarHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        AppManager.getAppManager().addActivity(this);
        Bundle bundle = getIntent().getExtras();

        initToolBar();

        initParms(bundle);

        initViewAndListener();

        doBusiness();
    }

    /**
     * [初始化ToolBar]
     */
    private void initToolBar() {

        if (mToolBar != null) {
            setSupportActionBar(mToolBar);

            mToolbarHelper = new ToolbarHelper(mToolBar);

            handleToolbar(mToolbarHelper);
        }
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
     * [toolbar设置]
     */
    protected void handleToolbar(ToolbarHelper toolbarHelper) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    };

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
        Log.d(TAG, msg);
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
     * [无效的]
     * [沉浸式状态栏]
     */
    @Deprecated
    private void initState() {
        //设置noTitle
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置android 4.4以上即api19以上的状态栏为半透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow ().getAttributes ();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * [显示通知类dialog]
     */
    protected void showConfirmDialog(String titile, String msg){
        showDialog(titile, msg, getApplicationContext().getResources().getString(R.string.ok)
                , null, null, null);
    }

    /**
     *  [显示选择类dialog]
     */
    protected void showSelectDialog(String title, String msg, DialogInterface.OnClickListener posListioner){
        showDialog(title, msg, getApplicationContext().getResources().getString(R.string.ok)
                , getApplicationContext().getResources().getString(R.string.cancel)
                , posListioner, null);
    }

    /**
     *  [显示普通dialog, 点击dialog以外可以取消]
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
     *  [显示普通dialog，不可取消]
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

    /**
     * [显示加载框]
     */
    protected void showWaitDialog(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * [显示加载框，自定义文字]
     * @param message
     */
    protected void showWaitDialog(String message){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * [隐藏加载框]
     */
    protected void hideWaitDialog(){
        if (progressDialog == null) {
            return;
        }

        progressDialog.dismiss();
    }

    /**
     * [重写]
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    /**
     * [重写]
     * [根据resId返回view]
     * @param id
     * @return
     */
    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    /**
     * [重写]
     * [获取swipelayout]
     */
    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    /**
     * [重写]
     * [设置是否禁用swipeback]
     * @param enable
     */
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    /**
     * [重写]
     * [滑动销毁activity]
     */
    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * [创建toolbarmenu菜单]
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * menu菜单点击的回调
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * ToolBar Helper
     */
    public static class ToolbarHelper {

        private Toolbar toolbar;

        public ToolbarHelper(Toolbar toolbar) {
            this.toolbar = toolbar;
        }

        public Toolbar getToolbar() {
            return toolbar;
        }

        public void setTitle(String title) {
            TextView titleTV = (TextView) toolbar.findViewById(R.id.toolbar_title);
            titleTV.setText(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
