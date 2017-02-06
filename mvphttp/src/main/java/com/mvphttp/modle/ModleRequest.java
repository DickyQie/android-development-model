package com.mvphttp.modle;

import android.util.Log;

import com.mvphttp.api.MyApplication;
import com.mvphttp.callback.CallBackListenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zq on 2016/8/16.
 */

public class ModleRequest implements ILoadHttp {

    private OkHttpClient mOkHttpClient= MyApplication.mOkHttpClient;
    @Override
    public void onGet(CallBackListenter listenter) {
        getAsynHttp(listenter);
    }
    /**
     * get异步请求
     */
    private void getAsynHttp(final CallBackListenter listenter) {
        Request.Builder requestBuilder = new Request.Builder().url("https://api.github.com/repos/square/retrofit/contributors");
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listenter.onError("数据加载失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.body().string();
                    listenter.onDataCallBackListenter(str);
                } else {
                    String str = response.body().string();
                    listenter.onDataCallBackListenter(str);
                }
            }
        });
    }
}
