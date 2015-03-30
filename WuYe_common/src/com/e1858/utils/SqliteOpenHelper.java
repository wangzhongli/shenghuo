package com.e1858.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class SqliteOpenHelper extends OrmLiteSqliteOpenHelper {
	static final int		VERSION_CREATE_20141120			= 1;
	static final int		VERSION_MALL_COMMON_20141230	= 4;
	static final int		VERSION_RELEASE_2_3_2_20150109	= 5;
	static final int		VERSION_RELEASE_2_4_0_20150204	= 6;								//添加商品评论
	static final int		VERSION_RELEASE_2_4_0_20150210	= 7;								//删除冗余实体
	static final int		VERSION_CURRENT					= VERSION_RELEASE_2_4_0_20150210;

	static List<Class<?>>	sEntityClassList				= new ArrayList<Class<?>>();

	public static void registEntity(Class<?> clazz) {
		sEntityClassList.add(clazz);
	}

	public SqliteOpenHelper(Context context) {
		super(context, "e1858_cache.db", null, VERSION_CURRENT);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			for (Class<?> clazz : sEntityClassList) {
				TableUtils.createTableIfNotExists(arg1, clazz);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			for (Class<?> clazz : sEntityClassList) {
				TableUtils.dropTable(arg1, clazz, true);
			}
			onCreate(arg0, arg1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDowngrade(SQLiteDatabase arg0, int arg2, int arg3) {
		super.onUpgrade(arg0, arg2, arg3);
	}
}
