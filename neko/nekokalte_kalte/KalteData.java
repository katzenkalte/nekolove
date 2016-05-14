package neko.neko.nekokalte_kalte;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

import neko.neko.nekokalte.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class KalteData extends Activity implements View.OnClickListener {
	// ■フィールドの設定 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	// XML関連付け用変数
	private TextView txtTitle; // 「カルテ入力画面」

	private TextView txt_day_title;// 「日付」タイトル
	private TextView txt_S_title;// 「症状」タイトル
	private TextView txtExplan_S; // 症状入力説明文
	private TextView txtExplan_S4p;// 症状写真選択説明文
	private TextView txt_D_title;// 「診断」タイトル
	private TextView txtExplan_D; // 診察入力説明
	private TextView txt_P_title;// 「処方」タイトル
	private TextView txt_F_title;// 「金額」タイトル
	private TextView slash;// 日付と項目の間の「/」

	private TextView txtDay; // 入力された「日付」
	private TextView txt_feeUnit;// 「円」
	private TextView txtGenre;// 選択された「項目」
	private TextView txtSymptom; // 入力された「症状」
	private TextView txtDiagnosis; // 入力された「診察内容」
	private TextView txtPrescription; // 入力された「処方」
	private TextView txtFee;// 入力された「金額」

	// ******************************************************************
	/** 「画像追加」 */

	private ImageButton imgButton1;// 「症状」の写真データ選択ボタン１
	private ImageButton imgButton2;// 「症状」の写真データ選択ボタン２
	private ImageButton imgButton3;// 「症状」の写真データ選択ボタン３

	private ImageView img1;// XML用「症状」の写真データ変数１
	private ImageView img2;// XML用「症状」の写真データ変数２
	private ImageView img3;// XML用「症状」の写真データ変数３

	private Uri uri_1; // 選択した画像データのID
	private Uri uri_2; // 選択した画像データのID
	private Uri uri_3; // 選択した画像データのID

	private Bitmap bmpThumnail_1; // 取得したサムネイル画像
	private Bitmap bmpThumnail_2; // 取得したサムネイル画像
	private Bitmap bmpThumnail_3; // 取得したサムネイル画像
	private Bitmap image;// 何も選択されていないときのサムネイル画像

	private Bitmap bmpSymptomPhoto1; // Bitmap変換後の加工済み写真
	private Bitmap bmpSymptomPhoto2; // Bitmap変換後の加工済み写真
	private Bitmap bmpSymptomPhoto3; // Bitmap変換後の加工済み写真

	private final static int REQUEST_GALLERY1 = 1; // 写真データ取得用
	private final static int REQUEST_GALLERY2 = 2; // 写真データ取得用
	private final static int REQUEST_GALLERY3 = 3; // 写真データ取得用

	private Button button1; // 入力完了
	private Button button2; // 編集
	private Button button3; // 保存
	// private Button button4; // 追加

	private EditText edtDayData; // 日付
	private EditText edtSymptom; // 症状入力
	private EditText edtDiagnosis; // 診察入力
	private EditText edtPrescrption;// 処方入力
	private EditText edtFee; // 金額

	private Spinner spinner1; // 項目

	private int magic; // マジックナンバー
	private int catID_id;// 個体識別用ID
	boolean flag;

	// データ保存用
	private int k_listId;
	private LovecatDao2 dao2;
	private TblLovecatRecord2 r2;
	Resources r;

	// 日付設定ダイアログのインスタンスを格納するフィールド
	private DatePickerDialog.OnDateSetListener varDateSetListener;
	static int year;
	static int monthOfYear;
	static int dayOfMonth;
	Calendar calendar;

	// フィールドの設定終わり ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onCreate」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 【レイアウト】XMLとの関連付け
		setContentView(R.layout.kaltedatainput);

		// 【テキストビュー】XMLとの関連付け
		txtTitle = (TextView) findViewById(R.id.textView1);
		txtExplan_S = (TextView) findViewById(R.id.textView2);
		txtExplan_D = (TextView) findViewById(R.id.textView3);
		txtSymptom = (TextView) findViewById(R.id.textView4);
		txtDiagnosis = (TextView) findViewById(R.id.textView5);
		txtPrescription = (TextView) findViewById(R.id.textView6);
		txt_day_title = (TextView) findViewById(R.id.textView7);
		slash = (TextView) findViewById(R.id.textView8);
		txt_S_title = (TextView) findViewById(R.id.textView9);
		txt_D_title = (TextView) findViewById(R.id.textView10);
		txt_P_title = (TextView) findViewById(R.id.textView11);
		txtGenre = (TextView) findViewById(R.id.textView12);
		txt_F_title = (TextView) findViewById(R.id.textView13);
		txtFee = (TextView) findViewById(R.id.textView14);
		txt_feeUnit = (TextView) findViewById(R.id.textView15);
		txtDay = (TextView) findViewById(R.id.textView16);
		txtExplan_S4p = (TextView) findViewById(R.id.textView17);

		// 【エディットテキスト】XMLとの関連付け
		edtDayData = (EditText) findViewById(R.id.editText1);
		edtDayData.setOnClickListener(this);
		edtSymptom = (EditText) findViewById(R.id.editText2);
		edtDiagnosis = (EditText) findViewById(R.id.editText3);
		edtPrescrption = (EditText) findViewById(R.id.editText4);
		edtFee = (EditText) findViewById(R.id.editText5);

		// 【スピナー】デザインの指定
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);

		// 【スピナー】項目の追加
		adapter1.add("ケガ");
		adapter1.add("病気");
		adapter1.add("体調不良");
		adapter1.add("問題行動");
		adapter1.add("健康診断");
		adapter1.add("ダイエット");
		adapter1.add("そのほか");

		// 【スピナー】選択タイプの指定
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 【スピナー】XMLとの関連付け
		spinner1 = (Spinner) findViewById(R.id.spinner1);

		// 【スピナー】項目のセット
		spinner1.setAdapter(adapter1);

		// 【ボタン】XMLとの関連付け+クリックの有効化
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);

		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);

		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);

		// button4 = (Button) findViewById(R.id.button4);
		// button4.setOnClickListener(this);

		// ******************************************************************
		/** 「画像追加」 */

		// 【イメージボタン】XMLとの関連付け+クリックの有効化
		imgButton1 = (ImageButton) findViewById(R.id.imageButton1);
		imgButton1.setOnClickListener(this);

		imgButton2 = (ImageButton) findViewById(R.id.imageButton2);
		imgButton2.setOnClickListener(this);

		imgButton3 = (ImageButton) findViewById(R.id.imageButton3);
		imgButton3.setOnClickListener(this);

		// 【イメージビュー】XMLとの関連付け
		img1 = (ImageView) findViewById(R.id.imageView1);
		img2 = (ImageView) findViewById(R.id.imageView2);
		img3 = (ImageView) findViewById(R.id.imageView3);

		// ******************************************************************

		magic = 0;

		flag = false;

	}// 「onCreat」メソッド終了 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onStart」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	@Override
	protected void onStart() {
		super.onStart();
		r = getResources();
		image = BitmapFactory.decodeResource(r, R.drawable.ic_launcher);

		dao2 = new LovecatDao2(this);

		if (!flag) {
			// インテントデータ取得 …確認画面から編集ボタンを押されたとき
			Bundle b = getIntent().getExtras();
			if (b != null) {
				magic = b.getInt("key");
				catID_id = b.getInt("catID_id");

				if (magic == 11) {
					input();
				}
				if (magic == 12) {

					// パラメーターよりIDを取得
					k_listId = b.getInt("k_listId");
					// DBより該当するレコードを取得
					r2 = dao2.getRecord(k_listId);

					// （確認画面用）各コンポーネントに該当フィールドの値をセット
					year = r2.year;
					monthOfYear = r2.monthOfYear;
					dayOfMonth = r2.dayOfMonth;

					txtDay.setText(r2.day); // 日付
					txtSymptom.setText(r2.symptom);// 入力された「症状」
					txtDiagnosis.setText(r2.diagnosis); // 入力された「診察内容」
					txtPrescription.setText(r2.prescription);// 入力された「処方」
					txtFee.setText("" + r2.fee);// 入力された「金額」
					txtGenre.setText(r2.genre);// 選択された「項目」

					// ******************************************************************
					/** 「画像追加」 */
					img1.setImageBitmap(r2.symptomPhoto1);// 写真データ
					img2.setImageBitmap(r2.symptomPhoto2);// 写真データ
					img3.setImageBitmap(r2.symptomPhoto3);// 写真データ
					// ******************************************************************

					// （編集画面用) 各コンポーネントに該当フィールドの値をセット
					edtDayData.setText(r2.day); // 日付
					spinner1.setSelection(r2.genreID); // ジャンル選択スピナーデータ
					edtSymptom.setText(r2.symptom); // 症状
					edtDiagnosis.setText(r2.diagnosis); // 診察
					edtPrescrption.setText(r2.prescription); // 処方
					edtFee.setText("" + r2.fee); // 金額
					if (r2.thumnail1 != null) {
						imgButton1.setImageBitmap(r2.thumnail1);// サムネイル１
					} else if (r2.thumnail1 == null) {
						imgButton1.setImageBitmap(image);// サムネイル１
					}

					if (r2.thumnail2 != null) {
						imgButton2.setImageBitmap(r2.thumnail2);// サムネイル１
					} else if (r2.thumnail2 == null) {
						imgButton2.setImageBitmap(image);// サムネイル１
					}

					if (r2.thumnail3 != null) {
						imgButton3.setImageBitmap(r2.thumnail3);// サムネイル１
					} else if (r2.thumnail3 == null) {
						imgButton3.setImageBitmap(image);// サムネイル１
					}

					// ******************************************************************
					/** 「画像追加」 */
					bmpThumnail_1 = r2.thumnail1; // DBからデータを取得して値の入った変数を準備しておく
					bmpThumnail_2 = r2.thumnail2;
					bmpThumnail_3 = r2.thumnail3;
					bmpSymptomPhoto1 = r2.symptomPhoto1;
					bmpSymptomPhoto2 = r2.symptomPhoto2;
					bmpSymptomPhoto3 = r2.symptomPhoto3;
					// ******************************************************************

					result();
				}

			} else {
				input();
			}
		}
	}// 「onStart」メソッド終了 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// ■「onClick」メソッド開始 ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	public void onClick(View v) {

		if (v == edtDayData) {
			// 「日付入力ダイアログ」

			// ①イベントリスナーのインスタンス化
			varDateSetListener = new DatePickerDialog.OnDateSetListener() {
				// ②日付設定ダイアログの｢設定｣ボタンがクリックされたときの処理
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// ③データをエディットテキストに書き込む
					edtDayData
							.setText(year
									+ ". "
									+ new DecimalFormat("00")
											.format(monthOfYear + 1)
									+ ". "
									+ new DecimalFormat("00")
											.format(dayOfMonth));

					KalteData.year = year;
					KalteData.monthOfYear = monthOfYear + 1;
					KalteData.dayOfMonth = dayOfMonth;
				}
			};

			// ④現在日時を取得
			Calendar calendar = Calendar.getInstance();
			// ⑤日時設定ダイアログのインスタンスを生成

			DatePickerDialog dateDialog = null;
			if (magic == 11) {
				dateDialog = new DatePickerDialog(this,
						varDateSetListener, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
			}
			if (magic == 12) {
				dateDialog = new DatePickerDialog(this,
						varDateSetListener, year, monthOfYear-1, dayOfMonth);

			}
			// ⑥日付設定ダイアログを表示
			dateDialog.show();
		}

		/**
		 * イメージボタンがクリックされたときにデータが入っている場合は、 デリートするかどうかを尋ねるダイアログを表示。
		 * ダイアログには「画像を削除する/画像を変更する」の選択肢が出てくるようにする。
		 */

		// 【イメージボタン１】
		if (v == imgButton1) {

			if (bmpThumnail_1 != null) {
				ordelete1();
			}
			if (bmpThumnail_1 == null) {
				photoSelect1();
			}

			// 【イメージボタン２】
		} else if (v == imgButton2) {
			if (bmpThumnail_2 != null) {
				ordelete2();
			}
			if (bmpThumnail_2 == null) {
				photoSelect2();
			}

			// 【イメージボタン３】
		} else if (v == imgButton3) {
			if (bmpThumnail_3 != null) {
				ordelete3();
			}
			if (bmpThumnail_3 == null) {
				photoSelect3();
			}
		}

		// 【入力完了ボタン】
		if (v == button1) {
			result();
			set();

			// 【編集ボタン】
		} else if (v == button2) {
			input();

			// 【保存ボタン】
		} else if (v == button3) {

			if (magic == 11) {
				ContentValues values = new ContentValues();
				values.put("day", edtDayData.getText().toString()); // 日付フル
				values.put("year", year);// 年
				values.put("monthOfYear", monthOfYear);// 月
				values.put("dayOfMonth", dayOfMonth);// 日
				values.put("genre", spinner1.getSelectedItem().toString()); // 項目
				values.put("genreID", spinner1.getSelectedItemPosition()); // 項目スピナーデータ
				values.put("symptom", edtSymptom.getText().toString()); // 症状
				values.put("diagnosis", edtDiagnosis.getText().toString()); // 診察内容
				values.put("prescription", edtPrescrption.getText().toString()); // 処方
				if (edtFee.getText().toString().length() == 0) {// 取得する文字列が何もないとき～.length==0で判定
					values.put("fee", 0);// 金額
				} else {
					values.put("fee",
							Integer.parseInt(edtFee.getText().toString()));// 金額
				}
				values.put("catID_id", catID_id);
				values.put("symptomPhoto1", dao2.bitmapToByte(bmpSymptomPhoto1));
				values.put("symptomPhoto2", dao2.bitmapToByte(bmpSymptomPhoto2));
				values.put("symptomPhoto3", dao2.bitmapToByte(bmpSymptomPhoto3));
				values.put("thumnail1", dao2.bitmapToByte(bmpThumnail_1));
				values.put("thumnail2", dao2.bitmapToByte(bmpThumnail_2));
				values.put("thumnail3", dao2.bitmapToByte(bmpThumnail_3));

				dao2.insert("kalte", values);

				showYesNoDialog(this, "この情報を登録しました。", "カルテ一覧に戻りますか？",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (which == DialogInterface.BUTTON_POSITIVE) {
									// 登録後メニュー画面へ戻る
									// Intent intent = new Intent(
									// neko.neko.nekokalte_kalte.KalteData.this,
									// KalteListView.class);
									// intent.putExtra("catID_id", catID_id);
									// startActivity(intent);
									finish();
								} else if (which == DialogInterface.BUTTON_NEGATIVE) {
									// 登録後、この画面に残る
								}

							}
						});// showYes/NoDialogメソッド終了
			}
			if (magic == 12) {

				ContentValues values = new ContentValues();

				values.put("day", edtDayData.getText().toString()); // 日付フル
				values.put("year", year);// 年
				values.put("monthOfYear", monthOfYear);// 月
				values.put("dayOfMonth", dayOfMonth);// 日
				values.put("genre", spinner1.getSelectedItem().toString()); // 項目
				values.put("genreID", spinner1.getSelectedItemPosition()); // 項目スピナーデータ
				values.put("symptom", edtSymptom.getText().toString()); // 症状
				values.put("diagnosis", edtDiagnosis.getText().toString()); // 診察内容
				values.put("prescription", edtPrescrption.getText().toString()); // 処方
				if (edtFee.getText().toString().length() == 0) {// 取得する文字列が何もないとき～.length==0で判定
					values.put("fee", 0);// 金額
				} else {
					values.put("fee",
							Integer.parseInt(edtFee.getText().toString()));// 金額
				}
				values.put("catID_id", catID_id);
				values.put("symptomPhoto1", dao2.bitmapToByte(bmpSymptomPhoto1));
				values.put("symptomPhoto2", dao2.bitmapToByte(bmpSymptomPhoto2));
				values.put("symptomPhoto3", dao2.bitmapToByte(bmpSymptomPhoto3));
				values.put("thumnail1", dao2.bitmapToByte(bmpThumnail_1));
				values.put("thumnail2", dao2.bitmapToByte(bmpThumnail_2));
				values.put("thumnail3", dao2.bitmapToByte(bmpThumnail_3));

				dao2.update("kalte", values, k_listId);

				showYesNoDialog(this, "この情報を上書きしました。", "カルテ一覧に戻りますか？",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == DialogInterface.BUTTON_POSITIVE) {
									// 登録後メニュー画面へ戻る
									// Intent intent = new Intent(
									// neko.neko.nekokalte_kalte.KalteData.this,
									// KalteListView.class);
									// intent.putExtra("catID_id", catID_id);
									// startActivity(intent);
									finish();
								} else if (which == DialogInterface.BUTTON_NEGATIVE) {
									// 登録後、この画面に残る
								}
							}
						});// showYes/NoDialogメソッド終了

			}
		}
	}// 「onClick」メソッド終わり ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

	// 「表示用」
	private void input() {
		// 【タイトル表示】
		txtTitle.setVisibility(View.VISIBLE);
		txtExplan_S.setVisibility(View.VISIBLE); // 症状入力説明文
		txtExplan_S4p.setVisibility(View.VISIBLE); // 症状写真選択説明文
		txtExplan_D.setVisibility(View.VISIBLE); // 診察入力説明
		txt_day_title.setVisibility(View.GONE);// 「日付」タイトル
		txt_S_title.setVisibility(View.VISIBLE);// 「症状」タイトル
		txt_D_title.setVisibility(View.VISIBLE);// 「診断」タイトル
		txt_P_title.setVisibility(View.VISIBLE);// 「処方」タイトル
		txt_F_title.setVisibility(View.VISIBLE);// 「金額」タイトル
		slash.setVisibility(View.GONE);// 日付と項目の間の「/」

		// 【テキストボックス表示】
		edtDayData.setVisibility(View.VISIBLE);
		edtSymptom.setVisibility(View.VISIBLE);
		edtDiagnosis.setVisibility(View.VISIBLE);
		edtPrescrption.setVisibility(View.VISIBLE);
		edtFee.setVisibility(View.VISIBLE);

		// 【テキストビュー】XMLとの関連付け 確認データ
		txtDay.setVisibility(View.GONE);
		txtSymptom.setVisibility(View.GONE);// 入力された「症状」
		txtDiagnosis.setVisibility(View.GONE); // 入力された「診察内容」
		txtPrescription.setVisibility(View.GONE);// 入力された「処方」
		txtFee.setVisibility(View.GONE);// 入力された「金額」
		txt_feeUnit.setVisibility(View.GONE);// 「円」
		txtGenre.setVisibility(View.GONE);// 選択された「項目」

		// 【スピナー】
		spinner1.setVisibility(View.VISIBLE);

		// 【ボタン】
		button1.setVisibility(View.VISIBLE);
		button2.setVisibility(View.GONE);
		button3.setVisibility(View.GONE);
		// button4.setVisibility(View.GONE);

		// ******************************************************************
		/** 「画像追加」 */
		imgButton1.setVisibility(View.VISIBLE);
		imgButton2.setVisibility(View.VISIBLE);
		imgButton3.setVisibility(View.VISIBLE);

		img1.setVisibility(View.GONE);
		img2.setVisibility(View.GONE);
		img3.setVisibility(View.GONE);
		// ******************************************************************

	}

	private void result() {
		txtTitle.setVisibility(View.GONE);
		txtExplan_S.setVisibility(View.GONE); // 症状入力説明文
		txtExplan_S4p.setVisibility(View.GONE); // 症状写真選択説明文
		txtExplan_D.setVisibility(View.GONE); // 診察入力説明
		txt_day_title.setVisibility(View.GONE);// 「日付」タイトル
		txt_S_title.setVisibility(View.VISIBLE);// 「症状」タイトル
		txt_D_title.setVisibility(View.VISIBLE);// 「診断」タイトル
		txt_P_title.setVisibility(View.VISIBLE);// 「処方」タイトル
		txt_F_title.setVisibility(View.VISIBLE);// 「金額」タイトル
		slash.setVisibility(View.VISIBLE);// 日付と項目の間の「/」

		// 【テキストボックス表示】
		edtDayData.setVisibility(View.GONE);
		edtSymptom.setVisibility(View.GONE);
		edtDiagnosis.setVisibility(View.GONE);
		edtPrescrption.setVisibility(View.GONE);
		edtFee.setVisibility(View.GONE);

		// 【テキストビュー】XMLとの関連付け 確認データ
		txtDay.setVisibility(View.VISIBLE);// 入力された「日付」
		txtSymptom.setVisibility(View.VISIBLE);// 入力された「症状」
		txtDiagnosis.setVisibility(View.VISIBLE); // 入力された「診察内容」
		txtPrescription.setVisibility(View.VISIBLE);// 入力された「処方」
		txtFee.setVisibility(View.VISIBLE);// 入力された「金額」
		txt_feeUnit.setVisibility(View.VISIBLE);// 「円」
		txtGenre.setVisibility(View.VISIBLE);// 選択された「項目」

		// 【スピナー】
		spinner1.setVisibility(View.GONE);

		// 【ボタン】
		button1.setVisibility(View.GONE);
		button2.setVisibility(View.VISIBLE);
		button3.setVisibility(View.VISIBLE);
		// button4.setVisibility(View.VISIBLE);

		// ******************************************************************
		/** 「画像追加」 */

		imgButton1.setVisibility(View.GONE);
		imgButton2.setVisibility(View.GONE);
		imgButton3.setVisibility(View.GONE);

		// if(もし画像が選択されていたら表示、されていなかったら非表示){
		if (bmpSymptomPhoto1 == null) {
			img1.setVisibility(View.GONE);
			bmpThumnail_1 = null;
		} else if (bmpSymptomPhoto1 != null) {
			img1.setVisibility(View.VISIBLE);
		}

		if (bmpSymptomPhoto2 == null) {
			img2.setVisibility(View.GONE);
			bmpThumnail_2 = null;
		} else if (bmpSymptomPhoto2 != null) {
			img2.setVisibility(View.VISIBLE);
		}

		if (bmpSymptomPhoto3 == null) {
			img3.setVisibility(View.GONE);
			bmpThumnail_3 = null;
		} else if (bmpSymptomPhoto3 != null) {
			img3.setVisibility(View.VISIBLE);
		}
		// ******************************************************************

	}

	// 「インプット後の確認画面セット用」
	private void set() {

		txtDay.setText(edtDayData.getText().toString()); // 日付
		txtGenre.setText(spinner1.getSelectedItem().toString());// 項目
		txtSymptom.setText(edtSymptom.getText().toString());// 症状
		txtDiagnosis.setText(edtDiagnosis.getText().toString());// 診断
		txtPrescription.setText(edtPrescrption.getText().toString());// 処方
		txtFee.setText(edtFee.getText().toString());// 金額
		img1.setImageBitmap(bmpSymptomPhoto1);
		img2.setImageBitmap(bmpSymptomPhoto2);
		img3.setImageBitmap(bmpSymptomPhoto3);
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

	// ******************************************************************
	/** 「画像追加」 */
	// ■「photoSelect」メソッド開始
	private void photoSelect1() {
		Intent intent = new Intent();

		// 【写真】 写真の選択
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK); // ギャラリーの呼び出し

		// インテントデータのマーキング+送信
		startActivityForResult(intent, REQUEST_GALLERY1);
	}// 「photoSelect」メソッド終了

	private void photoSelect2() {
		Intent intent = new Intent();

		// 【写真】 写真の選択
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK); // ギャラリーの呼び出し

		// インテントデータのマーキング+送信
		startActivityForResult(intent, REQUEST_GALLERY2);
	}// 「photoSelect」メソッド終了

	private void photoSelect3() {
		Intent intent = new Intent();

		// 【写真】 写真の選択
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK); // ギャラリーの呼び出し

		// インテントデータのマーキング+送信
		startActivityForResult(intent, REQUEST_GALLERY3);
	}// 「photoSelect」メソッド終了

	// ■「onActivityResult」メソッド開始
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// ギャラリーで選択した写真かどうかを判断
		if (requestCode == REQUEST_GALLERY1 && resultCode == RESULT_OK) {
			uri_1 = intent.getData();

			try {
				Cursor c = this.managedQuery(uri_1, null, null, null, null);
				c.moveToFirst();
				long id = c.getLong(c.getColumnIndexOrThrow("_id"));
				ContentResolver cr = getContentResolver();
				bmpThumnail_1 = MediaStore.Images.Thumbnails.getThumbnail(cr,
						id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
				imgButton1.setImageBitmap(bmpThumnail_1);

			} catch (Exception e) {
				Toast.makeText(this, "失敗しました。", Toast.LENGTH_LONG).show();
			}

			imageReduction1();
			flag = true;

		} else if (requestCode == REQUEST_GALLERY2 && resultCode == RESULT_OK) {
			uri_2 = intent.getData();
			try {
				Cursor c = this.managedQuery(uri_2, null, null, null, null);
				c.moveToFirst();
				long id = c.getLong(c.getColumnIndexOrThrow("_id"));
				ContentResolver cr = getContentResolver();
				bmpThumnail_2 = MediaStore.Images.Thumbnails.getThumbnail(cr,
						id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
				imgButton2.setImageBitmap(bmpThumnail_2);

			} catch (Exception e) {
				Toast.makeText(this, "失敗しました。", Toast.LENGTH_LONG).show();
			}

			imageReduction2();
			flag = true;

		} else if (requestCode == REQUEST_GALLERY3 && resultCode == RESULT_OK) {
			uri_3 = intent.getData();
			try {
				Cursor c = this.managedQuery(uri_3, null, null, null, null);
				c.moveToFirst();
				long id = c.getLong(c.getColumnIndexOrThrow("_id"));
				ContentResolver cr = getContentResolver();
				bmpThumnail_3 = MediaStore.Images.Thumbnails.getThumbnail(cr,
						id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
				imgButton3.setImageBitmap(bmpThumnail_3);

			} catch (Exception e) {
				Toast.makeText(this, "失敗しました。", Toast.LENGTH_LONG).show();
			}

			imageReduction3();
			flag = true;
		}
	}

	// ■「imageReduction」選択画像の縮小メソッド開始
	private void imageReduction1() {
		BitmapFactory.Options mOptions = new BitmapFactory.Options();
		mOptions.inSampleSize = 10; // サイズはここで変更できる
		Bitmap resizeBitmap = null;

		try {
			InputStream is = getContentResolver().openInputStream(uri_1);
			resizeBitmap = BitmapFactory.decodeStream(is, null, mOptions);
			is.close();

			bmpSymptomPhoto1 = resizeBitmap;
		} catch (IOException e) {

		}

	}// 「imageReduction」メソッド終了

	private void imageReduction2() {
		BitmapFactory.Options mOptions = new BitmapFactory.Options();
		mOptions.inSampleSize = 10; // サイズはここで変更できる
		Bitmap resizeBitmap = null;

		try {

			InputStream is = getContentResolver().openInputStream(uri_2);
			resizeBitmap = BitmapFactory.decodeStream(is, null, mOptions);
			is.close();

			bmpSymptomPhoto2 = resizeBitmap;
		} catch (IOException e) {

		}

	}// 「imageReduction」メソッド終了

	private void imageReduction3() {
		BitmapFactory.Options mOptions = new BitmapFactory.Options();
		mOptions.inSampleSize = 10; // サイズはここで変更できる
		Bitmap resizeBitmap = null;

		try {

			InputStream is = getContentResolver().openInputStream(uri_3);
			resizeBitmap = BitmapFactory.decodeStream(is, null, mOptions);
			is.close();

			bmpSymptomPhoto3 = resizeBitmap;
		} catch (IOException e) {

		}

	}// 「imageReduction」メソッド終了

	// ******************************************************************

	// イメージ画像の削除、最選択
	private void ordelete1() {

		// ダイアログの表示()
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// メッセージ
		ad.setMessage("画像を削除しますか？");
		// OKを押したとき
		ad.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				bmpThumnail_1 = null;
				bmpSymptomPhoto1 = null;
				imgButton1.setImageBitmap(image);

				ContentValues values = new ContentValues();
				values.put("thumnail1", dao2.bitmapToByte(bmpThumnail_1));
				values.put("symptomPhoto1", dao2.bitmapToByte(bmpSymptomPhoto1));
				dao2.update("kalte", values, k_listId);

			}
		});
		// キャンセルを押したとき
		ad.setNegativeButton("選びなおす", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				photoSelect1();
				// ダイアログを閉じる。
				dialog.cancel();
			}
		});
		ad.show();
	}

	private void ordelete2() {

		// ダイアログの表示()
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// メッセージ
		ad.setMessage("画像を削除しますか？");
		// OKを押したとき
		ad.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				bmpThumnail_2 = null;
				bmpSymptomPhoto2 = null;
				imgButton2.setImageBitmap(image);

				ContentValues values = new ContentValues();
				values.put("thumnail2", dao2.bitmapToByte(bmpThumnail_2));
				values.put("symptomPhoto2", dao2.bitmapToByte(bmpSymptomPhoto2));
				dao2.update("kalte", values, k_listId);

			}
		});
		// キャンセルを押したとき
		ad.setNegativeButton("選びなおす", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				photoSelect1();
				// ダイアログを閉じる。
				dialog.cancel();
			}
		});
		ad.show();
	}

	private void ordelete3() {

		// ダイアログの表示()
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// メッセージ
		ad.setMessage("画像を削除しますか？");
		// OKを押したとき
		ad.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				bmpThumnail_3 = null;
				bmpSymptomPhoto3 = null;
				imgButton3.setImageBitmap(image);

				ContentValues values = new ContentValues();
				values.put("thumnail3", dao2.bitmapToByte(bmpThumnail_3));
				values.put("symptomPhoto3", dao2.bitmapToByte(bmpSymptomPhoto3));
				dao2.update("kalte", values, k_listId);

			}
		});
		// キャンセルを押したとき
		ad.setNegativeButton("選びなおす", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				photoSelect1();
				// ダイアログを閉じる。
				dialog.cancel();
			}
		});
		ad.show();
	}

}
