package com.e1858.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.e1858.wuye.protocol.http.PacketRequest;
import com.e1858.wuye.protocol.http.UploadJson;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

public class HttpPacketClient {

	private static String			EncryptHeader	= "encrypt";
	private static String			Encrypt_Des3	= "des3";
	private static final String		TAG				= "HttpPacketClient";
	private static final String		ENCODING		= "UTF-8";
	private static final int		CONNECT_TIMEOUT	= 60 * 1000;
	private SyncHttpClient			httpClient;
	private String					apiServer;
	private String					fileServer;
	private final Gson				gson			= new Gson();
	private Handler					uiHandler		= null;

	public static HttpPacketClient	sInstance;

	public static HttpPacketClient getInstaince() {
		if (sInstance == null) {
			sInstance = new HttpPacketClient();
			sInstance.httpClient = new SyncHttpClient();
			sInstance.httpClient.setTimeout(CONNECT_TIMEOUT);
			sInstance.uiHandler = new Handler(Looper.getMainLooper());
		}
		return sInstance;
	}

	public static UploadJson syncUpdaloadFile(String file) {
		final UploadJson uploadJson = new UploadJson();
		RequestParams params = new RequestParams();
		try {
			File fFile = new File(file);
			params.put("file", fFile);
			params.put("filename", file);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		getInstaince().httpClient.post(getInstaince().fileServer, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				UploadJson tmp = getInstaince().gson.fromJson(arg2, UploadJson.class);
				uploadJson.setError(tmp.getError());
				uploadJson.setRet(tmp.getRet());
				uploadJson.setUrl(tmp.getUrl());
				MLog.i(TAG, "receive responseString " + arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String responseString, Throwable throwable) {
				if (throwable != null) {
					uploadJson.setError(throwable.getLocalizedMessage());
				} else {
					uploadJson.setError("网络连接错误");
				}
				MLog.i(TAG, "receive error " + uploadJson.getError() + " \nresponseString " + responseString);
			}
		});
		return uploadJson;
	}

	public static <ResponseClass> void postPacketAsynchronous(final PacketRequest request,
			final Class<ResponseClass> responseClass, final ResponseHandler<ResponseClass> responseHandler) {
		postPacketAsynchronous(request, responseClass, responseHandler, false);
	}

	public static <ResponseClass> void postPacketSynchronous(PacketRequest request,
			final Class<ResponseClass> responseClass, final ResponseHandler<ResponseClass> responseHandler) {
		postPacketSynchronous(request, responseClass, responseHandler, false);
	}

	public static <ResponseClass> void postPacketSynchronous(PacketRequest request,
			final Class<ResponseClass> responseClass, final ResponseHandler<ResponseClass> responseHandler,
			boolean encrypt) {
		getInstaince().postPacket(request, responseClass, responseHandler, null, encrypt);
	}

	public static <ResponseClass> void postPacketAsynchronous(final PacketRequest request,
			final Class<ResponseClass> responseClass, final ResponseHandler<ResponseClass> responseHandler,
			final boolean encrypt) {
		new Thread() {
			@Override
			public void run() {
				getInstaince().postPacket(request, responseClass, responseHandler, sInstance.uiHandler, encrypt);
			}
		}.start();
	}

	//////////////////
	private <ResponseClass> void postPacket(PacketRequest request, final Class<ResponseClass> responseClass,
			final ResponseHandler<ResponseClass> responseHandler, Handler handler, boolean encrypt) {

		if (handler != null) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					responseHandler.onStart();
				}
			});
		} else {
			responseHandler.onStart();
		}

		String content = gson.toJson(request);
		String[] error = new String[1];
		String result = post(content, encrypt, error);
		responseHandler.error = error[0];
		try {
			responseHandler.responseObject = gson.fromJson(result, responseClass);
		}
		catch (Exception e) {
			responseHandler.error = "服务器返回数据格式错误";
		}

		if (handler != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					responseHandler.onFinish(responseHandler.responseObject, responseHandler.error);
				}
			});
		} else {
			responseHandler.onFinish(responseHandler.responseObject, responseHandler.error);
		}
	}

	public static abstract class ResponseHandler<ResponseClass> {
		public ResponseClass	responseObject	= null;
		public String			error			= null;
		private ProgressDialog	dialog;

		public void showProgressDialog(Context context, CharSequence message) {
			if (dialog == null) {
				dialog = new ProgressDialog(context);
			}
			dialog.setMessage(message);
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}

		public void hideProgressDialog() {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}

		public abstract void onStart();

		public abstract void onFinish(ResponseClass response, String error);
	}

	public static class SimpleResponseHandler<ResponseClass> extends ResponseHandler<ResponseClass> {

		@Override
		public void onStart() {}

		@Override
		public void onFinish(ResponseClass response, String error) {};
	}

	public String getApiServer() {
		return apiServer;
	}

	public void setApiServer(String apiServer) {
		this.apiServer = apiServer;
	}

	public String getFileServer() {
		return fileServer;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	/////////////////////////////

	// post form data
	public String post(String content, boolean encrypt, String[] error) {
		MLog.i(TAG, "send " + content);

		String contentType = "application/json";
		try {
			if (encrypt) {
				content = Encrypt.des3EncodeCBC(content);
			}
		}
		catch (Exception e) {
			error[0] = "加密失败";
			return null;
		}

		String result = null;
		int bufferSize = 1024;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		DataOutputStream dataOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		String contentEncoding;
		byte[] buffer = new byte[bufferSize];
		int readBytes = 0;
		try {
			httpURLConnection = (HttpURLConnection) (new URL(apiServer).openConnection());
			httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", ENCODING);
			httpURLConnection.setInstanceFollowRedirects(true);
			if (encrypt) {
				httpURLConnection.setRequestProperty(EncryptHeader, Encrypt_Des3);
			}
			httpURLConnection.setRequestProperty("Contenty-Type", contentType);

			dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			dataOutputStream.write(content.getBytes(ENCODING));
			dataOutputStream.flush();
			// get response
			byteArrayOutputStream = new ByteArrayOutputStream();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				while ((readBytes = inputStream.read(buffer)) > 0) {
					byteArrayOutputStream.write(buffer, 0, readBytes);
				}
			} else {
				error[0] = httpURLConnection.getResponseMessage();
			}
			contentEncoding = httpURLConnection.getContentEncoding();
			if (contentEncoding == null || contentEncoding.trim().length() == 0) {
				contentEncoding = ENCODING;
			}
			result = new String(byteArrayOutputStream.toByteArray(), contentEncoding);
		}
		catch (Exception ex) {
			error[0] = ex.getLocalizedMessage();
			ex.printStackTrace();
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (Exception ex) {}
			}
			if (dataOutputStream != null) {
				try {
					dataOutputStream.close();
				}
				catch (Exception ex) {}
			}

			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				}
				catch (Exception ex) {}
			}
			if (httpURLConnection != null) {
				try {
					httpURLConnection.disconnect();
				}
				catch (Exception ex) {}
			}
		}
		if (TextUtils.isEmpty(result) && TextUtils.isEmpty(error[0])) {
			error[0] = "未知错误";
		}
		if (!TextUtils.isEmpty(result)) {
			if (Encrypt_Des3.equalsIgnoreCase(httpURLConnection.getHeaderField(EncryptHeader))) {
				try {
					result = Encrypt.des3DecodeCBC(result);
				}
				catch (Exception e) {
					error[0] = "解密失败";
					e.printStackTrace();
				}
			}
			MLog.i(TAG, "receive " + result);
		} else {
			MLog.i(TAG, "receive error " + error[0]);
		}
		return result;
	}
}
