package com.bowen.zdsjclub.network.configraution;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class CommonParamInterceptor implements Interceptor {

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request original = chain.request();

		//请求定制：添加请求头
		Request.Builder requestBuilder = original.newBuilder()
										//添加公共参数到请求头
										.header("APIKEY", "android")
										;

		//请求体定制：统一添加token参数
		if(original.body() instanceof FormBody){
			FormBody.Builder newFormBody = new FormBody.Builder();
			FormBody oidFormBody = (FormBody) original.body();
			for (int i = 0;i<oidFormBody.size();i++){
				newFormBody.addEncoded(oidFormBody.encodedName(i),oidFormBody.encodedValue(i));
			}
			//添加公共的请求参数信息
			newFormBody.add("token","test");
			requestBuilder.method(original.method(),newFormBody.build());
		}

		Request request = requestBuilder.build();
		return chain.proceed(request);
	}
}
