package com.bowen.hannengclub.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bowen.hannengclub.R;
import com.bowen.hannengclub.activity.HomeActivity;
import com.bowen.hannengclub.util.ToolLog;

import java.io.File;

/**
 * Created by 肖稳华 on 2017/5/2.
 public class UpLoadService extends Service {
 */

/**
 * Created by yuandl on 2016-12-19.
 */

public class UpLoadService extends Service {
	/****
	 * 发送广播的请求码
	 */
	private final int    REQUEST_CODE_BROADCAST = 0X0001;
	/****
	 * 发送广播的action
	 */
	private final String BROADCAST_ACTION_CLICK = "servicetask";
	/**
	 * 通知
	 */
	private Notification notification;
	/**
	 * 通知的Id
	 */
	private final int NOTIFICATION_ID = 1123;
	/**
	 * 通知管理器
	 */
	private static NotificationManager notificationManager;
	/**
	 * 通知栏的远程View
	 */
	private        RemoteViews         mRemoteViews;

	/**
	 * 自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
	 */
	private String filePath = Environment.getExternalStorageDirectory() + "/ServiceTask/";
	private File    file;
	private boolean isCancelled;
	private Thread  mDownThread;
	private String mDownLoadUrl;

	/**
	 * 通知栏操作的四种状态
	 */
	private enum Status {
		DOWNLOADING, PAUSE, FAIL, SUCCESS
	}

	/**
	 * 当前在状态 默认正在下载中
	 */
	private Status status = Status.DOWNLOADING;
	private MyBroadcastReceiver myBroadcastReceiver;

	public final static String DOWNLOAD_RUL = "download_url";

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			registerBroadCast();
			mDownLoadUrl = intent.getStringExtra(DOWNLOAD_RUL);
			uploadFile();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	//下载文件
	private void uploadFile(){
		//如果线程已经启动
		if(mDownThread != null || TextUtils.isEmpty(mDownLoadUrl)){
			Toast.makeText(this, "正在下载中", Toast.LENGTH_SHORT).show();
			return;
		}
		//用这个显示进度
		showNotificationProgress(this);
		showFileName();
		initAPKDir();
		//downloadThread
		//不计时
		//关闭通知栏
		mDownThread = new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 101; i++) {
					if (!isCancelled) {
						updateNotification(0.01f * i);
					} else {
						//不计时
						i--;
					}
					try {
						Thread.sleep(80L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//停止相关业务
				//stopService();
			}
		};
		mDownThread.start();
	}

	private String APK_dir;
	private void initAPKDir() {
		/**
		 * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
		 * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
		 */
		if (isHasSdcard())// 判断是否插入SD卡
			APK_dir = getApplicationContext().getFilesDir().getAbsolutePath() + "/apk/download/";// 保存到app的包名路径下
		else
			APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/apk/download/";// 保存到SD卡路径下
		File destDir = new File(APK_dir);
		if (!destDir.exists()) {// 判断文件夹是否存在
			destDir.mkdirs();
		}
		APK_dir = File.separator + APK_dir +File.separator+"hannengclub.apk";
	}

	/**
	 *
	 * @Description:判断是否插入SD卡
	 */
	private boolean isHasSdcard() {
		String status = Environment.getExternalStorageDirectory().getAbsolutePath();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	//停止服务
	private void stopService(){
		//打开新页面
//		Intent dialogIntent = new Intent(getBaseContext(), HomeActivity.class);
//		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		getApplication().startActivity(dialogIntent);

		//关闭通知栏
		notificationManager.cancel(NOTIFICATION_ID);
		//将线程清除
		mDownThread = null;
		stopSelf();
	}

	/**
	 * 注册按钮点击广播*
	 */
	private void registerBroadCast() {
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_ACTION_CLICK);
		registerReceiver(myBroadcastReceiver, filter);
	}


	/**
	 * 更新通知界面的按钮的广播
	 */
	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals(BROADCAST_ACTION_CLICK)) {
				return;
			}
			Log.d("status=", "status : " + status);
			switch (status) {
				case DOWNLOADING:
					/**当在下载中点击暂停按钮**/

					mRemoteViews.setTextViewText(R.id.bt, "下载");
					mRemoteViews.setTextViewText(R.id.tv_message, "暂停中...");
					status = Status.PAUSE;
					notificationManager.notify(NOTIFICATION_ID, notification);
					break;
				case SUCCESS:
					mRemoteViews.setTextViewText(R.id.tv_name, "下载的标题");
					mRemoteViews.setTextViewText(R.id.tv_size, String.valueOf(100));
					//		int result = Math.round((float) current / (float) total * 100);
					//mRemoteViews.setTextViewText(R.id.tv_progress, percent);
					mRemoteViews.setProgressBar(R.id.pb, 100, 100, false);
					/**当下载完成点击完成按钮时关闭通知栏**/
					notificationManager.cancel(NOTIFICATION_ID);
					break;
				case FAIL:

				case PAUSE:
					/**当在暂停时点击下载按钮**/
					isCancelled = !isCancelled;
					mRemoteViews.setTextViewText(R.id.bt, isCancelled ? "下载" : "暂停");
					mRemoteViews.setTextViewText(R.id.tv_message, isCancelled ? "暂停中..." : "下载中...");
					status = Status.DOWNLOADING;
					notificationManager.notify(NOTIFICATION_ID, notification);
					break;
			}
		}
	}



	/**
	 * 显示一个下载带进度条的通知
	 *
	 * @param context 上下文
	 */
	public void showNotificationProgress(Context context) {
		/**进度条通知构建**/
		NotificationCompat.Builder builderProgress = new NotificationCompat.Builder(context);
		/**设置为一个正在进行的通知**/
		builderProgress.setOngoing(true);
		/**设置小图标**/
		builderProgress.setSmallIcon(R.mipmap.ic_launcher);

		/**新建通知自定义布局**/
		mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
		/**进度条ProgressBar**/
		mRemoteViews.setProgressBar(R.id.pb, 100, 0, false);
		/**提示信息的TextView**/
		mRemoteViews.setTextViewText(R.id.tv_message, "下载中...");
		/**操作按钮的Button**/
		mRemoteViews.setTextViewText(R.id.bt, "暂停");
		/**设置左侧小图标*/
		mRemoteViews.setImageViewResource(R.id.iv, R.mipmap.ic_launcher);
		/**设置通过广播形式的PendingIntent**/
		Intent intent = new Intent(BROADCAST_ACTION_CLICK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_BROADCAST, intent, 0);
		mRemoteViews.setOnClickPendingIntent(R.id.bt, pendingIntent);
		/**设置自定义布局**/
		builderProgress.setContent(mRemoteViews);
		/**设置滚动提示**/
		builderProgress.setTicker("开始下载...");
		notification = builderProgress.build();
		/**设置不可手动清除**/
		notification.flags = Notification.FLAG_NO_CLEAR;
		/**获取通知管理器**/
		notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		/**发送一个通知**/
		notificationManager.notify(NOTIFICATION_ID, notification);
	}


	/**
	 * 在通知栏显示文件名
	 *
	 * @param
	 */
	private void showFileName() {
		float percent = 0.0f;
		mRemoteViews.setTextViewText(R.id.tv_name, "下载的标题");
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	/**
	 * 下载更改进度
	 *
	 * @param
	 * @param current 当前已下载大小
	 */
	private void updateNotification(double current) {
		ToolLog.e("main",current + " : current");
		String percent = (int)(100 * current) + "%";
		mRemoteViews.setTextViewText(R.id.tv_size, percent);
//		int result = Math.round((float) current / (float) total * 100);
		//mRemoteViews.setTextViewText(R.id.tv_progress, percent);
		mRemoteViews.setProgressBar(R.id.pb, 100, (int) (current * 100), false);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	/**
	 * 下载失败
	 */
	private void downloadFail() {
		Intent intent = new Intent(HomeActivity.UPDATE_ERR);
		sendBroadcast(intent);
		status = Status.FAIL;
		mRemoteViews.setTextViewText(R.id.bt, "重试");
		mRemoteViews.setTextViewText(R.id.tv_message, "下载失败");
		notificationManager.cancel(NOTIFICATION_ID);
		notificationManager.notify(NOTIFICATION_ID, notification);
		stopService();
	}

	/**
	 * 下载成功
	 */
	protected void downloadSuccess() {
		status = Status.SUCCESS;
		mRemoteViews.setTextViewText(R.id.bt, "完成");
		mRemoteViews.setTextViewText(R.id.tv_message, "下载成功");
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	/**
	 * 格式化文件大小
	 *
	 * @param size
	 * @return
	 */
	private String formatSize(long size) {
		String format;
		if (size >= 1024 * 1024) {
			format = byteToMB(size) + "M";
		} else if (size >= 1024) {
			format = byteToKB(size) + "k";
		} else {
			format = size + "b";
		}
		return format;
	}

	/**
	 * byte转换为MB
	 *
	 * @param bt 大小
	 * @return MB
	 */
	private float byteToMB(long bt) {
		int mb = 1024 * 1024;
		float f = (float) bt / (float) mb;
		float temp = (float) Math.round(f * 100.0F);
		return temp / 100.0F;
	}

	/**
	 * byte转换为KB
	 *
	 * @param bt 大小
	 * @return K
	 */
	private int byteToKB(long bt) {
		return Math.round((bt / 1024));
	}

	/**
	 * 销毁时取消下载，并取消注册广播，防止内存溢出
	 */
	@Override
	public void onDestroy() {
		if (myBroadcastReceiver != null) {
			unregisterReceiver(myBroadcastReceiver);
		}
		notificationManager = null;
		super.onDestroy();
	}

}
