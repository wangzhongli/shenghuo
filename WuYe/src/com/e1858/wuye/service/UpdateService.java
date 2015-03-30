package com.e1858.wuye.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.e1858.common.Constant;
import com.e1858.common.app.AppManager;
import com.e1858.utils.AppUtil;
import com.e1858.wuye.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
/**
 * 下载服务
 * @author jiajia 2014年9月4日
 *
 */
public class UpdateService extends Service{
	private final static int DOWNLOAD_COMPLETE = 0;//下载完成
	private final static int DOWNLOAD_FAIL = 1; //下载失败
	private File updateDir = null;
	private File updateFile = null;
	private NotificationManager updateNotificationManager = null;//通知管理
	private Notification updateNotification = null;//通知
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;//用于通知栏,即将发生的事情
	private String download_url;
	private String currentActivity;
	private boolean isForceUP=false;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		download_url=intent.getStringExtra("download_url");
		currentActivity=intent.getStringExtra("currentActivity");
		isForceUP=intent.getBooleanExtra("forceUP", false);
	    if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){
	        updateDir = new File(Environment.getExternalStorageDirectory(),"app/download/");
	        updateFile = new File(updateDir.getPath(),Constant.APKNAME);
	    }
	    this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	    this.updateNotification = new Notification();
	    //下载过程中,点击通知栏回到当前view
	    updateIntent = new Intent(this,currentActivity.getClass());
	    updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
	    //设置通知栏显示内容
	    updateNotification.icon = R.drawable.icon;
	    updateNotification.tickerText = "畅享生活";
	    updateNotification.setLatestEventInfo(this,getResources().getString(R.string.app_name),"0%",updatePendingIntent);
	    //发出通知
	    updateNotificationManager.notify(0,updateNotification);
	    //开启一个新的线程下载
	    new Thread(new updateRunnable()).start();
	     
	    return super.onStartCommand(intent, flags, startId);
	}
	private Handler updateHandler = new  Handler(){
		@SuppressWarnings("static-access")
		@Override
	    public void handleMessage(Message msg) {
	    	 switch(msg.what){
	            case DOWNLOAD_COMPLETE:
	            	//强制安装
	            	if(isForceUP){
	            		updateNotificationManager.cancel(0);
	            		stopService(updateIntent);
	            		AppUtil.installNormal(UpdateService.this, updateFile.getAbsolutePath());
	            		AppManager.getAppManager().finishAllActivity();
	            	}else{
	            		updateNotification.flags|=updateNotification.FLAG_AUTO_CANCEL;
		                //点击安装PendingIntent
		                Uri uri = Uri.fromFile(updateFile);
		                Intent installIntent = new Intent(Intent.ACTION_VIEW);
		                installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
		                updatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);
		                updateNotification.defaults = Notification.DEFAULT_SOUND;
		                updateNotification.setLatestEventInfo(UpdateService.this, getResources().getString(R.string.app_name), "下载完成,点击安装。", updatePendingIntent);
		                updateNotificationManager.notify(0, updateNotification);
		                //停止服务
		                stopService(updateIntent);
	            	}
	            	
	            case DOWNLOAD_FAIL:
	                //下载失败
	                updateNotification.setLatestEventInfo(UpdateService.this, getResources().getString(R.string.app_name), "下载完成,点击安装。", updatePendingIntent);
	                updateNotificationManager.notify(0, updateNotification);
	                stopService(updateIntent);
	            default:
	            	updateNotificationManager.cancel(0);
	                stopService(updateIntent);
	        }  
	    }
	};
	
	
	class updateRunnable implements Runnable {
        Message message = updateHandler.obtainMessage();
        public void run() {
            message.what = DOWNLOAD_COMPLETE;
            try{
                if(!updateDir.exists()){
                    updateDir.mkdirs();
                }
                if(!updateFile.exists()){
                    updateFile.createNewFile();
                }
                  long downloadSize=downloadUpdateFile(download_url, updateFile);
                if(downloadSize>0){
                    updateHandler.sendMessage(message);
                }
            }catch(Exception ex){
                ex.printStackTrace();
                message.what = DOWNLOAD_FAIL;
                updateHandler.sendMessage(message);
            }
        }
    }
	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
        int downloadCount = 0;
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;
         
        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
         
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            if(currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
            }
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();                   
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[4096];
            int readsize = 0;
            while((readsize = is.read(buffer)) > 0){
                fos.write(buffer, 0, readsize);
                totalSize += readsize;
                if((downloadCount == 0)||(int) (totalSize*100/updateTotalSize)-10>downloadCount){ 
                    downloadCount += 10;
                    updateNotification.setLatestEventInfo(UpdateService.this, "正在下载", (int)totalSize*100/updateTotalSize+"%", updatePendingIntent);
                    updateNotificationManager.notify(0, updateNotification);
                }                        
            }
        } finally {
            if(httpConnection != null) {
                httpConnection.disconnect();
            }
            if(is != null) {
                is.close();
            }
            if(fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }
		
}
