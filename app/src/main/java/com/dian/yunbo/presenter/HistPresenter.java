package com.dian.yunbo.presenter;

import com.dian.yunbo.App;
import com.dian.yunbo.model.HistBean;
import com.dian.yunbo.utils.FileUtil;
import com.dian.yunbo.view.HistItemView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
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
import static com.dian.yunbo.utils.FileUtil.FormatTime;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public class HistPresenter extends BasePresenter<HistItemView> {

    public HistPresenter() {
        mDisposables = new CompositeDisposable();
    }

    public void loadData() {
        Disposable disposable = getHist().delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<HistBean>>() {
                    @Override
                    public void accept(List<HistBean> list) throws Exception {
                        itemView.onSuccess(list);
                    }
                });
        mDisposables.add(disposable);
    }

    private Single<List<HistBean>> getHist() {
        File hist = new File(App.getInstance().getHistPath());
        File[] strs = new File[] {};
        if (hist.exists()) {
            strs = hist.listFiles();
        }
        return Observable.fromArray(strs)
                .sorted(new FileUtil.FileComparator())
                .flatMap(new Function<File, ObservableSource<HistBean>>() {
                    @Override
                    public ObservableSource<HistBean> apply(File hist) throws Exception {
                        String json = FileUtil.readTextFromSDcard(hist);
                        Type type = new TypeToken<HistBean>(){}.getType();
                        HistBean bean = new Gson().fromJson(json, type);

                        if (bean != null) {
                            bean.setTime(FormatTime(hist));
                            return Observable.just(bean);
                        }
                        return null;
                    }
                })
                .filter(new Predicate<HistBean>() {
                    @Override
                    public boolean test(HistBean bean) throws Exception {
                        return bean != null;
                    }
                })
                .toList();
    }

}
