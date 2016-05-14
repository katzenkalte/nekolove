package neko.neko.nekokalte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import neko.neko.nekokalte_cat.CsvOperation1;
import neko.neko.nekokalte_cat.LovecatDao;
import neko.neko.nekokalte_cat.TblLovecatRecord;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TopMenu extends Activity implements View.OnClickListener {
	// ■フィールドの設定
	private TextView title; // 「猫カルテ」
	private Button btn1; // 「新規登録」ボタン
	private Button btn2; // 「猫リスト」ボタン
	private Button btn3; // 「バックアップ」ボタン
	private Button btn4; // 「バックアップデータの読み込み」ボタン

	// DBデータを扱う変数
	private LovecatDao dao;

	// csvを操作するクラス
	CsvOperation1 c1;
	// データ書き込み用の配列
	String[] dataForCatdata;
	// csvファイルから読み込み用
	ArrayList<TblLovecatRecord> ar1;
	TblLovecatRecord csvRec1;

	// フィールドの設定終わり

	// ■「onCreat」メソッド開始
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 【レイアウト】XMLとの関連付け
		setContentView(R.layout.topmenu);

		// 【テキストビュー】XMLとの関連付け
		title = (TextView) findViewById(R.id.textView1);

		// 【ボタン】XMLとの関連付け+クリックの有効化
		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(this);
		btn3 = (Button) findViewById(R.id.button3);
		btn3.setOnClickListener(this);
		btn4 = (Button) findViewById(R.id.button4);
		btn4.setOnClickListener(this);

	}

	// 「onCreate」メソッド終了

	// ■「onClick」メソッド開始
	public void onClick(View v) {
		if (v == btn1) {
			// 【新規登録】→入力画面
			Intent intent = new Intent(this,
					neko.neko.nekokalte_cat.CatData.class);// 第2引数に呼び出したいクラスを指定
			intent.putExtra("key", 1);
			startActivity(intent); // 呼び出した先のActivityを実行

		}

		if (v == btn2) {
			// 【猫リスト】→リストビュー画面
			Intent intent = new Intent(this,
					neko.neko.nekokalte_cat.CatListView.class); // 第2引数に呼び出したいクラスを指定
			startActivity(intent); // 呼び出した先のActivityを実行
		}

		// 【アプリの終了】DB→SDカード
		if (v == btn3) {

			// ダイアログの表示()
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			// メッセージ
			ad.setMessage("バックアップを取りますか？");
			// OKを押したとき
			ad.setPositiveButton("はい", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Toast.makeText(TopMenu.this, "SDカードにデータのバックアップを取っています。", Toast.LENGTH_LONG)
					.show();
					try {
						String DB_NAME = "lovecat.db";
						copyDb2Sd(TopMenu.this, DB_NAME);
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					Toast.makeText(TopMenu.this, "バックアップを取りました。", Toast.LENGTH_LONG).show();
				}
			});
			// キャンセルを押したとき
			ad.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					// ダイアログを閉じる。
					dialog.cancel();
				}
			});
			ad.show();

		}
		if (v == btn4) {
			// 【ロード】→//SDカード→DB

			// ダイアログの表示()
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			// メッセージ
			ad.setMessage("バックアップを読み込みますか？");
			// OKを押したとき
			ad.setPositiveButton("はい", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					Toast.makeText(TopMenu.this, "SDカードに保存したデータバックアップを読み込みます",
					Toast.LENGTH_LONG).show();
					streamDB();
					Toast.makeText(TopMenu.this, "読み込みが終了しました。", Toast.LENGTH_LONG).show();
				}
			});
			// キャンセルを押したとき
			ad.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					// ダイアログを閉じる。
					dialog.cancel();
			}
		});
		ad.show();
		}
	}

	// ■DB→SDカード(ローカルメソッド)
	public boolean copyDb2Sd(Context context, String DB_NAME)
			throws IOException {
		String TAG = "copyDb2Sd";//
		// SDカードの保存先パスを取得
		String pathSd = new StringBuilder()
				.append(Environment.getExternalStorageDirectory().getPath())
				.append("/").append(this.getPackageName()).toString();

		File filePathToSaved = new File(pathSd);

		if (!filePathToSaved.exists() && !filePathToSaved.mkdirs()) {
			throw new IOException("FAILED_TO_CREATE_PATH_ON_SD");
		}

		String fileDb = this.getDatabasePath(DB_NAME).getPath();
		String fileSd = new StringBuilder().append(pathSd).append("/")
				.append(DB_NAME)// DB名
				.toString();

		FileChannel channelSource = new FileInputStream(fileDb).getChannel();
		FileChannel channelTarget = new FileOutputStream(fileSd).getChannel();

		channelSource.transferTo(0, channelSource.size(), channelTarget);

		channelSource.close();
		channelTarget.close();

		return true;
	}

	// ■SDカード→DB（ローカルメソッド）
	public void streamDB() {
		try {
			final String DB_NAME = "lovecat.db";
			// 既存データベースを削除
			this.deleteDatabase(DB_NAME);
			// コピー元パス（SDカード）
			String pathFrom = Environment.getExternalStorageDirectory()
					.getPath()
					+ "/"
					+ this.getPackageName().toString()
					+ "/"
					+ DB_NAME;
			// コピー先パス（データベースフォルダ）
			String pathTo = this.getDatabasePath(DB_NAME).getPath();

			// コピー
			FileInputStream fis = new FileInputStream(pathFrom);
			FileChannel channelFrom = fis.getChannel();
			FileOutputStream fos = new FileOutputStream(pathTo);
			FileChannel channeTo = fos.getChannel();
			try {
				channelFrom.transferTo(0, channelFrom.size(), channeTo);
			} finally {
				fis.close();
				channelFrom.close();
				fos.close();
				channeTo.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 戻るボタンを押したときの処理
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// アラートダイアログ
			showDialog(0);
			return true;
		}
		return false;
	}

	// アラートダイアログ
	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {

		case 0:
			// ダイアログの作成(AlertDialog.Builder)
			return new AlertDialog.Builder(TopMenu.this)
					.setMessage("「アプリ」を終了しますか?")
					.setCancelable(false)
					// 「終了する」が押された時の処理
					.setPositiveButton("終了する",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// アクティビティ消去
									TopMenu.this.finish();
								}
							})
					// 「終了しない」が押された時の処理
					.setNegativeButton("終了しない",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							}).create();
		}
		return null;
	}
}