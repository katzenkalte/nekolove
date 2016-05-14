package neko.neko.nekokalte_weight;

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
import android.widget.Toast;

public class LovecatDao3 {

	public static final String CREATE_TABLE_WEIGHT = "create table weight_control ("
			+ "_id integer primary key autoincrement, "
			+ "catID_id integer, "
			+ "unit_id integer, "
			+ "year integer, "
			+ "monthOfYear integer, "
			+ "dayOfMonth integer, "
			+ "w_day text, "
			+ "weight float, "
			+ "unit text" + ");";

	private LovecatDbOpenHelper helper;

	// コンストラクタ
	public LovecatDao3(Context context) {
		this.helper = new LovecatDbOpenHelper(context);
	}

	/*
	 * *********************************************************
	 * getCurrentRecordData()メソッド カレントレコードのフィールドデータを取得（ローカルメソッド） 引数：Cursor
	 * 戻り値：TblSampleRecord
	 * *********************************************************
	 */
	private TblLovecatRecord3 getCurrentRecordData(Cursor c) {
		// 各フィールドデータを取得

		int weight_Id = c.getInt(c.getColumnIndex("_id"));
		int catID_id = c.getInt(c.getColumnIndex("catID_id"));
		int unit_id = c.getInt(c.getColumnIndex("unit_id"));

		int year = c.getInt(c.getColumnIndex("year"));
		int monthOfYear = c.getInt(c.getColumnIndex("monthOfYear"));
		int dayOfMonth = c.getInt(c.getColumnIndex("dayOfMonth"));

		String w_day = c.getString(c.getColumnIndex("w_day"));
		float weight = c.getFloat(c.getColumnIndex("weight"));
		String unit = c.getString(c.getColumnIndex("unit"));

		return new TblLovecatRecord3(weight_Id, catID_id, unit_id, year,
				monthOfYear, dayOfMonth, w_day, weight, unit);
	}

	/*
	 * *********************************************************
	 * select()メソッド ラベルフィールドへのあいまい検索結果を抽出 引数：String
	 * 戻り値：ArrayList<TblSampleRecord>
	 * *********************************************************
	 */
	public ArrayList<TblLovecatRecord3> select(String keyword) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			if (keyword == null) {
				keyword = "";
			}

			// 検索条件をセット（SQL）
			String sql = "select * from weight_control where _id like ? order by w_day desc, _id desc;";
			String[] args = { "%" + keyword + "%" };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			ArrayList<TblLovecatRecord3> list = new ArrayList<TblLovecatRecord3>();
			while (c.moveToNext()) {
				list.add(getCurrentRecordData(c));
			}

			// カーソルを閉じる
			c.close();
			c = null;

			return list;

			// }catch (Exception e){

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
	public TblLovecatRecord3 getRecord2(int id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
			String sql = "select * from weight_control where _id = ?";
			String[] args = { String.valueOf(id) };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜１件）
			TblLovecatRecord3 r=null;	//初期値なのでnullでOK

			if (c.moveToFirst()) {
//				Log.v("TblLovecatRecord3_getCount", ""+c.getCount());
//				Log.v("TblLovecatRecord3_weight", c.getString(c.getColumnIndex("weight")));
				r = getCurrentRecordData(c);
				Log.v("bbbbb", "bbbbb");
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
	 * getRecord()メソッド
	 * 指定IDの該当レコードデータを返す
	 * 引数：int 戻り値：TblSampleRecord
	 * *********************************************************
	 */
	public TblLovecatRecord3 getRecord(int catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
			String sql = "select * from weight_control where catID_id = ? order by _id desc;";
			String[] args = { String.valueOf(catID_id) };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜１件）
			TblLovecatRecord3 r=null;	//初期値なのでnullでOK

			if (c.moveToFirst()) {
//				Log.v("TblLovecatRecord3_getCount", ""+c.getCount());
//				Log.v("TblLovecatRecord3_weight", c.getString(c.getColumnIndex("weight")));
				r = getCurrentRecordData(c);
				Log.v("bbbbb", "bbbbb");
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
	/*
	 * *********************************************************
	 * getRecord()メソッド
	 * 指定IDの該当レコードデータを返す
	 * 引数：int 戻り値：TblSampleRecord
	 * *********************************************************
	 */
	public TblLovecatRecord3 getRecord4(int catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
			String sql = "select * from weight_control where catID_id = ? order by w_day desc;";
			String[] args = { String.valueOf(catID_id) };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜１件）
			TblLovecatRecord3 r=null;	//初期値なのでnullでOK

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

	 *
	 *
	 * *********************************************************
	 * getRecord3()メソッド
	 * 体重管理の最新の日付順に並べ、一番新しい日付のデータを取得する
	 *
	 * *********************************************************
	 */

	public TblLovecatRecord3 getRecord3(int catID_id, String w_day) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
			String sql = "select * from weight_control where catID_id = ? and w_day = ? order by _id desc;";
			String[] args = { String.valueOf(catID_id), w_day };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0?１件）
			TblLovecatRecord3 r=null;	//初期値なのでnullでOK

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
			db.delete("weight_control", where, null);

			// auto_incrementの値を初期化
			db.execSQL("update sqlite_sequence set seq=1 where name='weight_control';");

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
			return db.delete("weight_control", where, args);

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
			bitmap.compress(CompressFormat.PNG, 100, baos); // エラーが表示されるとき
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


	/*
	 * *********************************************************
	 * 最新の体重を取得するメソッド
	 * getmaxData()メソッド 日付順に並べて先頭を取得する
	 * 引数：catID_id
	 * 戻り値：int
	 * *********************************************************
	 */
	public float getMaxData(String catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			String sql = "select weight from weight_control where catID_id = ? order by w_day desc;";
			String[] args = { catID_id  };

			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			float maxD = 0;
			if (c.moveToFirst()) {
				maxD = c.getInt(0);
			}

			// カーソルを閉じる
			c.close();
			c = null;
			
			return maxD;

			// }catch (Exception e){

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
	 * 最新の体重単位を取得するメソッド
	 * getmaxUnit()メソッド 日付順に並べて先頭を取得する
	 * 引数：catID_id
	 * 戻り値：String
	 * *********************************************************
	 */
	public int getMaxUnit(String catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			String sql = "select unit_id from weight_control where catID_id = ? order by w_day desc;";
			String[] args = { catID_id  };

			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			int maxD = 0;
			if (c.moveToFirst()) {
				maxD = c.getInt(0);
			}

			// カーソルを閉じる
			c.close();
			c = null;

			return maxD;

			// }catch (Exception e){

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
		 * 最新の体重単位IDを取得するメソッド
		 * getmaxUnit()メソッド 日付順に並べて先頭を取得する
		 * 引数：catID_id
		 * 戻り値：String
		 * *********************************************************
		 */
		public int getMaxUnitNum(String catID_id) {
			SQLiteDatabase db = null;
			Cursor c = null;
			try {
				// DBを読み込みモードでOPEN
				db = helper.getReadableDatabase();

				String sql = "select unit_id from weight_control where catID_id = ? order by w_day desc;";
				String[] args = { catID_id  };

				c = db.rawQuery(sql, args);

				// 検索結果をArrayListに格納（0〜複数件）
				int maxD = 0;
				if (c.moveToFirst()) {
					maxD = c.getInt(0);
				}

				// カーソルを閉じる
				c.close();
				c = null;

				return maxD;

				// }catch (Exception e){

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
}
