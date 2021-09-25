package com.lenovo.frame.net;

import android.content.Context;
import android.text.TextUtils;

import com.lenovo.frame.entity.User;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
    private RequsetAPI api;
    private Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private static String baseUrl = RequsetAPI.baseURL;

    private static Context mContext;
    private static String mUrl;
    private static Map<String, String> mHeaders;

    private static final int DEFAULT_TIMEOUT = 20;

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS);

    private RetrofitClient() {
    }

    private RetrofitClient(Context context, String url, Map<String, String> headers) {
        //url为空，则默认使用baseUrl
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        okHttpClient = httpBuilder.build();

        //创建retrofit
        retrofit = builder.client(okHttpClient).baseUrl(url).build();
        if (api == null) {
            api = create(RequsetAPI.class);
        }
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null)
            mContext = context;
        return RetrofitClientHolder.sInstance;
    }

    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null)
            mContext = context;
        if (url != null)
            mUrl = url;
        return RetrofitClientHolder.sInstance;
    }

    public static RetrofitClient getInstance(Context context, String url, Map<String, String> headers) {
        if (context != null)
            mContext = context;
        if (url != null)
            mUrl = url;
        if (headers != null)
            mHeaders = headers;
        return RetrofitClientHolder.sInstance;
    }

    private static class RetrofitClientHolder {
        private static final RetrofitClient sInstance = new RetrofitClient(mContext, mUrl,mHeaders);
    }

    //处理线程调度的变换
    ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return ((Observable) upstream).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public void selectAllUser(Observer<?> observer){
        api.selectAllUser().compose(schedulersTransformer).subscribe(observer);
    }
}
