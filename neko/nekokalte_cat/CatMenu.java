package neko.neko.nekokalte_cat;

import neko.neko.nekokalte.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CatMenu extends Activity implements View.OnClickListener {
	// フィールド設定
	private TextView title; // タイトル
	private TextView mes; // タイトルメッセージ
	private Button button1; // 個体情報
	private Button button2; // カルテ
	private Button button3; // 体重管理・健康チェック
	private Button button4; // お知らせ・会計
	private Button button5;	//一覧に戻る

	// マジックナンバー
	private int magic;

	// DB使用宣言
	private int catID_id;
	private LovecatDao dao;
	private TblLovecatRecord r;


	// フィールド設定終わり

	// 「onCreat」メソッド開始
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// レイアウト設定
		setContentView(R.layout.menu);

		// 【テキストビュー】XMLとの関連付け
		title = (TextView) findViewById(R.id.textView1);
		mes = (TextView) findViewById(R.id.textView2);

		// 【ボタン】XMLとの関連付け+クリックの有効化
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);

		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);

		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);

		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(this);

		button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(this);

		//マジックナンバーの初期化
		magic = 0;


	}// 「onCreat」メソッド終了


	//「onStart」tメソッド開始
	@Override
	protected void onStart() {
		super.onStart();
		// インテントでデータ取得
		Bundle b = getIntent().getExtras();
		if (b != null) {
			magic=b.getInt("key");
			catID_id = b.getInt("catID_id");
		}

	}//「onStar」tメソッド終了


	// 「onClick」メソッド開始
	public void onClick(View v) {

		// 個体情報画面
		if (v == button1) {
				Intent intent = new Intent(this, neko.neko.nekokalte_cat.CatData.class);
				intent.putExtra("catID_id", catID_id);
				intent.putExtra("key", 2);
				startActivity(intent);
		}

		// カルテ画面
		if (v == button2) {
				Intent intent = new Intent(this, neko.neko.nekokalte_kalte.KalteListView.class);
				intent.putExtra("catID_id", catID_id);
				intent.putExtra("key", 12);
				startActivity(intent);
		}

		// 体重管理画面
		if (v == button3) {
			Intent intent = new Intent(this, neko.neko.nekokalte_weight.WeightListView.class);
			intent.putExtra("catID_id", catID_id);
			startActivity(intent);
		}

		// 会計画面
		if (v == button4) {
			Intent intent = new Intent(this, neko.neko.nekokalte_fee.FeeListView.class);
			intent.putExtra("catID_id", catID_id);
			intent.putExtra("key", 31);
			startActivity(intent);
		}

		//一覧に戻る
		if(v==button5){
		finish();
		}// 「onClick」メソッド終了
	}
}
