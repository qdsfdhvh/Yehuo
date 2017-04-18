package com.dian.yunbo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import dian.com.yunbo.R;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.custom_toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initToolbar();
        initViews(savedInstanceState);
    }

    protected void initToolbar() {
        if (mToolbar != null) {
            mToolbar.setTitle(getLayoutTitle());
            setSupportActionBar(mToolbar);
        }
    }

    public abstract int getLayoutId();

    public abstract String getLayoutTitle();

    public abstract void initViews(Bundle save);

}
