package com.dian.yunbo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.dian.yunbo.view.PageItemView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dian.com.yunbo.R;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class PageFragment extends DialogFragment {

    @BindView(R.id.et)
    EditText et;

    private PageItemView itemView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container);
        ButterKnife.bind(this, view);
        setPage();
        return view;
    }

    private void setPage() {
        String page = getArguments().getString("page");
        if (!TextUtils.isEmpty(page)) {
            et.setText(page);
            et.setSelection(page.length());
        }
    }

    @OnClick(R.id.center)
    void OnPage() {
        String input = et.getText().toString();
        if ("".equals(input)) {
            Toast.makeText(getContext(), "页数不能为空！",Toast.LENGTH_SHORT).show();
        } else {
            itemView.toPage(Integer.parseInt(input));
            onDestroyView();
        }
    }

    @OnClick(R.id.cancel)
    void Cancel() {
        onDestroyView();
    }

    public void setDataLoadCallBack(PageItemView itemView) {
        this.itemView = itemView;
    }

}
