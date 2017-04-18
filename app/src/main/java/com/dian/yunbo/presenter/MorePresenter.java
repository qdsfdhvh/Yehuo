package com.dian.yunbo.presenter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.dian.yunbo.api.LocalApi;
import com.dian.yunbo.api.Comics;
import com.dian.yunbo.model.ComicBean;
import com.dian.yunbo.view.MoreItemView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Seiko on 2017/3/26. Y
 */

public class MorePresenter extends BasePresenter<MoreItemView> {
    private int count = -10;
    private int num = 1;
    private Comics comic;

    public MorePresenter(String type) {
        mDisposables = new CompositeDisposable();
        comic = LocalApi.getInstance().getComic(type);
    }

    public void loadData() {
        Disposable disposable = create().delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ComicBean>>() {
                    @Override
                    public void accept(List<ComicBean> list) throws Exception {
                        itemView.onSuccess(list, getPage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        itemView.onFailed();
                    }
                });
        mDisposables.add(disposable);
    }

    private Observable<List<ComicBean>> create() {
        count += 10 * num;
        return Observable.create(new ObservableOnSubscribe<List<ComicBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ComicBean>> e) throws Exception {
                e.onNext(comic.sub(count, count + 10));
                e.onComplete();
            }
        });
    }

    /* 上下页 */
    public void setNum(int num) {
        this.num = num;
    }
    /* 第XX页 */
    public void setCount(int count) {
        this.count = (count - num - 1) * 10;
    }
    /* 当前页 */
    private int getPage() {return count / 10 + 1;}

}
