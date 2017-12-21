package com.lxc.birthday;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by LaiXiancheng on 2017/12/18.
 * Email: lxc.sysu@qq.com
 */

public class BirthDB extends SQLiteOpenHelper {

	private static final String TABLE_NAME = "Contacts";
	Context context;

	public BirthDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "create table " + TABLE_NAME
				+ "(name text , "
				+ "birth text , "
				+ "gift text);";
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void insert(String name, String birth, String gift) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("birth", birth);
		values.put("gift", gift);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}


	public void update(String name, String birth, String gift) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = "name = ?"; // 主键列名 = ?
		String[] whereArgs = {name}; // 主键的值
		ContentValues values = new ContentValues();
		values.put("birth", birth);
		values.put("gift", gift);
		db.update(TABLE_NAME, values, whereClause, whereArgs);
		db.close();
	}

	public void delete(String name) {
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = "name = ?"; // 主键列名 = ?
		String[] whereArgs = {name}; // 主键的值
		db.delete(TABLE_NAME, whereClause, whereArgs);
		db.close();
	}

	public ArrayList<BirthdayBean> queryAll() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
		ArrayList<BirthdayBean> birthdayBeans = new ArrayList<>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String birthday = cursor.getString(cursor.getColumnIndex("birth"));
			String gift = cursor.getString(cursor.getColumnIndex("gift"));
			BirthdayBean bean = new BirthdayBean(name, birthday, gift, "");
			birthdayBeans.add(bean);
		}
		db.close();
		return birthdayBeans;
	}

	public boolean isExits(String name) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where name='"
				+ name + "'", null);
		boolean isExits = false;
		if (cursor.moveToNext()) {
			isExits = true;
		}
		db.close();
		return isExits;
	}

	public String queryTel(String name) {
		String tel = "无";
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
		while (cursor != null && cursor.moveToNext()) {
			//获得联系人ID
			String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
			//获得联系人姓名
			String tel_name = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
			//获得联系人手机号码
			Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

			StringBuilder sb = new StringBuilder("");
			if (tel_name.equals(name)) {
				while (phone.moveToNext()) { //取得电话号码(可能存在多个号码)
					int phoneFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					String phoneNumber = phone.getString(phoneFieldColumnIndex);
					sb.append(phoneNumber + " ");
				}
				tel = sb.toString();
				phone.close();
				break;
			}
		}
		cursor.close();
		return tel;
	}
}
