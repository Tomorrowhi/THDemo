package com.tomorrowhi.thdemo.activitys;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageButton;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.f2prateek.rx.preferences2.Preference;
import com.tomorrowhi.thdemo.R;
import com.tomorrowhi.thdemo.base.BaseActivity;
import com.tomorrowhi.thdemo.common.MyConstants;
import com.tomorrowhi.thdemo.util.retrofitUtils.ApiService;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by zhaotaotao on 13/01/2017.
 * RxJava使用Demo
 */
public class RxJavaUseActivity extends BaseActivity {

    @BindView(R.id.title_return_iv)
    ImageButton titleReturnIv;

    private Subscriber<String> subscriber;
    private Flowable<String> flowable;
    private Consumer<String> consumer;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_rx_java;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        flowable.subscribe(subscriber);
        flowable.subscribe(consumer);
        method_1();
        method_2();
        method_3();
        method_4();
        method_5();
        Preference<Long> appId = defaultRxPreferences.getLong(MyConstants.APP_ID);
        Preference<Boolean> showWhatsNew = defaultRxPreferences.getBoolean("show-whats-new", true);
        Preference<String> username = defaultRxPreferences.getString("username", MyConstants.DEFAULT);

        username.asObservable().subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });

        appId.asObservable()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.d("appId Long :" + aLong);
                    }
                });
        appId.set(1000L);
        Long aLong = appId.get();
        LogUtils.d("appId value :" + aLong);
    }

    private void method_5() {
        Flowable.just("Hellow ThDemo")
                .flatMap(new Function<String, Publisher<Long>>() {
                    @Override
                    public Publisher<Long> apply(String s) throws Exception {
                        return Flowable.interval(1, TimeUnit.SECONDS);
                    }
                })
                .compose(this.<Long>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.d("along:" + aLong);
                    }
                });
        Flowable.interval(1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.d("SECONDS doOnNext along" + aLong);
                    }
                }).flatMap(new Function<Long, Publisher<String>>() {
            @Override
            public Publisher<String> apply(Long aLong) throws Exception {
                return Flowable.just(aLong + "");
            }
        }).compose(this.<String>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.d("subscribe along" + s);
                    }
                });
        Flowable<String> just = Flowable.just("213");
        just.compose(this.<String>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
    }

    private void method_4() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getText()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        ToastUtils.showShortToast("获取成功");
                        LogUtils.d("获取成功");
                        LogUtils.d(responseBody.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtils.showShortToast("获取失败，检查网络");
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("网络请求结束");
                    }
                });

        List<String> list = Flowable.range(1, 100)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "id:" + integer;
                    }
                }).toList().blockingGet();
        LogUtils.d(list);
    }

    private void method_3() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("exception:" + (1 / 1));
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.d(s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        LogUtils.d("onError");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("3秒后显示");
                e.onComplete();
                LogUtils.d("3秒后显示");
                SystemClock.sleep(3000);
                e.onNext("thDemo");
//                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                //指定发送数据是在IO线程（某个子线程）
                .subscribeOn(Schedulers.io())
                //指定订阅者是在主线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShortToast(s);
                    }
                });
    }

    private void method_2() {
        Flowable.just("map_1").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return s.hashCode();
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer.toString();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.d("Consumer accept_2 " + s);
            }
        });

        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(1);
        list.add(5);
        list.add(6);
        Flowable.fromIterable(list).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.d("fromIterable Consumer accept_2 " + integer);
            }
        });
        Flowable.just(list).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                Observable.fromIterable(integers).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.d("just fromIterable Consumer accept_2 " + integer);
                    }
                });
            }
        });

        Flowable.just(list).flatMap(new Function<List<Integer>, Publisher<Integer>>() {
            @Override
            public Publisher<Integer> apply(List<Integer> integers) throws Exception {
                return Flowable.fromIterable(integers);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.d("flatMap fromIterable Consumer accept_2 " + integer);
            }
        });

        Flowable.fromArray(1, 10, 20, 40, 0, -1, 8, 2).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 5;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.d("filter fromIterable Consumer accept_2 " + integer);
            }
        });
        Flowable.fromArray(1, 2, 3, 4, 5, 6, 7).take(4).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.d("take fromIterable Consumer accept_2 " + integer);
            }
        });
        Flowable.just(1, 2, 3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.d("just doOnNext accept_2 " + integer);
                        integer = integer + 1;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.d("just subscribe accept_2 " + integer);
                    }
                });
    }

    private void method_1() {
        Flowable.just("Demo RxJava 2").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.d("Consumer accept " + s);
            }
        });
        Flowable.just("map").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s + "thDemo";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.d("Consumer accept " + s);
            }
        });
    }

    @Override
    protected void initEvent() {
        subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                LogUtils.d("RxJava onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                LogUtils.d("onNext " + s);
            }

            @Override
            public void onError(Throwable t) {
                LogUtils.d("onError" + t.getMessage());
            }

            @Override
            public void onComplete() {
                LogUtils.d("onComplete");
            }
        };
        consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.d("Consumer " + s);
            }
        };
    }

    @Override
    protected void initData() {
        flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("Demo RxJava");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }


}
