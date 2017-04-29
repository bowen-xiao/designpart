package com.bowen.hannengclub.network;

import com.bowen.hannengclub.MyApplication;
import com.bowen.hannengclub.util.Constans;
import com.bowen.hannengclub.util.SHA1Util;
import com.bowen.hannengclub.util.UserUtil;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 肖稳华 on 2016/11/22.
 */

public class UpLoadFile {


	public static void upFile(File file,String uploadUrl, Callback callback){

		 long timestamp = System.currentTimeMillis();

		 int nonce = new Random().nextInt(10000) + 10000;

		 int device_type = Constans.DEVICE_TYPE;

		 int version_id = Constans.VERSION_ID;

		String signature = SHA1Util.SHA1("TOKEN_WITH_HANNENG_IPHONE" + timestamp
										 + nonce);

		String token = UserUtil.getToken(MyApplication.context);

		OkHttpClient client = new OkHttpClient();
    /* 第一个要上传的file */
		RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file);
		String file1Name = file.getName();

    /* 第二个要上传的文件,这里偷懒了,和file1用的一个图片 */
		/*File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.jpg");
		RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream") , file2);
		String file2Name = "testFile2.txt";*/


    /* form的分割线,自己定义 */
		String boundary = "xx--------------------------------------------------------------xx";

		MultipartBody mBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "param" */
																 //.addFormDataPart("tag" , "异步上传")
																 .addFormDataPart("signature",signature)
																 .addFormDataPart("timestamp",timestamp + "")
																 .addFormDataPart("nonce",nonce + "")
																 .addFormDataPart("device_type",device_type + "")
																 .addFormDataPart("api_ver",version_id + "")
																.addFormDataPart("token",token)
																.addFormDataPart("login_token",token)
																.addFormDataPart("file","file")
            /* 底下是上传了两个文件 */
																 .addFormDataPart("file" , file1Name , fileBody1)
																// .addFormDataPart("file" , file2Name , fileBody2)
																 .build();

    /* 下边的就和post一样了 */
		Request request = new Request.Builder().url(uploadUrl).post(mBody).build();
		client.newCall(request).enqueue(callback);
	}


	//同步上传文件
	public static String synchronizationUpFile(File file,String uploadUrl){
		String result = "";
		OkHttpClient client = new OkHttpClient();
    /* 第一个要上传的file */
		RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream") , file);
		String file1Name = file.getName();

    /* form的分割线,自己定义 */
		String boundary = "xx--------------------------------------------------------------xx";

		MultipartBody mBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "param" */
																 .addFormDataPart("tag" , "同步上传图片")
            /* 底下是上传了两个文件 */
																 .addFormDataPart("file" , file1Name , fileBody1)
																 // .addFormDataPart("file" , file2Name , fileBody2)
																 .build();

    /* 下边的就和post一样了 */
		Request request = new Request.Builder().url(uploadUrl).post(mBody).build();
		try {
			Response execute = client.newCall(request).execute();
			String bodyStr = execute.body().string();
			//成功上传的结果
//			FileUploadResult resultBean = JSON.parseObject(bodyStr, FileUploadResult.class);
//			Log.e("main", " save result  " + bodyStr );
//			if(resultBean != null){
//				result = resultBean.getResult();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
