package neko.neko.nekokalte;

import neko.neko.nekokalte_cat.LovecatDao;
import neko.neko.nekokalte_kalte.LovecatDao2;
import neko.neko.nekokalte_weight.LovecatDao3;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LovecatDbOpenHelper extends SQLiteOpenHelper {

	// データベース情報
	private final static String DB = "lovecat.db"; // データベースのファイル名
	private final static int DB_VERSION = 1; // 作成したデータベースの管理番号（更新毎に数値を増やす）

	// コンストラクタ
	public LovecatDbOpenHelper(Context context) {
		super(context, DB, null, DB_VERSION);
	}

	// データベースファイルが見つからない時（初めて使用する時）に呼ばれるメソッド。
	@Override
	public void onCreate(SQLiteDatabase db) {
		// データベースを作成する。
		// テーブル作成クエリー（SQL）を実行。
		db.execSQL(LovecatDao.CREATE_TABLE_CAT_ID);
		db.execSQL(LovecatDao2.CREATE_TABLE_KALTE);
		db.execSQL(LovecatDao3.CREATE_TABLE_WEIGHT);
	}

	// 作成したデータベースの管理番号（version）が変更した時に呼ばれるメソッド。
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 今回は使用しません。
	}
}
