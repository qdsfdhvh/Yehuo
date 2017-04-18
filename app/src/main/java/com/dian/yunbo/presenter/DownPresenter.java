package com.dian.yunbo.presenter;

import com.dian.yunbo.App;
import com.dian.yunbo.model.DownBean;
import com.dian.yunbo.utils.FileUtil;
import com.dian.yunbo.view.DownItemView;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Seiko on 2017/4/8. Y
 */

public class DownPresenter extends BasePresenter<DownItemView> {

    public DownPresenter() {
        mDisposables = new CompositeDisposable();
    }

    public void loadData() {
        Disposable disposable = getDown().delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<DownBean>>() {
                    @Override
                    public void accept(List<DownBean> list) throws Exception {
                        itemView.onSuccess(list);
                    }
                });
        mDisposables.add(disposable);
    }

    private Single<List<DownBean>> getDown() {
        File downs = new File(App.getInstance().getDownPath());
        File[] strs = new File[] {};
        if (downs.exists()) {
            strs = downs.listFiles();
        }
        return Observable.fromArray(strs)
                .sorted(new FileUtil.FileComparator())
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File down) throws Exception {
                        return !down.getName().contains(".cache");
                    }
                })
                .flatMap(new Function<File, ObservableSource<DownBean>>() {
                    @Override
                    public ObservableSource<DownBean> apply(File down) throws Exception {
                        return Observable.just(new DownBean(down));
                    }
                })
                .toList();
    }
}
