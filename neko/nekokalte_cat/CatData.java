package neko.neko.nekokalte_cat;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import neko.neko.nekokalte.R;
import neko.neko.nekokalte_weight.LovecatDao3;
import neko.neko.nekokalte_weight.TblLovecatRecord3;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ●体重の入力に関してはDBのWeightテーブルと連結させておく。
 * ●スピナーデータはラジオボタンに変更
 *
 * フィールド
 * 		ArrayList<TblLovecatRecord3> record3;
 *	 	private LovecatDao3 dao3;
 * 		private TblLovecatRecord3 r3;
 *
 * onStart
 * ★体重管理のリストビューの最新情報を個体情報のエディットテキストに反映させたい
 * 		magic==11のとき、セットなし
 * 		magic==12のとき、セットあり
 * 			セットするデータは最新順に並べた一番目の体重データr3.weight
 * 			ラジオボタンのデータをスピナーにセット？？
 *
 * onClick
 * 	【エディットボックスを選択したとき】
 * 		インテントでWeightControlを呼び出し、
 * 		戻り値あり…データをエディットボックスにセット
 *
 * 	【新規保存ボタンを押したとき】
 * 		weightテーブルにデータを保存。
 * 		inseret(weight,value);
 * 	【上書き保存ボタンを押したとき】
 * 		updata(weight,value,catID_id);
 *
 *
 *
 * */

public class CatData extends Activity implements View.OnClickListener {
	// private static final String TAG = "CatData"; 何のタグ？？
	// ■フィールドの設定 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	private Uri uri; // 選択した画像データのID
	private Bitmap bmpThumnail; // 取得したサムネイル画像
	private Bitmap bmpFacePhoto; // Bitmap変換後の加工済み写真
	private final static int REQUEST_GALLERY = 1; // 写真データ取得用

	// XML関連付け用変数
	private EditText edtName; // 名前入力
	private EditText edtAge; // 年齢入力
	private EditText edtWeight; // 体重入力
	private EditText edtHairColor; // 猫種入力

	private Spinner selectSpinner1; // 性別選択スピナー
	private Spinner selectSpinner2; // 年齢選択スピナー
	private Spinner selectSpinner3; // 体重選択スピナー

	private TextView txtPhoto_title; // 「写真」
	private ImageView imgImageView; // 画像表示

	private TextView txtName_title; // 「名前」
	private TextView txtName; // 入力された名前の表示

	private TextView txtSex_title; // 「性別」
	private TextView txtSex; // 選択された性別

	private TextView txtAge_title; // 「年齢」
	private TextView txtAge; // 入力された年齢の表示
	private TextView txtAgeUnit; // 年齢単位選択スピナーのテキストデータ

	private int w_day;// 直近の体重
	private ArrayList<Integer> w_day_List;// 日付順に並べる
	private int current_w_day_Index;// 最新の日付位置

	private TextView txtWeight_title; // 「体重」
	private TextView txtWeight; // 入力された体重の表示
	private TextView txtWeightUnit; // 体重単位選択スピナーのテキストデータ

	private TextView txtHairColor_title; // 「猫種」
	private TextView txtHairColor; // 入力された種類の表示意

	private ImageButton imgImageButton1;// 写真選択ボタン
	private Button btn1; // 入力完了ボタン
	private Button btn2; // 再編集する場合のボタン
	private Button btn3; // 保存するボタン

	private int magic; // マジックナンバー

	// DB用変数
	private int catID_id;

	private LovecatDao dao;
	private TblLovecatRecord r1;

	private LovecatDao3 dao3;
	private TblLovecatRecord3 r3;

	boolean flag;

	// フィールドの設定終わり ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onCreate」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 【レイアウト】XMLとの関連付け
		setContentView(R.layout.catdatainput);

		// 【テキストビュー】XMLとの関連付け タイトル
		txtPhoto_title = (TextView) findViewById(R.id.txtV1);
		txtName_title = (TextView) findViewById(R.id.txtV2);
		txtSex_title = (TextView) findViewById(R.id.txtV4);
		txtAge_title = (TextView) findViewById(R.id.txtV6);
		txtWeight_title = (TextView) findViewById(R.id.txtV9);
		txtHairColor_title = (TextView) findViewById(R.id.txtV12);

		// 【テキストビュー】XMLとの関連付け 確認データ
		txtName = (TextView) findViewById(R.id.txtV3); // 入力された名前
		txtSex = (TextView) findViewById(R.id.txtV5); // 選択された性別
		txtAge = (TextView) findViewById(R.id.txtV7); // 入力された年齢
		txtAgeUnit = (TextView) findViewById(R.id.txtV8); // 選択された年齢単位
		txtWeight = (TextView) findViewById(R.id.txtV10); // 入力された体重
		txtWeightUnit = (TextView) findViewById(R.id.txtV11); // 選択された体重単位
		txtHairColor = (TextView) findViewById(R.id.txtV13); // 入力された種類

		// 【エディットテキスト】XMLとの関連付け
		edtName = (EditText) findViewById(R.id.editText1);
		edtAge = (EditText) findViewById(R.id.editText2);
		edtWeight = (EditText) findViewById(R.id.editText3);
		edtHairColor = (EditText) findViewById(R.id.editText4);

		// 【スピナー】デザインの指定
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);

		// 【スピナー】項目の追加
		// 性別の選択肢
		adapter1.add("オス");
		adapter1.add("メス");
		adapter1.add("去勢済み（オス）");
		adapter1.add("避妊済み（メス）");

		// 年齢の選択肢
		adapter2.add("ヶ月");
		adapter2.add("歳");

		// 体重の選択肢
		adapter3.add("ｇ");
		adapter3.add("kg");

		// 【スピナー】選択タイプの指定
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 【スピナー】XMLとの関連付け
		selectSpinner1 = (Spinner) findViewById(R.id.spinner1);
		selectSpinner2 = (Spinner) findViewById(R.id.spinner2);
		selectSpinner3 = (Spinner) findViewById(R.id.spinner3);

		// 【スピナー】項目のセット
		selectSpinner1.setAdapter(adapter1);
		selectSpinner2.setAdapter(adapter2);
		selectSpinner3.setAdapter(adapter3);

		// 【ボタン】XMLとの関連付け
		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(this);
		btn3 = (Button) findViewById(R.id.button3);
		btn3.setOnClickListener(this);

		// 【イメージボタン】XMLとの関連付け
		imgImageButton1 = (ImageButton) findViewById(R.id.imageButton1);
		// 【イメージボタン】クリックの有効化
		imgImageButton1.setOnClickListener(this);

		// 【イメージビュー】XMLとの関連付け
		imgImageView = (ImageView) findViewById(R.id.imageView1);

		flag = false;
		// マジックナンバーの初期値を代入
		magic = 0;
		// input();

	}// 「onCreat」メソッド終了 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onStart」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	@Override
	protected void onStart() {
		super.onStart();
		dao = new LovecatDao(this);
		dao3 = new LovecatDao3(this);

		if (!flag) {
			// インテントデータ取得 …確認画面から編集ボタンを押されたとき
			Bundle b = getIntent().getExtras();
			if (b != null) {
				magic = b.getInt("key");
				if (magic == 1) {
					input();
				}
				if (magic == 2) {
					// パラメーターよりIDを取得
					catID_id = b.getInt("catID_id");

					// DBより該当するレコードを取得
					r1 = dao.getRecord(catID_id);

					// （確認画面用）各コンポーネントに該当フィールドの値をセット

					txtName.setText(r1.name); // 入力された名前
					txtSex.setText(r1.sex); // 選択された性別
					txtAge.setText(r1.age); // 入力された年齢
					txtAgeUnit.setText(r1.ageUnit); // 選択された年齢単位
					txtHairColor.setText(r1.hairColor); // 入力された種類

					// 【イメージビュー】
					imgImageView.setImageBitmap(r1.facePhoto);// 写真データ

					// （編集画面用) 各コンポーネントに該当フィールドの値をセット
					imgImageButton1.setImageBitmap(r1.thumnail);// サムネイル
					edtName.setText(r1.name); // 名前
					selectSpinner1.setSelection(r1.sexID); // 性別スピナーデータ
					edtAge.setText(r1.age); // 年齢
					selectSpinner2.setSelection(r1.ageUnitID); // 年齢スピナーデータ
					edtHairColor.setText(r1.hairColor); // 種類
					bmpThumnail = r1.thumnail; // DBからデータを取得して値の入った変数を準備しておく
					bmpFacePhoto = r1.facePhoto; // 同上


					r3= dao3.getRecord4(catID_id);

					// 体重専門DB
					// 最新日付の体重データをセット
					float maxWeight = dao3.getMaxData(catID_id+"");
					// 最新日付の体重単位データをセット
					int maxUnit = dao3.getMaxUnit(catID_id+"");
					// 最新日付の体重単位データをセット
					int maxUnitNum = dao3.getMaxUnitNum(catID_id+"");

					//DBがあるとき
					if (r3 != null) {
						// 確認画面用のセット
						txtWeight.setText(""+maxWeight); // 入力された体重
						if(maxUnit==0){
							txtWeightUnit.setText("g"); // 選択された体重単位
						}else{
							txtWeightUnit.setText("kg"); // 選択された体重単位
						}

						// 入力画面用のセット
						edtWeight.setText(""+maxWeight); // 体重
						selectSpinner3.setSelection(maxUnitNum); // 体重スピナーデータ

					//DBがないとき
					} else {
						// 確認画面用セット
						txtWeight.setText("体重が登録されていません");
						txtWeightUnit.setText(""); // 選択された体重単位
						// 入力画面用
						edtWeight.setText(""); // 体重
						selectSpinner3.setSelection(0); // 体重スピナーデータ
					}
					result();
				}// mafic==2の時の処理終わり

			}
		} else {
			input();
		}
	}// 「onStart」メソッド終了 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onClick」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	public void onClick(View v) {
		// 【写真登録ボタン】

		if (v == imgImageButton1) {
			// 【ギャラリー】インテントのインスタンス生成（リンク先のアクティビティを第2引数に入れておく）
			Intent intent = new Intent();

			// 【写真】 写真の選択
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK); // ギャラリーの呼び出し

			// インテントデータのマーキング+送信
			startActivityForResult(intent, REQUEST_GALLERY);
		}

		// 【入力完了ボタン】
		if (v == btn1) {
			result();
			set();

			// 【編集ボタン】
		}
		if (v == btn2) {
			input();

			// 【保存ボタン】
		}
		if (v == btn3) {
			if (magic == 1) {

				ContentValues values = new ContentValues();
				values.put("facePhoto", dao.bitmapToByte(bmpFacePhoto)); // 顔写真
				values.put("thumnail", dao.bitmapToByte(bmpThumnail)); // サムネイル
				values.put("name", edtName.getText().toString()); // 名前
				values.put("sex", selectSpinner1.getSelectedItem().toString()); // 性別
				values.put("sexID", selectSpinner1.getSelectedItemPosition()); // 性別スピナーデータ
				values.put("age", edtAge.getText().toString()); // 年齢
				values.put("ageUnit", selectSpinner2.getSelectedItem()
						.toString()); // 年齢単位
				values.put("ageUnitID",
						selectSpinner2.getSelectedItemPosition()); // 年齢単位スピナーデータ
				values.put("hairColor", edtHairColor.getText().toString()); // 種類
				final long newId = dao.insert("catID", values);

				ContentValues values2 = new ContentValues();

				// 現時刻を取得して、保存する
				Time time = new Time("Asia/Tokyo");
				time.setToNow();
				String w_day = "" + time.year + ". "
						+ new DecimalFormat("00").format(time.month + 1) + ". "
						+ new DecimalFormat("00").format(time.monthDay);
				values2.put("year", time.year);
				values2.put("monthOfYear", time.month);
				values2.put("dayOfMonth", time.monthDay);
				values2.put("w_day", w_day);
				values2.put("catID_id", newId);
				if (edtWeight.getText().toString() != null) {
					values2.put("weight", edtWeight.getText().toString()); // 体重
				} else {
					values2.put("weight", 0); // 体重
				}
			//	values2.put("weight", edtWeight.getText().toString()); // 体重
				values2.put("unit", selectSpinner3.getSelectedItem().toString()); // 体重単位
				values2.put("unit_id", selectSpinner3.getSelectedItemPosition()); // 体重単位スピナーデータ
				dao3.insert("weight_control", values2);

				showYesNoDialog(this, "この情報を登録しました。", "メニュー画面に移ります",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (which == DialogInterface.BUTTON_POSITIVE) {
									// 登録後メニュー画面へ戻る
									Intent intent = new Intent(
											neko.neko.nekokalte_cat.CatData.this,
											neko.neko.nekokalte_cat.CatMenu.class);
									intent.putExtra("catID_id", (int) newId);
									intent.putExtra("key", 2);
									startActivity(intent);
									finish();
								} else if (which == DialogInterface.BUTTON_NEGATIVE) {
									// 登録後、この画面に残る
								}

							}
						});// showYes/NoDialogメソッド終了

			}// magic==1のときの処理終わり

			if (magic == 2) {
				ContentValues values = new ContentValues();
				values.put("facePhoto", dao.bitmapToByte(bmpFacePhoto)); // 顔写真
				values.put("thumnail", dao.bitmapToByte(bmpThumnail)); // サムネイル
				values.put("name", edtName.getText().toString()); // 名前
				values.put("sex", selectSpinner1.getSelectedItem().toString()); // 性別
				values.put("sexID", selectSpinner1.getSelectedItemPosition()); // 性別スピナーデータ
				values.put("age", edtAge.getText().toString()); // 年齢
				values.put("ageUnit", selectSpinner2.getSelectedItem()
						.toString()); // 年齢単位
				values.put("ageUnitID",
						selectSpinner2.getSelectedItemPosition()); // 年齢単位スピナーデータ
				values.put("hairColor", edtHairColor.getText().toString()); // 種類
				dao.update("catID", values, catID_id);
				ContentValues values2 = new ContentValues();
				// 現時刻を取得して、保存する
				Time time = new Time("Asia/Tokyo");
				time.setToNow();
				String w_day = "" + time.year + ". "
						+ new DecimalFormat("00").format(time.month + 1) + ". "
						+ new DecimalFormat("00").format(time.monthDay);


				dao3 = new LovecatDao3(this);
				r3 = dao3.getRecord3(catID_id, w_day);

				if (w_day.equals(r3.w_day)) {
					values2.put("year", time.year);
					values2.put("monthOfYear", time.month);
					values2.put("dayOfMonth", time.monthDay);
					values2.put("w_day", w_day);
					values2.put("catID_id", catID_id);
					if (edtWeight.getText().toString() != null) {
						values2.put("weight", edtWeight.getText().toString()); // 体重
					} else {
						values2.put("weight", edtWeight.getText().toString()); // 体重
					}
					values2.put("unit", selectSpinner3.getSelectedItem()
							.toString()); // 体重単位
					values2.put("unit_id",
							selectSpinner3.getSelectedItemPosition()); // 体重単位スピナーデータ
					dao3.update("weight_control", values2, r3.weight_Id);

				} else {
					values2.put("year", time.year);
					values2.put("monthOfYear", time.month);
					values2.put("dayOfMonth", time.monthDay);
					values2.put("w_day", w_day);
					values2.put("catID_id", catID_id);
					values2.put("weight", edtWeight.getText().toString()); // 体重
					values2.put("unit", selectSpinner3.getSelectedItem()
							.toString()); // 体重単位
					values2.put("unit_id",
							selectSpinner3.getSelectedItemPosition()); // 体重単位スピナーデータ

					dao3.insert("weight_control", values2);
				}

				// メニューに戻るかどうかをたずねるダイアログの表示
				showYesNoDialog(this, "この情報を上書き保存しました。", "メニュー画面に戻りますか？",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (which == DialogInterface.BUTTON_POSITIVE) {
									// 登録後メニュー画面へ戻る
									// Intent intent = new Intent(
									// neko.neko.nekokalte_cat.CatData.this,
									// neko.neko.nekokalte_cat.CatMenu.class);
									// intent.putExtra("catID_id", catID_id);
									// intent.putExtra("key", 2);
									// startActivity(intent);
									finish();
								} else if (which == DialogInterface.BUTTON_NEGATIVE) {
									// 登録後、この画面に残る
								}

							}
						});// showYes/NoDialogメソッド終了
			}// magic==2のときの処理終わり

		}

	}

	// 「onClick」メソッド終わり ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onActivityResult」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// ギャラリーで選択した写真かどうかを判断
		if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			uri = intent.getData();

			try {
				Cursor c = this.managedQuery(uri, null, null, null, null);
				c.moveToFirst();
				long id = c.getLong(c.getColumnIndexOrThrow("_id"));
				ContentResolver cr = getContentResolver();
				bmpThumnail = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
						MediaStore.Images.Thumbnails.MICRO_KIND, null);
				imgImageButton1.setImageBitmap(bmpThumnail);

			} catch (Exception e) {
				Toast.makeText(this, "失敗しました。", Toast.LENGTH_LONG).show();
			}

			imageReduction(); // 選択画像の縮小メソッド呼び出し
			flag = true;// onStartで編集からきたm=2と競合しないようにフラグをつけておく。
		}

	}// 「onActivityResult」メソッド終わり ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// 【ローカルメソッド】選択画像の縮小メソッド ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	public void imageReduction() {
		BitmapFactory.Options mOptions = new BitmapFactory.Options();
		mOptions.inSampleSize = 10; // サイズはここで変更できる
		Bitmap resizeBitmap = null;

		try {
			InputStream is = getContentResolver().openInputStream(uri);
			resizeBitmap = BitmapFactory.decodeStream(is, null, mOptions);
			is.close();

			bmpFacePhoto = resizeBitmap;
		} catch (IOException e) {

		}

	}// 「imageReduction」メソッド終わり ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// 「表示用」
	private void input() {
		// 【タイトル表示】
		txtPhoto_title.setVisibility(View.VISIBLE);
		txtName_title.setVisibility(View.VISIBLE);
		txtSex_title.setVisibility(View.VISIBLE);
		txtAge_title.setVisibility(View.VISIBLE);
		txtWeight_title.setVisibility(View.VISIBLE);
		txtHairColor_title.setVisibility(View.VISIBLE);

		// 【テキストボックス表示】
		edtName.setVisibility(View.VISIBLE);
		edtAge.setVisibility(View.VISIBLE);
		edtWeight.setVisibility(View.VISIBLE);
		edtHairColor.setVisibility(View.VISIBLE);

		// 【テキストビュー】XMLとの関連付け 確認データ
		txtName.setVisibility(View.GONE); // 入力された名前
		txtSex.setVisibility(View.GONE); // 選択された性別
		txtAge.setVisibility(View.GONE); // 入力された年齢
		txtAgeUnit.setVisibility(View.GONE); // 選択された年齢単位
		txtWeight.setVisibility(View.GONE); // 入力された体重
		txtWeightUnit.setVisibility(View.GONE); // 選択された体重単位
		txtHairColor.setVisibility(View.GONE); // 入力された種類

		// 【スピナー】
		selectSpinner1.setVisibility(View.VISIBLE);
		selectSpinner2.setVisibility(View.VISIBLE);
		selectSpinner3.setVisibility(View.VISIBLE);

		// 【ボタン】
		btn1.setVisibility(View.VISIBLE);
		btn2.setVisibility(View.GONE);
		btn3.setVisibility(View.GONE);
		// 【イメージボタン】
		imgImageButton1.setVisibility(View.VISIBLE);

		// 【イメージビュー】
		imgImageView.setVisibility(View.GONE);
	}

	private void result() {
		// 【タイトル表示】
		txtPhoto_title.setVisibility(View.GONE);
		txtName_title.setVisibility(View.VISIBLE);
		txtSex_title.setVisibility(View.VISIBLE);
		txtAge_title.setVisibility(View.VISIBLE);
		txtWeight_title.setVisibility(View.VISIBLE);
		txtHairColor_title.setVisibility(View.VISIBLE);

		// 【テキストボックス表示】
		edtName.setVisibility(View.GONE);
		edtAge.setVisibility(View.GONE);
		edtWeight.setVisibility(View.GONE);
		edtHairColor.setVisibility(View.GONE);

		// 【テキストビュー】XMLとの関連付け 確認データ
		txtName.setVisibility(View.VISIBLE); // 入力された名前
		txtSex.setVisibility(View.VISIBLE); // 選択された性別

		txtAge.setVisibility(View.VISIBLE); // 入力された年齢

		txtAgeUnit.setVisibility(View.VISIBLE); // 選択された年齢単位

		txtWeight.setVisibility(View.VISIBLE); // 入力された体重

		txtWeightUnit.setVisibility(View.VISIBLE); // 選択された体重単位

		txtHairColor.setVisibility(View.VISIBLE); // 入力された種類

		// 【スピナー】
		selectSpinner1.setVisibility(View.GONE);
		selectSpinner2.setVisibility(View.GONE);
		selectSpinner3.setVisibility(View.GONE);

		// 【ボタン】
		btn1.setVisibility(View.GONE);
		btn2.setVisibility(View.VISIBLE);
		btn3.setVisibility(View.VISIBLE);
		// 【イメージボタン】
		imgImageButton1.setVisibility(View.GONE);

		// 【イメージビュー】
		imgImageView.setVisibility(View.VISIBLE);

	}

	// 「インプット後の確認画面セット用」
	private void set() {

		txtName.setText(edtName.getText().toString());
		txtSex.setText(selectSpinner1.getSelectedItem().toString());
		txtAge.setText(edtAge.getText().toString());
		txtAgeUnit.setText(selectSpinner2.getSelectedItem().toString());
		txtWeight.setText(edtWeight.getText().toString());
		txtWeightUnit.setText(selectSpinner3.getSelectedItem().toString());
		txtHairColor.setText(edtHairColor.getText().toString());
		imgImageView.setImageBitmap(bmpFacePhoto);

	}

	// ■「ローカルメソッド」開始 Yes/Noダイアログ→教科書P162～163＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	private static void showYesNoDialog(Context cotext, String title,
			String text, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder ad = new AlertDialog.Builder(cotext);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("はい", listener);
		ad.setNegativeButton("いいえ", listener);
		ad.show();
	}// 「ローカルメソッド」終了＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

}
