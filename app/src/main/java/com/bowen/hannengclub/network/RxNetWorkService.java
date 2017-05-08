package com.bowen.hannengclub.network;

import com.bowen.hannengclub.bean.BaseReqResult;
import com.bowen.hannengclub.bean.LoginResult;
import com.bowen.hannengclub.bean.VersionResult;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 肖稳华 on 2016/11/23.
 * 通用的网络请求框架,用户模块
 */

public interface RxNetWorkService {

    //访问百度后面的空不能省略
    @FormUrlEncoded
    @POST(" ")
    Observable<String> getBaiDuInfo(@FieldMap Map<String,Object> filedMap );

    //key	是	string	信鸽推送KEY
    @FormUrlEncoded
    @POST("based/push/index")
    Observable<String> upLoadPushID(@FieldMap Map<String,Object> filedMap);

    //登录  LoginResult
    @FormUrlEncoded
    @POST("user/login/index")
    Observable<LoginResult> login(@FieldMap Map<String,Object> paramMap);

    //获取检验码
    @FormUrlEncoded
    @POST("based/code/index")
    Observable<BaseReqResult> getPhoneCode(@FieldMap Map<String,Object> paramMap);

    @FormUrlEncoded
    @POST("based/code/checkcode")
    Observable<BaseReqResult> checkPhoneCode(@FieldMap Map<String,Object> paramMap);

    @FormUrlEncoded
    @POST("user/login/register")
    Observable<BaseReqResult> register(@FieldMap Map<String,Object> paramMap);

    @FormUrlEncoded
    @POST("user/login/resetpasswd")
    Observable<BaseReqResult> resetPasword(@FieldMap Map<String,Object> paramMap);

    @FormUrlEncoded
    @POST("based/version/index")
    Observable<VersionResult> getVersion(@FieldMap Map<String,Object> param);

    //http://api.hannengclub.com/user/login/get
    @FormUrlEncoded
    @POST("user/login/get")
    Observable<LoginResult> getLogin(@FieldMap Map<String,Object> param);

    //http://api.hannengclub.com/user/login/get
    @FormUrlEncoded
    @POST("user/login/third")
    Observable<String> thirdLogin(@FieldMap Map<String,Object> param);

    @FormUrlEncoded
    @POST("user/login/thirdclick")
    Observable<String> thirdLoginClick(@FieldMap Map<String,Object> param);

    @FormUrlEncoded
    @POST("user/login/thirdbind")
    Observable<String> thirdbind(@FieldMap Map<String,Object> param);
}
