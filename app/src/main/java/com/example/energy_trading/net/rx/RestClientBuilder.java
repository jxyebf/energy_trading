package com.example.energy_trading.net.rx;

import android.content.Context;

import com.example.energy_trading.net.callback.IError;
import com.example.energy_trading.net.callback.IFailure;
import com.example.energy_trading.net.callback.IRequest;
import com.example.energy_trading.net.callback.ISuccess;
import com.example.energy_trading.ui.Loader.LoaderStyle;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 内部含有方法接受网络请求需要的参数。
 *
 * 主要作用：将传入的参数通过build()方法传回RestClient类中，为后续call请求做准备
 */

public class RestClientBuilder {

    private String mUrl =null;
    private  static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest;
    private ISuccess mISuccess ;
    private IFailure mIFailure ;
    private IError mIError ;
    private  RequestBody mBody;
    private File mFile=null;

    private String mDownloadDir=null;
    private String mExtension=null;
    private String mName=null;

    private Context mContext =null;
    private LoaderStyle mLoaderStyle =null;

    RestClientBuilder() {

    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String file) {
        this.mFile = new File(file);
        return this;
    }
    public final RestClientBuilder name(String name){
        this.mName=name;
        return this;
    }

    public final RestClientBuilder dir(String dir){
        this.mDownloadDir=dir;
        return this;
    }

    public final RestClientBuilder extension(String extension){
        this.mExtension=extension;
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest){
        this.mIRequest=iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess=iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure=iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError){
        this.mIError=iError;
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle loaderStyle) {
        this.mContext = context;
        this.mLoaderStyle = loaderStyle;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }


    public final RestClient build(){
        return new RestClient(mUrl, PARAMS, mIRequest, mDownloadDir, mExtension, mName, mISuccess, mIError, mIFailure, mBody, mFile, mContext, mLoaderStyle);
    }
}
