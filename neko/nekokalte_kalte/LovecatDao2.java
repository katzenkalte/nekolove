package neko.neko.nekokalte_kalte;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import neko.neko.nekokalte.LovecatDbOpenHelper;
import neko.neko.nekokalte_fee.FeeListItem1;
import neko.neko.nekokalte_fee.FeeListItem2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class LovecatDao2 {

	/*
	 * DAO = データベースへのアクセスをまとめたクラス。 このクラスはtblSampleへのアクセスに特化しています。
	 * テーブルが増える毎にそのテーブルにあわせたクラスを作成していきます。 基本機能としては、DBへの追加、更新・削除となります。
	 */
	// DbHelper向けのテーブル作成クエリー（SQL文）を定数化しています。
	public static final String CREATE_TABLE_KALTE = "create table kalte ("
			+ "_id integer primary key autoincrement, " + "day text, "
			+ "year integer, " + "monthOfYear integer, "
			+ "dayOfMonth integer, " + "genre text, " + "symptom text, "
			+ "diagnosis text, " + "prescription text, " + " fee integer, "
			+ "catID_id integer, " + "genreID integer, "
			+ "symptomPhoto1 blob, " + "symptomPhoto2 blob, "
			+ "symptomPhoto3 blob, " + "thumnail1 blob, " + "thumnail2 blob, "
			+ "thumnail3 blob " + ");";

	private LovecatDbOpenHelper helper;

	// コンストラクタ
	public LovecatDao2(Context context) {
		this.helper = new LovecatDbOpenHelper(context);
	}

	/*
	 * *********************************************************
	 * getCurrentRecordData()メソッド カレントレコードのフィールドデータを取得（ローカルメソッド） 引数：Cursor
	 * 戻り値：TblSampleRecord
	 * *********************************************************
	 */
	private TblLovecatRecord2 getCurrentRecordData(Cursor c) {
		// 各フィールドデータを取得

		int k_listId = c.getInt(c.getColumnIndex("_id"));
		int catID_id = c.getInt(c.getColumnIndex("catID_id"));
		int genreID = c.getInt(c.getColumnIndex("genreID"));
		int year = c.getInt(c.getColumnIndex("year"));
		int monthOfYear = c.getInt(c.getColumnIndex("monthOfYear"));
		int dayOfMonth = c.getInt(c.getColumnIndex("dayOfMonth"));
		int fee = c.getInt(c.getColumnIndex("fee"));

		String day = c.getString(c.getColumnIndex("day"));
		String genre = c.getString(c.getColumnIndex("genre"));
		String symptom = c.getString(c.getColumnIndex("symptom"));
		String diagnosis = c.getString(c.getColumnIndex("diagnosis"));
		String prescription = c.getString(c.getColumnIndex("prescription"));


		Bitmap symptomPhoto1 = byteToBitmap(c.getBlob(c
				.getColumnIndex("symptomPhoto1")));
		Bitmap symptomPhoto2 = byteToBitmap(c.getBlob(c
				.getColumnIndex("symptomPhoto2")));
		Bitmap symptomPhoto3 = byteToBitmap(c.getBlob(c
				.getColumnIndex("symptomPhoto3")));

		Bitmap thumnail1 = byteToBitmap(c
				.getBlob(c.getColumnIndex("thumnail1")));
		Bitmap thumnail2 = byteToBitmap(c
				.getBlob(c.getColumnIndex("thumnail2")));
		Bitmap thumnail3 = byteToBitmap(c
				.getBlob(c.getColumnIndex("thumnail3")));

		return new TblLovecatRecord2(k_listId, day, year, monthOfYear,
				dayOfMonth, genre, symptom, diagnosis, prescription, fee,
				catID_id, genreID, symptomPhoto1, symptomPhoto2, symptomPhoto3,
				thumnail1, thumnail2, thumnail3);
	}



	public int getMaxYear(String catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
//			String sql = "select year from kalte group by year desc;";
//			修正＠もん
//			SQL構文を下記に変更しました。
			String sql = "select max(year) from kalte where catID_id = ? group by year order by year desc;";
			String[] args = { catID_id  };

			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			int maxY = 0;
//			while (c.moveToFirst()) {
//				maxY = c.getInt(0);
//			}
//			修正＠もん
//			確実に１件しか必要ないのでIF文に変更しました。
			if (c.moveToFirst()) {
				maxY = c.getInt(0);
			}

			// カーソルを閉じる
			c.close();
			c = null;

			return maxY;

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

//	新規追加メソッド＠もん
//  FeeListViewの動作仕様をみると、全登録年数を取得する必要があるようなので追加。
	public ArrayList<Integer> getYearList(String catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			String sql = "select year from kalte where catID_id = ? group by year order by year desc;";
			String[] args = { catID_id  };

			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (c.moveToNext()) {
				int y = c.getInt(0);
				list.add(y);
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
	 * select()メソッド ラベルフィールドへのあいまい検索結果を抽出 引数：String
	 * 戻り値：ArrayList<TblSampleRecord>
	 * *********************************************************
	 */
	public ArrayList<TblLovecatRecord2> select(String keyword) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			if (keyword == null) {
				keyword = "";
			}

			// 検索条件をセット（SQL）
			String sql = "select * from kalte where day like ? order by day desc;";
			String[] args = { "%" + keyword + "%" };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜複数件）
			ArrayList<TblLovecatRecord2> list = new ArrayList<TblLovecatRecord2>();
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
	public TblLovecatRecord2 getRecord(int id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）
			String sql = "select * from kalte where _id = ?;";
			String[] args = { String.valueOf(id) };
			c = db.rawQuery(sql, args);

			// 検索結果をArrayListに格納（0〜１件）
			TblLovecatRecord2 r = null;
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
			db.delete("kalte", where, null);

			// auto_incrementの値を初期化
			db.execSQL("update sqlite_sequence set seq=1 where name='kalte';");

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
			return db.delete("kalte", where, args);

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

//	/*
//	 * *********************************************************
//	 * feeOfMonth()メソッド ラベルフィールドへのあいまい検索結果を抽出 引数：String
//	 * 戻り値：ArrayList<TblSampleRecord>
//	 *
//	 * 年を指定すると月額のリストを返すメソッド
//	 *
//	 * *********************************************************
//	 */
//	public ArrayList<TblLovecatRecord2> feeOfMonth(String catID_id, String year) {
//		SQLiteDatabase db = null;
//		Cursor c = null;
//		try {
//			// DBを読み込みモードでOPEN
//			db = helper.getReadableDatabase();
//			ArrayList<TblLovecatRecord2> list = new ArrayList<TblLovecatRecord2>();
//			// 検索条件をセット（SQL）
//			String sql = "select catID_id, year, monthOfYear, sum(fee) as fee from kalte group by monthOfYear having catID_id = ? and year = ?;";
//			String[] args = { catID_id, year };
//			c = db.rawQuery(sql, args);
//			while(c.moveToNext()){
//				list.add(getCurrentRecordData(c));
//			}
//			// カーソルを閉じる
//			c.close();
//			c = null;
//
//			return list;
//
//			// }catch (Exception e){
//
//		} finally {
//			// 後始末
//			if (c != null) {
//				c.close();
//			}
//			if (db != null) {
//				db.close();
//			}
//		}
//
//	}

// 変更＠もん
// FeeListViewの使用構成をみると、ここで欲しいデータは「FeeListItem1」型のデータになるので、
// 通常のテーブル構成と集計レコードの構成がかわります。
// 下記にの通りに変更します。

	public ArrayList<FeeListItem1> feeOfMonth(String catID_id, String year) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();
			ArrayList<FeeListItem1> list = new ArrayList<FeeListItem1>();
			// 検索条件をセット（SQL）
			String sql = "select catID_id, year, monthOfYear, sum(fee) as fee from kalte where year = ? and catID_id = ? group by monthOfYear;";
			String[] args = {year, catID_id };
			c = db.rawQuery(sql, args);
			while(c.moveToNext()){
				int moy = c.getInt(c.getColumnIndex("monthOfYear"));
				int fee = c.getInt(c.getColumnIndex("fee"));
				FeeListItem1 item = new FeeListItem1(moy, fee);
				list.add(item);
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



//	/*
//	 * *********************************************************
//	 * feeOfYear()メソッド ラベルフィールドへのあいまい検索結果を抽出 引数：String
//	 * 戻り値：ArrayList<TblSampleRecord>
//	 *
//	 * どの猫かを指定すると年合計のリストを返すメソッド
//	 *
//	 * *********************************************************
//	 */
//	public  ArrayList<TblLovecatRecord2> feeOfYear(String year, String catID_id) {
//		SQLiteDatabase db = null;
//		Cursor c = null;
//		try {
//			// DBを読み込みモードでOPEN
//			db = helper.getReadableDatabase();
//			ArrayList<TblLovecatRecord2> list = new ArrayList<TblLovecatRecord2>();
//			// 検索条件をセット（SQL）
//			String sql = "select sum(fee) as fee from kalte where catID_id = ? group by year desc;";
//			String[] args = { year, catID_id  };
//			while(c.moveToNext()){
//				list.add(getCurrentRecordData(c));
//			}
//			// カーソルを閉じる
//			c.close();
//			c = null;
//
//			return list;
//
//			// }catch (Exception e){
//
//		} finally {
//			// 後始末
//			if (c != null) {
//				c.close();
//			}
//			if (db != null) {
//				db.close();
//			}
//		}
//
//	}

//	変更＠もん
//	FeeListViewの使用構成をみると、ここで欲しいデータは「FeeListItem2」型のデータになるので、
//	通常のテーブル構成と集計レコードの構成がかわります。
//	下記にの通りに変更します。
	public ArrayList<FeeListItem2> feeOfYear(String catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();
			ArrayList<FeeListItem2> list = new ArrayList<FeeListItem2>();
			// 検索条件をセット（SQL）
			String sql = "select catID_id, year, sum(fee) as fee from kalte where catID_id = ? group by Year;";
			String[] args = { catID_id };
			c = db.rawQuery(sql, args);
			while(c.moveToNext()){
				int y = c.getInt(c.getColumnIndex("year"));
				int fee = c.getInt(c.getColumnIndex("fee"));
				FeeListItem2 item = new FeeListItem2(y, fee);
				list.add(item);
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
	 * totalFee()メソッド ラベルフィールドへのあいまい検索結果を抽出 引数：String
	 * 戻り値：ArrayList<TblSampleRecord>
	 *
	 * どの猫かを指定すると、総合計が帰ってくるメソッド
	 *
	 * *********************************************************
	 */
	public int totalFee(String catID_id) {
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			// DBを読み込みモードでOPEN
			db = helper.getReadableDatabase();

			// 検索条件をセット（SQL）

			String sql = "select sum(fee) as fee from kalte where catID_id = ?;";
			String[] args = { catID_id  };
			c = db.rawQuery(sql, args);
			int total=0;
			if (c.moveToNext())
				total=(c.getInt(c.getColumnIndex("fee")));

			// カーソルを閉じる
			c.close();
			c = null;

			return total;

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
