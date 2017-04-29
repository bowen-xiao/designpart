package com.bowen.hannengclub.network.configraution;

import android.text.TextUtils;

import com.bowen.hannengclub.MyApplication;
import com.bowen.hannengclub.util.Constans;
import com.bowen.hannengclub.util.SHA1Util;
import com.bowen.hannengclub.util.UserUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 肖稳华 on 2017/4/20.
 */

public class CommonParamInterceptor implements Interceptor {

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
		Request original = chain.request();

		//请求定制：添加请求头
		Request.Builder requestBuilder = original.newBuilder()
										//添加公共参数到请求头
										//.header("APIKEY", "android")
										;

		//请求体定制：统一添加token参数
		if(original.body() instanceof FormBody){
			Map<String,Object> reqParams = new HashMap<>();
			FormBody.Builder newFormBody = new FormBody.Builder();
			FormBody oidFormBody = (FormBody) original.body();
			for (int i = 0;i<oidFormBody.size();i++){
				newFormBody.addEncoded(oidFormBody.encodedName(i),oidFormBody.encodedValue(i));
				reqParams.put(oidFormBody.encodedName(i),oidFormBody.encodedValue(i));
			}
			String token = UserUtil.getToken(MyApplication.context);
			timestamp = System.currentTimeMillis();
			//添加公共的请求参数信息
			newFormBody.add("signature",getSign());
			newFormBody.add("timestamp",timestamp + "");
			newFormBody.add("nonce",nonce + "");
			newFormBody.add("device_type",device_type + "");
			newFormBody.add("api_ver",version_id + "");

			//如果有就进行添加
			if(!TextUtils.isEmpty(token)){
				newFormBody.add("token",token);
				reqParams.put("token",token);

				newFormBody.add("login_token",token );
				reqParams.put("login_token",token);
			}

			reqParams.put("signature",getSign());
			reqParams.put("timestamp",timestamp + "");
			reqParams.put("nonce",nonce + "");
			reqParams.put("device_type",device_type + "");
			reqParams.put("api_ver",version_id + "");


			//第一种：普遍使用，二次取值
//			ToolLog.i("通过Map.keySet遍历key和value：");
//			for (String key : reqParams.keySet()) {
//				ToolLog.i("key : value ;" + key + " : " + reqParams.get(key));
//			}

			requestBuilder.method(original.method(),newFormBody.build());
			/*.addQueryParameter("signature", getSign())
				.addQueryParameter("timestamp", timestamp + "")
				.addQueryParameter("nonce", nonce + "")
				.addQueryParameter("device_type", device_type + "")
				.addQueryParameter("api_ver", version_id + "")
			//                 .addQueryParameter("login_token", "")*/
		}

		Request request = requestBuilder.build();
		return chain.proceed(request);
	}
}
