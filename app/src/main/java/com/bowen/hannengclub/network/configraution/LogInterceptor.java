package com.bowen.hannengclub.network.configraution;


import com.bowen.hannengclub.util.Constans;
import com.bowen.hannengclub.util.SHA1Util;
import com.bowen.hannengclub.util.ToolLog;

import java.io.IOException;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 肖稳华 on 2016/10/11.
 * 用于请求日志拦截
 */
public class LogInterceptor implements Interceptor {

    private long timestamp = System.currentTimeMillis();

    private int nonce = new Random().nextInt(10000) + 10000;

    private int device_type = Constans.DEVICE_TYPE;

    private int version_id = Constans.VERSION_ID;


    private  String getSign(){
       String signature = SHA1Util.SHA1("TOKEN_WITH_HANNENG_IPHONE" + timestamp
                                  + nonce);
        return signature;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        //String marvelHash = ApiUtils.generateMarvelHash(mApiKey, mApiSecret);
        Request oldRequest = chain.request();
        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                //公共参数，app版本号
//                 .addQueryParameter("signature", getSign())
//                 .addQueryParameter("timestamp", timestamp + "")
//                 .addQueryParameter("nonce", nonce + "")
//                 .addQueryParameter("device_type", device_type + "")
//                 .addQueryParameter("api_ver", version_id + "")
                  //公共参数信息 // TODO: 2017/4/24  登录后需要保存到本地的信息
//                 .addQueryParameter("login_token", "")
                //添加公共的参数信息,这里不需要
                //.addQueryParameter("token", token)
                ;

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        long t1 = System.nanoTime();
        Response response = chain.proceed(newRequest);
        long t2 = System.nanoTime();
        MediaType contentType = null;
        String bodyString = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }
        // 请求响应时间
        double time = (t2 - t1) / 1e6d;

        //打印请求日志 需要关闭日志
        ToolLog.w("retrofit--> \n",
                  String.format("requestUrl : %s " +
                                "\n Time : %s " +
                                "\n Header : %s " +
                                "\n response Code : %s " +
                                "\n response Header : %s" +
                                "\n response body : %s",
                                newRequest.url(),
                                newRequest.body(),
                                time,
                                newRequest.headers(),
                                response.code(),
                                response.headers(),
                                (bodyString)));

//        return chain.proceed(newRequest);
        if (response.body() != null) {
            // get完body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }
}