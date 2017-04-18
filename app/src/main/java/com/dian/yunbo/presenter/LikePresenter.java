package com.dian.yunbo.presenter;

import com.dian.yunbo.App;
import com.dian.yunbo.model.LikeBean;
import com.dian.yunbo.utils.FileUtil;
import com.dian.yunbo.view.LikeItemView;
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
 * Created by Seiko on 2017/4/8. Y
 */

public class LikePresenter extends BasePresenter<LikeItemView> {

    public LikePresenter () {
        mDisposables = new CompositeDisposable();
    }

    public void loadData() {
        Disposable disposable = getLike().delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LikeBean>>() {
                    @Override
                    public void accept(List<LikeBean> list) throws Exception {
                        itemView.onSuccess(list);
                    }
                });
        mDisposables.add(disposable);
    }

    private Single<List<LikeBean>> getLike() {
        File like = new File(App.getInstance().getLikePath());
        File[] strs = new File[] {};
        if (like.exists()) {
            strs = like.listFiles();
        }
        return Observable.fromArray(strs)
                .sorted(new FileUtil.FileComparator())
                .flatMap(new Function<File, ObservableSource<LikeBean>>() {
                    @Override
                    public ObservableSource<LikeBean> apply(File like) throws Exception {
                        String json = FileUtil.readTextFromSDcard(like);
                        Type type = new TypeToken<LikeBean>(){}.getType();
                        LikeBean bean = new Gson().fromJson(json, type);

                        if (bean != null) {
                            bean.setTime(FormatTime(like));
                            return Observable.just(bean);
                        }
                        return null;
                    }
                })
                .filter(new Predicate<LikeBean>() {
                    @Override
                    public boolean test(LikeBean bean) throws Exception {
                        return bean != null;
                    }
                })
                .toList();
    }

}
