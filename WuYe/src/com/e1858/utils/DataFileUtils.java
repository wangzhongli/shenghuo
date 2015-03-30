package com.e1858.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

import com.e1858.common.app.BaseApplication;

/**
 * 日期: 2015年2月5日 上午11:47:59
 * 作者: 刘浩歌
 * 邮箱: okz@outlook.com
 * 作用:
 */
public class DataFileUtils {

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private static boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = BaseApplication.getBaseInstance().getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public static boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = BaseApplication.getBaseInstance().openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				oos.close();
			}
			catch (Exception e) {}
			try {
				fos.close();
			}
			catch (Exception e) {}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = BaseApplication.getBaseInstance().openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		}
		catch (FileNotFoundException e) {}
		catch (Exception e) {
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = BaseApplication.getBaseInstance().getFileStreamPath(file);
				data.delete();
			}
		}
		finally {
			try {
				ois.close();
			}
			catch (Exception e) {}
			try {
				fis.close();
			}
			catch (Exception e) {}
		}
		return null;
	}
}
