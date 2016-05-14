package neko.neko.nekokalte_weight;

import java.util.ArrayList;

import neko.neko.nekokalte.R;
import neko.neko.nekokalte_cat.LovecatDao;
import neko.neko.nekokalte_cat.TblLovecatRecord;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class WeightListView extends Activity implements View.OnClickListener,
		AdapterView.OnItemClickListener, OnItemLongClickListener {

	// ■フィールド設定

	// XML関連付け用変数
	private TextView name;// 名前→catIDテーブルから呼び出し
	private TextView txtV2;// 「ちゃんの体重」

	private Button btn1;// 新規追加ボタン

	private ListView listView;
	WeightListAdapter adapter;
	private ArrayList<WeightListItem> items3;

	private int catID_id;// 個体識別用ID
	private static final int REQUEST_MAIN = 1001;// リクエストコード

	// DB使用宣言
	private LovecatDao dao1;
	private TblLovecatRecord r1;

	ArrayList<TblLovecatRecord3> record3;
	private LovecatDao3 dao3;
	private TblLovecatRecord3 r3;

	// オプションメニュー用変数
	private final static int MENU_ITEM0 = 0;
	private final static int MENU_ITEM1 = 1;

	// フィールド設定終わり

	// 「onCreate」メソッド開始
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 【レイアウト】XMLとの関連付け→下地
		setContentView(R.layout.weightlistview);

		name = (TextView) findViewById(R.id.textView1);
		txtV2 = (TextView) findViewById(R.id.textView2);

		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listView1);

	}// 「onCreat」メソッド終了

	// 「onStart」メソッド開始
	@Override
	protected void onStart() {
		super.onStart();
		// インテントcatIDデータを取得
		Bundle b = getIntent().getExtras();
		if (b != null) {
			catID_id = b.getInt("catID_id");
		}

		//catID から名前を取得、セット
		dao1 = new LovecatDao(this);
		r1 = dao1.getRecord(catID_id);
		name.setText(r1.name);


		//リストの生成
		reload();

	}// 「onStart」メソッド終了


	// 「onClick」メソッド開始
	public void onClick(View v) {
		if (v == btn1) {
			Intent intent = new Intent(this, neko.neko.nekokalte_weight.WeightControl.class);
			intent.putExtra("catID_id", catID_id);
			intent.putExtra("key", 21);
			startActivityForResult(intent, REQUEST_MAIN);
		}
	}// 「onClick」メソッド開始


	//「onItemClick」メソッド開始
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// リストビューの項目がクリックされた時、項目データを取得
		ListView listView = (ListView) parent;
		WeightListItem item = (WeightListItem) listView
				.getItemAtPosition(position);

		Intent intent = new Intent(this, neko.neko.nekokalte_weight.WeightControl.class);
		intent.putExtra("catID_id", catID_id);
		intent.putExtra("weight_Id",item.weight_Id);
		intent.putExtra("key", 22);
		startActivityForResult(intent, REQUEST_MAIN);
	}//「onItemClick」メソッド終了



	//★「reload」メソッド
	private void reload() {
		record3 = new ArrayList<TblLovecatRecord3>();// DBの検索結果を受け取るためのインスタンス

		// DBのインスタンスの生成
		dao3 = new LovecatDao3(this);

		record3 = dao3.select("");// DBの全レコードを取得
		if (record3.size() == 0) {
			showDialog(this, "ご案内", "登録されている情報がありません。新規登録画面に移ります。");

		}

		// 【リストビュー】XMLとの関連づけ→詳細
		// ListView listView = (ListView) findViewById(R.id.listView1);

		items3 = new ArrayList<WeightListItem>(); // 要素をまとめる袋を準備
		for (int i = 0; i < record3.size(); i++) {
			TblLovecatRecord3 tlr = record3.get(i);
		if (tlr.catID_id == catID_id) {
				WeightListItem item = new WeightListItem(tlr.weight_Id,
						tlr.w_day, tlr.weight, tlr.unit); // 要素
				items3.add(item);// 袋の中に要素を入れる
			}

		}

		// アダプターのインスタンス生成
		adapter = new WeightListAdapter(this, items3); // 袋ごと渡す

		// リストビューに項目データ（アダプタ）をセット
		listView.setAdapter(adapter);

		// リストビューのItemClickListenerをセット
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	// showDialog()メソッド
	// ■「ローカルメソッド」ダイアログ表示
	public void showDialog(Context context, String title, String text) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 【新規登録】→入力画面
				Intent intent = new Intent(neko.neko.nekokalte_weight.WeightListView.this,
						WeightControl.class);
				intent.putExtra("catID_id", catID_id);
				intent.putExtra("key", 21);
				startActivityForResult(intent, REQUEST_MAIN);
			}
		});
		ad.show();

	}

	// 長押し：削除確認(ダイアログ)→削除(実行/キャンセル)
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		// クリックした項目情報を取得する
		ListView listView = (ListView) parent;
		final WeightListItem ITEM = (WeightListItem) listView
				.getItemAtPosition(position);

		// ダイアログの表示()
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// メッセージ
		ad.setMessage("削除しますか？");
		// OKを押したとき
		ad.setPositiveButton("削除", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				dao3.delete(ITEM.weight_Id);
				reload();
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

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == REQUEST_MAIN && resultCode == Activity.RESULT_OK) {
			Bundle b = intent.getExtras();
			catID_id = b.getInt("catID_id");

		}
		reload();
	}

	// ■「onCreatOptionMenu」メソッド開始 … オプションボタンの設定 P195
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// メニューアイテムの追加「新規登録」
		MenuItem item0 = menu.add(0, MENU_ITEM0, 0, "新規登録");
		item0.setIcon(android.R.drawable.ic_menu_add);

		MenuItem item1 = menu.add(0, MENU_ITEM1, 0, "全消去");
		item1.setIcon(android.R.drawable.ic_menu_delete);

		return true;
	}// 「onCreateOptionMenu」メソッド終了

	// ■ 「onOptionItemsSelected」メソッド開始 …オプションボタンを押したとき「新規」
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ITEM0:
			Intent intent = new Intent(this, neko.neko.nekokalte_weight.WeightControl.class);
			intent.putExtra("catID_id", catID_id);
			intent.putExtra("key", 21);
			startActivityForResult(intent, REQUEST_MAIN);

			return true;

		case MENU_ITEM1:
			// ダイアログの表示()
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
			// メッセージ
			ad.setMessage("全て削除しますか？");
			// OKを押したとき
			ad.setPositiveButton("全削除", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dao3.allDelete();
					reload();
				}
			});
			// キャンセルを押したとき
			ad.setNegativeButton("キャンセル",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// ダイアログを閉じる。
							dialog.cancel();
						}
					});
			ad.show();

			break;
		}
		return true;
	}

}
