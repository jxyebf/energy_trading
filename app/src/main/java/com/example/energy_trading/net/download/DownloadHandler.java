package com.example.energy_trading.net.download;



import android.os.AsyncTask;

import com.example.energy_trading.net.callback.IError;
import com.example.energy_trading.net.callback.IFailure;
import com.example.energy_trading.net.callback.IRequest;
import com.example.energy_trading.net.callback.ISuccess;
import com.example.energy_trading.net.rx.RestCreator;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author: wuchao
 * @date: 2017/11/6 22:48
 * @desciption:
 */

public class DownloadHandler {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(String url,
                           IRequest request,
                           String downDir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error) {
        this.URL = url;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = downDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
    }

    public final void handleDownload() {
        if (REQUEST != null) {
            REQUEST.onRequestStart();   //请求开始的回调
        }
        RestCreator.getRestService().download(URL,PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
//使用线程池方法进行下载，内部默认的线程池：核心1，最大20，时间3分
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWNLOAD_DIR,EXTENSION,responseBody,NAME);

                            //这里一定要注意判断，否则文件下载不全
                            if (task.isCancelled()){//下载结束
                                if (REQUEST != null){
                                    REQUEST.onRequestEnd();
                                }
                            }
                        }else {
                            if (ERROR != null){
                                ERROR.onError(response.code(), response.message());
                            }
                        }
                        RestCreator.getParams().clear();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE != null){
                            FAILURE.onFailure();
                            RestCreator.getParams().clear();
                        }
                    }
                });
    }

}
