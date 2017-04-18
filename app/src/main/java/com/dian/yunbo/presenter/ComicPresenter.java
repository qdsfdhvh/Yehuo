package com.dian.yunbo.presenter;

import com.dian.yunbo.api.LocalApi;
import com.dian.yunbo.api.Comics;
import com.dian.yunbo.model.HeadBean;
import com.dian.yunbo.view.ComicItemView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by Seiko on 2017/3/30. Y
 */

public class ComicPresenter extends BasePresenter<ComicItemView> {

    public ComicPresenter() {
        mDisposables = new CompositeDisposable();
    }

    public void loadData() {
        Disposable disposable = create().delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ItemType>>() {
                    @Override
                    public void accept(List<ItemType> list) throws Exception {
                        itemView.onSuccess(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        itemView.onFailed();
                    }
                });
        mDisposables.add(disposable);
    }

    private Observable<List<ItemType>> create() {
        return Observable.create(new ObservableOnSubscribe<List<ItemType>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ItemType>> e) throws Exception {
                List<ItemType> list = new ArrayList<>();
                for (Comics comic : LocalApi.getInstance().getComics()) {
                    HeadBean bean = new HeadBean();
                    bean.setTitle(comic.getName());
                    bean.setType(comic.getID());
                    list.add(bean);
                    for (int i=0; i < 4; i++) {
                        list.add(comic.getRadsom());
                    }
                    e.onNext(list);
                    e.onComplete();
                }
            }
        });
    }
}
