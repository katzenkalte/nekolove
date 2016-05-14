package neko.neko.nekokalte_fee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import neko.neko.nekokalte.R;
import neko.neko.nekokalte_cat.LovecatDao;
import neko.neko.nekokalte_cat.TblLovecatRecord;
import neko.neko.nekokalte_kalte.LovecatDao2;
import neko.neko.nekokalte_kalte.TblLovecatRecord2;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FeeListView extends Activity implements View.OnClickListener,
		AdapterView.OnItemClickListener {
	// ■フィールド設定

	// レイアウト用変数
	private ArrayList<FeeListItem1> items2_1;
	private ArrayList<FeeListItem2> items2_2;

	private TextView txtname;// 名前
	private TextView txtV2;// 「ちゃんにかかった診療費」
	private TextView txtV3;// 「総計」
	private TextView txttotal;// 総計
	private TextView txtV5;// 「円」
	private TextView txtYear;// 年
	private TextView txtV7;// 「年 月集計」
	private TextView txtV8;// 「年集計」
	private Button btn1;// 「→年集計」
	private Button btn2;// 「→月集計」
	private Button btn3;// 「→前の年」
	private Button btn4;// 「→次の年」
	private ListView list1;// 「月額が出るリストビュー」
	private ListView list2;// 「年額が出るリストビュー」

	//画面遷移用変数
	private int magic;
	private int catID_id;// 個体識別用ID

//	新規追加＠もん
//	年の管理用
	private int year;//直近の年
	private ArrayList<Integer> yearList;//年移動
	private int currentYearIndex;//年の位置

	// DB使用宣言
	ArrayList<TblLovecatRecord2> record2;
	ArrayList<Integer> total;
	private LovecatDao dao1;
	private LovecatDao2 dao2;
	private TblLovecatRecord2 r2;
	private TblLovecatRecord r1;

	// フィールド設定終わり

	// ■「onCreate」メソッド開始
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fee);

		// 【レイアウト】XMLとの関連付け→下地
		txtname = (TextView) findViewById(R.id.textView1);// 名前
		txtV2 = (TextView) findViewById(R.id.textView2);// 「ちゃんにかかった診療費」
		txtV3 = (TextView) findViewById(R.id.textView3);// 「総計」
		txttotal = (TextView) findViewById(R.id.textView4);// 総計
		txtV5 = (TextView) findViewById(R.id.textView5);// 「円」
		txtYear = (TextView) findViewById(R.id.textView6);// 年
		txtV7 = (TextView) findViewById(R.id.textView7);// 「年 月集計」
		txtV8 = (TextView) findViewById(R.id.textView8);// 「年集計」

		list1 = (ListView) findViewById(R.id.monthlistView);// 「月額が出るリストビュー」
		list2 = (ListView) findViewById(R.id.yearlistView);// 「年額が出るリストビュー」

		btn1 = (Button) findViewById(R.id.button1);// 「→年集計」
		btn1.setOnClickListener(this);

		btn2 = (Button) findViewById(R.id.button2);// 「→月集計」
		btn2.setOnClickListener(this);

		btn3 = (Button) findViewById(R.id.button3);// 「→前の年」
		btn3.setOnClickListener(this);

		btn4 = (Button) findViewById(R.id.button4);// 「→次の年」
		btn4.setOnClickListener(this);



		// マジックナンバーの初期化
		magic = 0;

	}// 「onCreat」メソッド終了

	// ■「onStart」メソッド開始
	//個体情報およびカルテ情報が入力されたとき成立する項目
	@Override
	public void onStart() {
		super.onStart();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			//個体識別情報の取得
			catID_id = b.getInt("catID_id");

			//個体識別情報から名前を取得+表示
			dao1 = new LovecatDao(this);
			r1 = dao1.getRecord(catID_id);
			txtname.setText(r1.name);

			//カルテで入力された総額を取得+表示
			dao2=new LovecatDao2(this);				//①Daoのインスタンス生成、DBを使える状態にする
			int total=dao2.totalFee(""+catID_id);	//②個体識別番号からトータルを計算するメソッド呼び出し
			txttotal.setText(""+total);				//③総額を表示

			//Kalteテーブルからyearの項目を抜粋し、最大値を求める
			year=dao2.getMaxYear(catID_id+"");

			/*
			 * ★ モンプラス
			 * 年移動を管理するための情報取得設定
			 * 取得した直近の年が0以上のとき、個体IDから登録された年数を取得する
			 * currentYearIndexで配列の位置を取得。変数=0の0は一番初め[0]のこと。
			 * 年の登録で取得したいのは最新年なので常に配列の0番目だから。
			 *
			 * */
			if (year > 0) {
				yearList = dao2.getYearList(catID_id+"");
				currentYearIndex = 0;
			} else {
				year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
			}

			/*
			 * ★ モンプラス
			 * LovecatDao2.feeOfMonth()メソッドの構成を変更および、
			 * 年毎の集計を行う為に、monthList()メソッドにリストの更新記述を移動。
			*/
			monthList();
		}
	}// 「onStart」メソッド終了

	// ■「onItemClick」メソッド開始 …リストビューの項目をクリックした時
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ただの表示にするのでクリックできないように設定しておく
	}// 「onItemClick」メソッド終了

	// 「onClick」メソッド開始
	public void onClick(View v) {
		// 【年表示メソッド】
		if (v == btn1) {

//			変更＠もん
//			LovecatDao2.feeOfYear()メソッドの構成を変更したので、
//			下記の通りに変更。

			items2_2 = dao2.feeOfYear(catID_id+"");// DBから月額を抜粋したものを取得
			if (items2_2.size() == 0) {
				showDialog(this, "ご案内", "登録されている情報がありません。先にカルテの金額の項目を入力してください。");
			}

			// アダプターのインスタンス生成
			FeeListAdapter2 adapter = new FeeListAdapter2(this, items2_2); // 袋ごと渡す

			// リストビューに項目データ（アダプタ）をセット
			list2.setAdapter(adapter);

			// リストビューのItemClickListenerをセット
			list2.setOnItemClickListener(this);

			//年合計で表示するものをセット
			yearList();
		}

		// 【月表示メソッド】
		if (v == btn2) {
			monthList();
		}

		//登録年数がnullでないとき…
		if (yearList != null) {

			// 【前の年 表示メソッド】
			if (v == btn3) {
				int y = currentYearIndex + 1;
				if (y < yearList.size()) {
					currentYearIndex = y;
					year = yearList.get(currentYearIndex);
					monthList();
				}
			}

			// 【次の年 表示メソッド】
			if (v == btn4) {
				int y = currentYearIndex - 1;
				if (y >= 0) {
					currentYearIndex = y;
					year = yearList.get(currentYearIndex);
					monthList();
				}
			}
		}


	}

	// 【ローカルメソッド 月集計】monthList()
	public void monthList() {
		//表示・非表示の設定
		txtname.setVisibility(View.VISIBLE);// 名前
		txtV2.setVisibility(View.VISIBLE);// 「ちゃんにかかった診療費」
		txtV3.setVisibility(View.VISIBLE);// 「総計」
		txttotal.setVisibility(View.VISIBLE);// 総計
		txtV5.setVisibility(View.VISIBLE);// 「円」
		txtYear.setVisibility(View.VISIBLE);// 年
		txtV7.setVisibility(View.VISIBLE);// 「年 月集計」
		txtV8.setVisibility(View.GONE);// 「年集計」

		list1.setVisibility(View.VISIBLE);// 「月額が出るリストビュー」
		list2.setVisibility(View.GONE);// 「年額が出るリストビュー」

		btn1.setVisibility(View.VISIBLE);// 「→年集計」
		btn2.setVisibility(View.GONE);// 「→月集計」
		btn3.setVisibility(View.VISIBLE);// 「→前の年」
		btn4.setVisibility(View.VISIBLE);// 「→次の年」

		/*
		 * ★ モンプラス
		 * 直近、前の年、次の年、それぞれ１年間の月額を表示させる都度、
		 * リストビューを更新する
		 */

		//どの年の月額を表示させるか、その該当年を設定
		txtYear.setText(""+year);

		// 該当年の月集計データを取得
		items2_1 = dao2.feeOfMonth(catID_id+"",year+"");// DBから月額を抜粋したものを取得
		if (items2_1.size() == 0) {
			showDialog(this, "ご案内", "登録されている情報がありません。先にカルテの金額の項目を入力してください。");
		}

		// アダプターのインスタンス生成
		FeeListAdapter adapter = new FeeListAdapter(this, items2_1); // 袋ごと渡す

		// リストビューに項目データ（アダプタ）をセット
		list1.setAdapter(adapter);

		// リストビューのItemClickListenerをセット
		list1.setOnItemClickListener(this);
	}

	// 【ローカルメソッド 年合計】yearList()
	public void yearList() {
		//表示・非表示の設定
		txtname.setVisibility(View.VISIBLE);// 名前
		txtV2.setVisibility(View.VISIBLE);// 「ちゃんにかかった診療費」
		txtV3.setVisibility(View.VISIBLE);// 「総計」
		txttotal.setVisibility(View.VISIBLE);// 総計
		txtV5.setVisibility(View.VISIBLE);// 「円」
		txtYear.setVisibility(View.GONE);// 年
		txtV7.setVisibility(View.GONE);// 「年 月集計」
		txtV8.setVisibility(View.VISIBLE);// 「年集計」

		list1.setVisibility(View.GONE);// 「月額が出るリストビュー」
		list2.setVisibility(View.VISIBLE);// 「年額が出るリストビュー」

		btn1.setVisibility(View.GONE);// 「→年集計」
		btn2.setVisibility(View.VISIBLE);// 「→月集計」
		btn3.setVisibility(View.GONE);// 「→前の年」
		btn4.setVisibility(View.GONE);// 「→次の年」

	}


	// ■「ローカルメソッド」ダイアログ表示
	public void showDialog(Context context, String title, String text) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		ad.show();

	}


}
