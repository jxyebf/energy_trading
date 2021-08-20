package com.example.energy_trading.interceptors;


import androidx.annotation.NonNull;


import com.example.energy_trading.Util.File.FileUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 根据传入参数，如果请求的url中包含参数，进行拦截，更改返回体。
 */

public class DebugInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;
    private final int DEBUG_RAW_ID;

    public DebugInterceptor(String debugUrl, int rawId) {
        this.DEBUG_URL = debugUrl;
        this.DEBUG_RAW_ID = rawId;
    }

    private Response getResponse(Interceptor.Chain chain, String json) {
        return new Response.Builder()//更改返回体
                .code(200)
                .addHeader("content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .message("ok")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    private Response debugResponse(Interceptor.Chain chain, int rawId) {
        String json = FileUtil.getRawFile(rawId); //将要返回的内容转为json格式
        return getResponse(chain, json);
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        final String url = chain.request().url().toString();
        if (url.contains(DEBUG_URL)) { //请求的url中包含参数
            return debugResponse(chain, DEBUG_RAW_ID);
        }
        return chain.proceed(chain.request());
    }
}
