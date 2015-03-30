package com.e1858.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.e1858.utils.StringUtils;
import com.e1858.wuye.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * 根据字母选择城市数据管理
 * @author jiajia
 *
 */
public class DBManager{
	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME="city_name.db";
	private SQLiteDatabase database;
	private Context context;
	public DBManager(Context context) {
		this.context = context;
	}
	public void openDatabase() {
		this.database = this.openDatabase(Constant.DATABASE_PATH + DB_NAME);
	}

	public SQLiteDatabase getDatabase() {
		return this.database;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			if (!StringUtils.isEmpty(Constant.DATABASE_PATH)) {
				String path = Constant.DATABASE_PATH;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				InputStream is = context.getResources().openRawResource(
						R.raw.city_name);
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
					fos.flush();
				}
				fos.close();
				is.close();
			}
			if(database!=null){
				database.close();
			}
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return database;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SQLiteDatabase.openOrCreateDatabase("",null);
	}

	public void closeDatabase() {
		if (this.database != null)
			this.database.close();
	}
}