package neko.neko.nekokalte_kalte;

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

public class KalteListView extends Activity implements View.OnClickListener,
		AdapterView.OnItemClickListener, OnItemLongClickListener {
	// ■フィールド設定

	// レイアウト用変数
	private ArrayList<KalteListItem> items2;
	private TextView textView1;
	private Button btn1;
	private Button btn2;
	private ListView list;

	private int magic; // マジックナンバー
	private int catID_id;// 個体識別用ID

	// オプションメニュー用変数
	private final static int MENU_ITEM0 = 0;

	// DB使用宣言
	ArrayList<TblLovecatRecord2> record2;
	private LovecatDao2 dao2;
	private TblLovecatRecord2 r2;

	// フィールド設定終わり

	// ■「onCreat」メソッド開始
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 【レイアウト】XMLとの関連付け→下地
		setContentView(R.layout.kalte_listview_row);

		textView1 = (TextView) findViewById(R.id.textView1);

		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);

		btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(this);


		list = (ListView) findViewById(R.id.listView1);
		reload();

		// マジックナンバーの初期化
		magic = 0;

	}// 「onCreat」メソッド終了

	// ■「onStart」メソッド開始
	@Override
	public void onStart() {
		super.onStart();
		// インテントでデータ取得
		Bundle b = getIntent().getExtras();
		if (b != null) {
			catID_id = b.getInt("catID_id");
		}
		reload();
	}// 「onStart」メソッド終了



	// ■「onItemClick」メソッド開始 …リストビューの項目をクリックした時
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// リストビューの項目がクリックされた時、項目データを取得
		ListView listView = (ListView) parent;
		KalteListItem item = (KalteListItem) listView
				.getItemAtPosition(position);

		// 取得した項目データ（DBの該当レコードID）をパラメーターにセットして、フォームアクティビティを起動
		Intent intent = new Intent(this, neko.neko.nekokalte_kalte.KalteData.class);
		intent.putExtra("k_listId", item.k_listId);
		intent.putExtra("catID_id", catID_id);
		intent.putExtra("key", 12);
		startActivity(intent);


	}// 「onItemClick」メソッド終了

	// 「onClick」メソッド開始
	public void onClick(View v) {
		if (v == btn1) {
			Intent intent = new Intent(this, neko.neko.nekokalte_kalte.KalteData.class);
			intent.putExtra("catID_id", catID_id);
			intent.putExtra("key", 11);
			startActivity(intent);
			finish();

		}else if(v==btn2){
//			Intent intent = new Intent(this,neko.neko.nekokalte_cat.CatMenu.class);
//			intent.putExtra("catID_id", catID_id);
//			startActivity(intent);
			finish();
		}

	}

	// ■「onCreatOptionMenu」メソッド開始 … オプションボタンの設定 P195
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// メニューアイテムの追加「新規登録」
		MenuItem item0 = menu.add(0, MENU_ITEM0, 0, "新規登録");
		item0.setIcon(android.R.drawable.ic_menu_add);
		return true;
	}// 「onCreateOptionMenu」メソッド終了

	// ■ 「onOptionItemsSelected」メソッド開始 …オプションボタンを押したとき「新規」
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ITEM0:
			Intent intent = new Intent(this, neko.neko.nekokalte_kalte.KalteData.class);
			intent.putExtra("catID_id", catID_id);
			intent.putExtra("key", 11);
			startActivity(intent);
			finish();
			return true;
		}
		return true;
	}

	// ■「ローカルメソッド」ダイアログ表示
	private void showDialog(Context context, String title, String text) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 【新規登録】→入力画面
				Intent intent = new Intent(neko.neko.nekokalte_kalte.KalteListView.this, neko.neko.nekokalte_kalte.KalteData.class);
				intent.putExtra("catID_id", catID_id);
				intent.putExtra("key", 11);
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
		final KalteListItem ITEM = (KalteListItem) listView
				.getItemAtPosition(position);

		// ダイアログの表示()
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// メッセージ
		ad.setMessage("削除しますか？");
		// OKを押したとき
		ad.setPositiveButton("削除", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				dao2.delete(ITEM.k_listId);
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

	// reload()
	private void reload() {
		record2 = new ArrayList<TblLovecatRecord2>();// DBの検索結果を受け取るためのインスタンス

		// DBのインスタンスの生成
		dao2 = new LovecatDao2(this);

		record2 = dao2.select("");// DBの全レコードを取得
		if (record2.size() == 0) {
			showDialog(this, "ご案内", "登録されている情報がありません。新規登録画面に移ります。");

		}

		// 【リストビュー】XMLとの関連づけ→詳細
		ListView listView = (ListView) findViewById(R.id.listView1);

		items2 = new ArrayList<KalteListItem>(); // 要素をまとめる袋を準備

		for (int i = 0; i < record2.size(); i++) {
			TblLovecatRecord2 tlr = record2.get(i);
			if (tlr.catID_id == catID_id) {
				KalteListItem item = new KalteListItem(tlr.k_listId, tlr.day,
						tlr.genre, tlr.symptom); // 要素
				items2.add(item);// 袋の中に要素を入れる

			}

		}

		// アダプターのインスタンス生成
		KalteListAdapter adapter = new KalteListAdapter(this, items2); // 袋ごと渡す

		// リストビューに項目データ（アダプタ）をセット
		listView.setAdapter(adapter);

		// リストビューのItemClickListenerをセット
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
}