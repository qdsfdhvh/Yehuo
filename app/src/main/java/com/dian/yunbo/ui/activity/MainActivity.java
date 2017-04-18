package com.dian.yunbo.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import dian.com.yunbo.R;

import com.dian.yunbo.App;
import com.dian.yunbo.api.LocalApi;
import com.dian.yunbo.api.SourceApi;
import com.dian.yunbo.manager.Permission;
import com.dian.yunbo.sited.YhSource;
import com.dian.yunbo.ui.BaseActivity;
import com.dian.yunbo.ui.BaseFragment;
import com.dian.yunbo.ui.fragment.ComicFragment;
import com.dian.yunbo.ui.fragment.HistFragment;
import com.dian.yunbo.ui.fragment.LikeFragment;
import com.dian.yunbo.ui.fragment.SearchFragment;
import com.dian.yunbo.ui.fragment.SitedFragment;
import com.dian.yunbo.utils.FileUtil;

import static com.dian.yunbo.Navigation.showBtInfo;

public class MainActivity extends BaseActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_NUM = 3;

    @BindView(R.id.main_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private long mExitTime = 0;
    private int mCheckItem;
    private BaseFragment mCurrentFragment;
    private SparseArray<BaseFragment> mFragmentArray;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String getLayoutTitle() {
        return "云播";
    }

    @Override
    protected void onStart() {
        super.onStart();
        Permission.get(this); //请求权限
    }

    @Override
    public void initViews(Bundle save) {
        initDrawerToggle();
        initFragment();
        forIntent(getIntent());
    }

    private void initDrawerToggle() {
        /* 点击切换fragment */
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0,0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (refreshCurrentFragment()) {
                    getFragmentManager().beginTransaction().show(mCurrentFragment).commit();
                } else {
                    getFragmentManager().beginTransaction().add(R.id.main_fragment, mCurrentFragment).commit();
                }
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initFragment() {
        mCheckItem = R.id.drawer_search;  //初始页:搜索
        mNavigationView.setCheckedItem(mCheckItem);
        mFragmentArray = new SparseArray<>(FRAGMENT_NUM);
        refreshCurrentFragment();
        getFragmentManager().beginTransaction().add(R.id.main_fragment, mCurrentFragment).commit();
    }

    /* 判断集合中是否有目标fragment，没有就添加 */
    private boolean refreshCurrentFragment() {
        mCurrentFragment = mFragmentArray.get(mCheckItem);
        if (mCurrentFragment == null) {
            switch (mCheckItem) {
                case R.id.drawer_search:
                    mCurrentFragment = new SearchFragment();
                    break;
                case R.id.drawer_comic:
                    mCurrentFragment = new ComicFragment();
                    break;
                case R.id.drawer_like:
                    mCurrentFragment = new LikeFragment();
                    break;
                case R.id.drawer_hist:
                    mCurrentFragment = new HistFragment();
                    break;
                case R.id.drawer_sited:
                    mCurrentFragment = new SitedFragment();
                    break;
            }
            mFragmentArray.put(mCheckItem, mCurrentFragment);
            return false;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId != mCheckItem) {
            switch (itemId) {
                case R.id.drawer_search:
                case R.id.drawer_comic:
                case R.id.drawer_like:
                case R.id.drawer_hist:
                case R.id.drawer_sited:
                    mCheckItem = itemId;
                    getFragmentManager().beginTransaction().hide(mCurrentFragment).commit();
                    if (mToolbar != null) {
                        mToolbar.setTitle(item.getTitle().toString());
                    }
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (System.currentTimeMillis() - mExitTime > 2000) {
            Snackbar.make(mDrawerLayout, "再按一次退出", Snackbar.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    protected void forIntent(final Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        if (!Intent.ACTION_VIEW.equals(action)) {
            return;
        }

        Uri uri = intent.getData();
        if (uri != null) {
            String scheme = uri.getScheme();

            if(scheme.equals("magnet")) {
                String hash = uri.getQuery().replace("xt=urn:btih:", "");
                showBtInfo(this, hash);
            }

            if(scheme.equals("file")) {
                try {
                    ContentResolver cr = this.getContentResolver();
                    //加载文本
                    final String sited = FileUtil.toString(cr.openInputStream(uri));
                    //转换为source并尝试更新
                    YhSource source = SourceApi.getInstance().load(sited, true);
                    //source保存到本地
                    String path = App.getInstance().getSitePath() + source.title + ".yh";
                    FileUtil.saveText2Sdcard(path, sited);
                    Toast.makeText(this, "安装成功：" + source.title, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
