package com.lenovo.frame.net;

import android.content.Context;

import com.lenovo.frame.net.base.BaseInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author feizai
 * @date 12/21/2020 021 10:05:39 PM
 */
public class RetrofitClient {
    private RequestAPI api;
    private ObservableTransformer schedulersTransformer;

    public static final class Builder {
        private RequestAPI api;
        private Retrofit retrofit;
        private Retrofit.Builder retrofitBuilder;
        private OkHttpClient okHttpClient;
        private OkHttpClient.Builder okHttpClientBuilder;

        private Context mContext;
        private String mUrl;
        private Map<String, String> mHeaders;
        private int mTimeOut;

        private ConnectionPool connectionPool;

        private static final int DEFAULT_TIMEOUT = 20;

        //处理线程调度的变换
        private ObservableTransformer schedulersTransformer;

        public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
            @Override
            public Observable<T> apply(Throwable throwable) throws Exception {
                return Observable.error(ExceptionHandle.handleException(throwable));
            }
        }

        public Builder(Context context) {
            mContext = context;
            mTimeOut = DEFAULT_TIMEOUT;
            mUrl = RequestAPI.BASE_URL;
            mHeaders = new HashMap<>();
            // 这里你可以根据自己的机型设置同时连接的个数和时间，这里使用默认设置5个，和每个保持时间为5s
            connectionPool = new ConnectionPool();

            retrofitBuilder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

            okHttpClientBuilder = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

            schedulersTransformer = new ObservableTransformer() {
                @Override
                public ObservableSource apply(Observable upstream) {
                    return ((Observable) upstream).subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorResumeNext(new HttpResponseFunc<>());
                }
            };
        }

        public Builder setUrl(String url) {
            if (!url.isEmpty())
                mUrl = url;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            if (!headers.isEmpty())
                mHeaders = headers;
            return this;
        }

        public Builder setTimeOut(int timeOut) {
            if (timeOut>0)
                mTimeOut = timeOut;
            return this;
        }

        // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
        public Builder setConnectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) {
            connectionPool = new ConnectionPool(maxIdleConnections, keepAliveDuration, timeUnit);
            return this;
        }

        /**
         * create you ApiService
         * Create an implementation of the API endpoints defined by the {@code service} interface.
         */
        private <T> T create(final Class<T> service) {
            if (service == null) {
                throw new RuntimeException("Api service is null!");
            }
            return retrofit.create(service);
        }

        public RetrofitClient builder() {
            okHttpClient = okHttpClientBuilder
                    .addInterceptor(new BaseInterceptor(mHeaders))
                    .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                    .connectionPool(connectionPool)
                    .build();
            retrofit = retrofitBuilder
                    .client(okHttpClient)
                    .baseUrl(mUrl)
                    .build();
            if (api == null) {
                api = create(RequestAPI.class);
            }
            return new RetrofitClient(this);
        }
    }

    private RetrofitClient(Context context) {
        this(new Builder(context));
    }

    private RetrofitClient(Builder builder) {
        api = builder.api;
        schedulersTransformer = builder.schedulersTransformer;
    }

    public void destory() {
        this.api = null;
        this.schedulersTransformer = null;
    }

    public void selectAllUser(Observer<?> observer){
        api.selectAllUser().compose(schedulersTransformer).subscribe(observer);
    }
}
