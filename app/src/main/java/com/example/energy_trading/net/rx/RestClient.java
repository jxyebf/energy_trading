package com.example.energy_trading.net.rx;

import android.content.Context;

import com.example.energy_trading.net.HttpMethod;
import com.example.energy_trading.net.callback.IError;
import com.example.energy_trading.net.callback.IFailure;

import com.example.energy_trading.net.callback.IRequest;
import com.example.energy_trading.net.callback.ISuccess;
import com.example.energy_trading.net.callback.RequestCallbacks;
import com.example.energy_trading.net.download.DownloadHandler;
import com.example.energy_trading.ui.Loader.Energy_trade_Loader;
import com.example.energy_trading.ui.Loader.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Restful请求的处理--同接口，使用不同方法，可以产生不同的作用
 * 创建RestService的实例，单例，确保全局唯一性
 */

public class RestClient {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;

    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;

    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final File FILE;
    private final Context CONTEXT;


    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      String download_dir,
                      String extension,
                      String name,
                      ISuccess success,
                      IError error,
                      IFailure failure,
                      RequestBody body,
                      File file,
                      Context context,
                      LoaderStyle loaderStyle) {

        this.URL = url;
        this.DOWNLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.FAILURE = failure;
        this.BODY = body;
        this.FILE = file;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if(REQUEST!=null){
            REQUEST.onRequestStart();
        }
        if (LOADER_STYLE != null) {
            Energy_trade_Loader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                //传入原始数据
                call = service.postRaw(URL, BODY);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                //传入原始数据
                call = service.putRaw(URL, BODY);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),FILE);
                final MultipartBody.Part body=MultipartBody.Part.createFormData("file",FILE.getName(),requestBody);
                call=RestCreator.getRestService().upload(URL,body);
                break;
            default:
                break;
        }
        if (call != null) {
            call.enqueue(getRequestCallback());
        }

/*
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return observable;
        */
    }

    private Callback<String> getRequestCallback(){
        return new RequestCallbacks(REQUEST,SUCCESS,FAILURE,ERROR,LOADER_STYLE);
    }
    public final void get(){
        request(HttpMethod.GET);
    }
    public final void post(){
        if(BODY==null){
            request(HttpMethod.POST);
        }else{
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put(){
        if(BODY==null){
            request(HttpMethod.PUT);
        }else{
            if(!PARAMS.isEmpty()){
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete(){
        request(HttpMethod.DELETE);
    }

    public final void upload(){
        request(HttpMethod.UPLOAD);
    }

    public final void download(){
        new DownloadHandler(URL,REQUEST,DOWNLOAD_DIR,EXTENSION,NAME,SUCCESS,FAILURE,ERROR).handleDownload();
    }
/*
    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            //原始数据PARAMS必须为null
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    public final Observable<String> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            //原始数据PARAMS必须为null
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null");
            }
            return request(HttpMethod.PUT_RAW);
        }
    }

    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<String> upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<ResponseBody> download() {
//        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME, SUCCESS, FAILURE, ERROR)
//                .handleDoenload();
        final Observable<ResponseBody> responseBodyObservable = RestCreator.getRestService().download(URL, PARAMS);
        return responseBodyObservable;
    }

 */
}
