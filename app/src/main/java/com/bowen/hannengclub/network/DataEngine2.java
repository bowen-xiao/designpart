package com.bowen.hannengclub.network;


import com.bowen.hannengclub.SysConfiguration;
import com.bowen.hannengclub.network.configraution.CommonParamInterceptor;
import com.bowen.hannengclub.network.configraution.FastJsonConverterFactory;
import com.bowen.hannengclub.network.configraution.LogInterceptor;
import com.bowen.hannengclub.network.configraution.SslContextFactory;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by 肖稳华 on 2016/10/11.
 * 数据引擎，用于获取网络数据,一个页面请求过多，用于请求合并
 */

public class DataEngine2 {

    private  static  Retrofit mRetrofit;

    //用于初始化数据引擎
    private synchronized static void initRetrofit(){
        if(mRetrofit == null){
            //用于添加第三方证书
            SSLSocketFactory sslSocketFactory = new SslContextFactory().getSslSocket().getSocketFactory();
            OkHttpClient client = new OkHttpClient.Builder()
//                                        .sslSocketFactory(sslSocketFactory)
                                        //用于添加公共的参数信息
                                        .addInterceptor(new CommonParamInterceptor())
                                        //用于日志拦截
                                        .addInterceptor(new LogInterceptor())
                                        .build();
            String mUrl = SysConfiguration.BASE_URL;
            mUrl = mUrl.substring(0, mUrl.lastIndexOf("/") + 1);
            mRetrofit = new Retrofit.Builder()
                .client(client)
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头  MyApplication.BASE_URL
                .baseUrl(mUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())//解析方法
                .build();
        }
    }

    public static <T> T getServiceApiByClass(Class<T> classOfT){
        if(mRetrofit == null){ initRetrofit();}
        return mRetrofit.create(classOfT);
    }

}
