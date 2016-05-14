package neko.neko.nekokalte_cat;

import java.util.ArrayList;

import neko.neko.nekokalte.R;
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

/*
 *リスト項目を選択した時（クリックした時）のイベント及び要素情報の取得を追加
 * 追加要素
 * AdapterView.OnItemClickListenerインターフェースを実装。
 * （void onItemClick(AdapterView<?> parent, View view, int position, long id)）
 */

public class CatListView extends Activity implements View.OnClickListener,
		AdapterView.OnItemClickListener, OnItemLongClickListener {
	// ■フィールド設定

	// レイアウト用変数
	private TextView titel;
	private Button btn1;
	private ListView list;

	private ArrayList<CatListItem> items;

	// マジックナンバー
	private int magic;

	// オプションメニュー用変数
	private final static int MENU_ITEM0 = 0;

	// DB使用宣言
	ArrayList<TblLovecatRecord> record;
	private LovecatDao dao;
	private TblLovecatRecord r;

	//
	ListView listView;

	// フィールド設定終わり

	// ■「onCreat」メソッド開始
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 【レイアウト】XMLとの関連付け→下地
		setContentView(R.layout.listview_row);
		titel = (TextView) findViewById(R.id.textView1);
		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		list = (ListView) findViewById(R.id.listView1);

		reload();

		// リストビューのItemClickListenerをセット
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);

		// マジックナンバーの初期化
		magic = 0;

	}// 「onCreat」メソッド終了


	@Override
	protected void onStart() {
		super.onStart();
		reload();

	}

	// 「reload」メソッド開始
	private void reload() {
		record = new ArrayList<TblLovecatRecord>();// DBの検索結果を受け取るためのインスタンス

		// DBのインスタンスの生成
		dao = new LovecatDao(this);

		record = dao.select("");// DBの全レコードを取得
		if (record.size() == 0) {
			showDialog(this, "ご案内", "登録されている情報がありません。新規登録画面に移ります。");
		}

		// 【リストビュー】XMLとの関連づけ→詳細
		listView = (ListView) findViewById(R.id.listView1);

		items = new ArrayList<CatListItem>(); // 要素をまとめる袋を準備

		for (int i = 0; i < record.size(); i++) {
			TblLovecatRecord tlr = record.get(i);
			CatListItem item = new CatListItem(tlr.catID_id, tlr.thumnail, tlr.name); // 要素
			items.add(item);// 袋の中に要素を入れる

		}

		// アダプターのインスタンス生成
		CatListAdapter adapter = new CatListAdapter(this, items); // 袋ごと渡す

		// リストビューに項目データ（アダプタ）をセット
		listView.setAdapter(adapter);

	}// 「reload」メソッド

	// 「onClick」メソッド開始
	public void onClick(View v) {

		// 【新規登録】→入力画面
		Intent intent = new Intent(neko.neko.nekokalte_cat.CatListView.this, neko.neko.nekokalte_cat.CatData.class);
		intent.putExtra("key", 1);
		startActivity(intent);
		finish();

	}// 「onClick」メソッド終了

	// ■「onItemClick」メソッド開始 …リストビューの項目をクリックした時
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// リストビューの項目がクリックされた時、項目データを取得
		ListView listView = (ListView) parent;
		CatListItem item = (CatListItem) listView.getItemAtPosition(position);

		// 取得した項目データ（DBの該当レコードID）をパラメーターにセットして、フォームアクティビティを起動
		Intent intent = new Intent(this, neko.neko.nekokalte_cat.CatMenu.class);
		intent.putExtra("catID_id", item.catID_id);
		startActivity(intent);

	}// 「onItemClick」メソッド終了

	// ■「onCreatOptionMenu」メソッド開始 … オプションボタンの設定 P195
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// メニューアイテムの追加「新規登録」
		MenuItem item0 = menu.add(0, MENU_ITEM0, 0, "新規登録");
		item0.setIcon(android.R.drawable.ic_menu_add);

		return true;
	}// 「onCreateOptionMenu」メソッド終了

	// ■ 「onOptionItemsSelected」メソッド開始 …オプションボタンを押したとき
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ITEM0:
			Intent intent = new Intent(this, neko.neko.nekokalte_cat.CatData.class);
			intent.putExtra("key", 1);
			startActivity(intent);
			return true;
		}
		return true;
	}

	// ■「ローカルメソッド」ダイアログ表示
	public void showDialog(Context context, String title, String text) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 【新規登録】→入力画面
				Intent intent = new Intent(neko.neko.nekokalte_cat.CatListView.this, neko.neko.nekokalte_cat.CatData.class);
				intent.putExtra("key", 1);
				startActivity(intent);
				finish();
			}
		});
		ad.show();

	}

	// 長押し：削除確認(ダイアログ)→削除(実行/キャンセル)
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		// クリックした項目情報を取得する
		ListView listView = (ListView) parent;
		final CatListItem ITEM = (CatListItem) listView.getItemAtPosition(position);

		// ダイアログの表示()
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// メッセージ
		ad.setMessage("削除しますか？");
		// OKを押したとき
		ad.setPositiveButton("削除", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				dao.delete(ITEM.catID_id);
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
}