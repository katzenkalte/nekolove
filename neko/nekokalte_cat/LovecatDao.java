package neko.neko.nekokalte_cat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import neko.neko.nekokalte.LovecatDbOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class LovecatDao {

	/*
	 * DAO = データベースへのアクセスをまとめたクラス。 このクラスはtblSampleへのアクセスに特化しています。
	 * テーブルが増える毎にそのテーブルにあわせたクラスを作成していきます。 基本機能としては、DBへの追加、更新・削除となります。
	 */
	// DbHelper向けのテーブル作成クエリー（SQL文）を定数化しています。
	public static final String CREATE_TABLE_CAT_ID = "create table catID ("
			+ "_id integer primary key autoincrement, " + "name text, "
			+ "sex text, " + "sexID integer, " + "age text, "
			+ "ageUnit text, " + "ageUnitID integer, " + "weight text, "
			+ "weightUnit text, " + "weightUnitID integer, "
			+ "hairColor text, " + "facePhoto blob, " + "thumnail blob "
			+ ");";

	private LovecatDbOpenHelper helper;

	// コンストラクタ
	public LovecatDao(Context context) {
		this.helper = new LovecatDbOpenHelper(context);
	}

	/*
	 * *********************************************************
	 * getCurrentRecordData()メソッド カレントレコードのフィールドデータを取得（ローカルメソッド） 引数：Cursor
	 * 戻り値：TblSampleRecord
	 * *********************************************************
	 */
	private TblLovecatRecord getCurrentRecordData(Cursor c) {
		// 各フィールドデータを取得

		int catID_id = c.getInt(c.getColumnIndex("_id"));
		int sexID = c.getInt(c.getColumnIndex("sexID"));
		int ageUnitID = c.getInt(c.getColumnIndex("ageUnitID"));
		int weightUnitID = c.getInt(c.getColumnIndex("weightUnitID"));

		String name = c.getString(c.getColumnIndex("name"));
		String sex = c.getString(c.getColumnIndex("sex"));
		String age = c.getString(c.getColumnIndex("age"));
		String ageUnit = c.getString(c.getColumnIndex("ageUnit"));
		String weight = c.getString(c.getColumnIndex("weight"));
		String weightUnit = c.getString(c.getColumnIndex("weightUnit"));
		String hairColor = c.getString(c.getColumnIndex("hairColor"));

		Bitmap facePhoto = byteToBitmap(c
				.getBlob(c.getColumnIndex("facePhoto")));
		Bitmap thumnail = byteToBitmap(c.getBlob(c.getColumnIndex("thumnail")));

		return new TblLovecatRecord(catID_id, sexID, ageUnitID, weightUnitID, name,
				sex, age, ageUnit, weight, weightUnit, hairColor, facePhoto,
				thumnail);
	}

	/*
	 * *********************************************************
	 * select()メソッド ラベルフィールドへのあいまい検索結果を抽出 引数：String
	 * 戻り値：ArrayList<TblSampleRecord>
	 * *********************************************************
	 */
	public ArrayList<TblLovecatRecord> select(String keyword) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getWritableDatabase();

			if (keyword == null) {
				keyword = "";
			}

			// 検索条件をセット（SQL）
			String sql = "select * from catID where name like ? order by _id ;";
			String[] args = { "%" + keyword + "%" };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			ArrayList<TblLovecatRecord> list = new ArrayList<TblLovecatRecord>();
			while (c.moveToNext()) {
				list.add(getCurrentRecordData(c));
			}

			// カーソルを閉じる
			c.close();
			c = null;

			return list;

		//}catch (Exception e){


		} finally {
			// 後始末
			if (c != null) {
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}


	}

	/*
	 * *********************************************************
	 * getRecord()メソッド 指定IDの該当レコードデータを返す 引数：int 戻り値：TblSampleRecord
	 * *********************************************************
	 */
	public TblLovecatRecord getRecord(int id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
			String sql = "select * from catID where _id = ?;";
			String[] args = { String.valueOf(id) };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜１件）
			TblLovecatRecord r = null;

			if (c.moveToFirst()) {
				r = getCurrentRecordData(c);
			}

			// カーソルを閉じる
			c.close();
			c = null;

			return r;

		} finally {
			// 後始末
			if (c != null) {
				c.close();
			}
			if (db != null) {
				db.close();
			}
		}
	}

	/*
	 * *********************************************************
	 * allDelete()メソッド テーブルデータを全件削除する 引数：なし 戻り値：なし
	 * *********************************************************
	 */
	public void allDelete() {
		SQLiteDatabase db = null;
		try {
			// DBを書き込みモードでOPEN
			db = helper.getWritableDatabase();

			// 検索条件指定
			String where = "_id like '%'";

			// 削除実行
			db.delete("catID", where, null);

			// auto_incrementの値を初期化
			db.execSQL("update sqlite_sequence set seq=1 where name='catID';");

		} finally {
			// 後始末
			if (db != null) {
				db.close();
			}
		}
	}

	/*
	 * *********************************************************
	 * delete()メソッド 指定IDの該当レコードを削除する 引数：int 戻り値：int
	 * *********************************************************
	 */
	public int delete(int id) {
		SQLiteDatabase db = null;
		try {
			// DBを書き込みモードでOPEN
			db = helper.getWritableDatabase();

			// 検索条件指定
			String where = "_id = ?";
			String[] args = { String.valueOf(id) };

			// 削除実行
			return db.delete("catID", where, args);

		} finally {
			// 後始末
			if (db != null) {
				db.close();
			}
		}
	}

	/*
	 * *********************************************************
	 * update()メソッド 指定IDの該当レコードを更新する 引数：tableName,value 戻り値：int
	 * *********************************************************
	 */
	public int update(String tableName, ContentValues values, int id) {
		SQLiteDatabase db = null;
		try {
			// DBを書き込みモードでOPEN
			db = helper.getWritableDatabase();

			// 検索条件指定
			String where = "_id = ?";
			String[] args = { String.valueOf(id) };

			// 更新実行
			return db.update(tableName, values, where, args);

		} finally {
			// 後始末
			if (db != null) {
				db.close();
			}
		}
	}

	/*
	 * *********************************************************
	 * insert()メソッド レコードを追加する 引数：tableName,value 戻り値：long
	 * *********************************************************
	 */
	public long insert(String tableName, ContentValues values) {
		SQLiteDatabase db = null;
		try {
			// DBを書き込みモードでOPEN
			db = helper.getWritableDatabase();

			// 追加処理実行
			return db.insert(tableName, null, values);

		} finally {
			// 後始末
			if (db != null) {
				db.close();
			}
		}
	}

	/*
	 * *********************************************************
	 * bitmapToByte()メソッド ビットマップをバイト型に変換する（ローカルメソッド） 引数：Bitmap 戻り値：byte[]
	 * *********************************************************
	 */
	public byte[] bitmapToByte(Bitmap bitmap) {

		byte[] blob = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, baos);	//エラーが表示されるとき
			blob = baos.toByteArray();
		} catch (Exception e) {
		}

		return blob;
	}

	/*
	 * *********************************************************
	 * byteToBitmap()メソッド バイト型をビットマップ型に変換する（ローカルメソッド） 引数：byte[] 戻り値：Bitmap
	 * *********************************************************
	 */
	public Bitmap byteToBitmap(byte[] blob) {

		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length,
					options);
		} catch (Exception e) {
		}

		return bitmap;
	}
}
