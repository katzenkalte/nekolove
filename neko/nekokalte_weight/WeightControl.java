package neko.neko.nekokalte_weight;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import neko.neko.nekokalte.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WeightControl extends Activity implements View.OnClickListener,
		DatePicker.OnDateChangedListener {

	// ■フィールド設定
	// ****************************************************************************
	// XML関連付け用変数
	private TextView title;// 「体重を入力してください」
	private TextView date;// 現在の日付
	private DatePicker w_day;// 体重管理用日付
	private EditText edtWeight;// 入力された体重
	private RadioGroup r_group;// ラジオボタンのグループ
	private RadioButton rBtn;// 選択された体重の単位
	private Button btn1;// OKボタン
	private Button btn2;// 編集ボタン

	private int catID_id;// 個体識別用ID

	// DB使用宣言
	ArrayList<TblLovecatRecord3> record3;
	private LovecatDao3 dao3;
	private TblLovecatRecord3 r3;
	private int weight_Id;
	// Resources r;

	// 日付設定ダイアログのインスタンスを格納するフィールド
	private int year;
	private int monthOfYear;
	private int dayOfMonth;
	Calendar calendar;
	private int magic;

	// ****************************************************************************
	// フィールド設定終わり

	// 「onCreate」メソッド開始
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weightcontrol);

		// これを入れないと最小化されたレイアウトが表示されてしまう
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		// 中断されてもいいようにリザルトコードをセット
		setResult(RESULT_CANCELED);

		// XMLとの関連付け
		title = (TextView) findViewById(R.id.textView1);
		date = (TextView) findViewById(R.id.textView2);
		w_day = (DatePicker) findViewById(R.id.datePicker1);

		r_group = (RadioGroup) findViewById(R.id.radioGroup1);

		edtWeight = (EditText) findViewById(R.id.editText1);

		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(this);

		btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(this);

		magic = 0;
	}// 「onCreat」メソッド終了

	// 「onStart」メソッド開始
	@Override
	protected void onStart() {
		super.onStart();

		// （初期設定）現在日時の設定
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		monthOfYear = calendar.get(Calendar.MONTH);
		dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		// DatePickerの設置
		w_day.init(year, monthOfYear, dayOfMonth, this);

		// DatePicker上部の日付表示設定
		calendar.set(year, monthOfYear, dayOfMonth);
		SimpleDateFormat s = new SimpleDateFormat(
				"yyyy'年 'MM'月 'dd'日（'EEE'曜日）'", Locale.JAPANESE);
		Date currentTime = calendar.getTime();
		date.setText(s.format(currentTime));// 「****年**月**日*曜日の表示ができる」

		// インテントでデータ取得
		Bundle b = getIntent().getExtras();
		if (b != null) {
			catID_id = b.getInt("catID_id");
			magic = b.getInt("key");

			// 新規追加ボタンから来たとき
			if (magic == 21) {
				// ボタンの表示設定
				btn1.setVisibility(View.VISIBLE);
				btn2.setVisibility(View.GONE);
			}

			// 編集ボタン（onItemClick）から来たとき
			if (magic == 22) {
				// パラメーターよりIDを取得
				weight_Id = b.getInt("weight_Id");

				// ボタンの表示設定
				btn1.setVisibility(View.GONE);
				btn2.setVisibility(View.VISIBLE);

				// DBより該当するレコードを取得
				dao3 = new LovecatDao3(this);
				r3 = dao3.getRecord2(weight_Id);

				// （確認画面用）各コンポーネントに該当フィールドの値をセット
				year = r3.year;
				monthOfYear = r3.monthOfYear;
				dayOfMonth = r3.dayOfMonth;
				edtWeight.setTextSize(r3.weight);

				// DatePicker上部の日付表示設定
				calendar.set(year, monthOfYear, dayOfMonth);
				s = new SimpleDateFormat("yyyy'年 'MM'月 'dd'日（'EEE'曜日）'",
						Locale.JAPANESE);
				currentTime = calendar.getTime();
				date.setText(s.format(currentTime));// 「****年**月**日*曜日の表示ができる」

				// DatePickerの日付設定
				w_day.init(year, monthOfYear, dayOfMonth, this);

				if (r3.unit_id == 0) {
					r_group.check(R.id.radio0);
				} else if (r3.unit_id == 1) {
					r_group.check(R.id.radio1);
				}// ラジオボタンの設定終わり
			}// 編集ボタンの設定終わり
		}// インテントデータ取得終わり
	}// 「onStart」メソッド終了

	// 「onClick」メソッド開始
	public void onClick(View v) {

		// ユーザーが設定した日付を取得
		String w_day = "" + this.w_day.getYear() + ". "
				+ new DecimalFormat("00").format(this.w_day.getMonth() + 1)
				+ ". "
				+ new DecimalFormat("00").format(this.w_day.getDayOfMonth());

		if (v == btn1) {
			dao3 = new LovecatDao3(this);
			r3 = dao3.getRecord3(catID_id, w_day);
			// ①DBがnullでないとき→同じ日付がDBに登録されているとき→update*********************
			if (r3 != null) {
				rBtn = (RadioButton) findViewById(r_group
						.getCheckedRadioButtonId());
				ContentValues values = new ContentValues();
				values.put("catID_id", catID_id);
				values.put(
						"w_day",
						year
								+ ". "
								+ new DecimalFormat("00")
										.format(monthOfYear + 1) + ". "
								+ new DecimalFormat("00").format(dayOfMonth)); // 日付
				values.put("year", year);
				values.put("monthOfYear", monthOfYear);
				values.put("dayOfMonth", dayOfMonth);
				values.put("weight", edtWeight.getText().toString());
				values.put("unit", rBtn.getText().toString());// 選択されたラジオボタンの文字列
				if (rBtn.getText().toString().equals("g")) {
					values.put("unit_id", 0); // 選択されたラジオボタンのID
				} else {
					values.put("unit_id", 1); // 選択されたラジオボタンのID
				}
				dao3.update("weight_control", values, r3.weight_Id);
				Toast.makeText(this,
						"新規登録でnullでないときかつ日付が同じとき、weight_Id " + weight_Id,
						Toast.LENGTH_LONG).show();
				// ①終わり**************************************************************************

				//上書きしたデータをもとのアクティビティに返す
				Intent intent = new Intent();
				intent.putExtra("catID_id", catID_id);
				setResult(Activity.RESULT_OK, intent);

				finish();
				// nullでないときの処理終わり*************************************************************

			} else {
				// ②DBがnullでないとき→同じ日付がDBに登録されていないとき→insert***********************
				rBtn = (RadioButton) findViewById(r_group
						.getCheckedRadioButtonId());
				ContentValues values = new ContentValues();
				values.put("catID_id", catID_id);
				values.put(
						"w_day",
						year
								+ ". "
								+ new DecimalFormat("00")
										.format(monthOfYear + 1) + ". "
								+ new DecimalFormat("00").format(dayOfMonth)); // 日付
				values.put("year", year);
				values.put("monthOfYear", monthOfYear);
				values.put("dayOfMonth", dayOfMonth);
				values.put("weight", edtWeight.getText().toString());
				values.put("unit", rBtn.getText().toString());// 選択されたラジオボタンの文字列
				if (rBtn.getText().toString().equals("g")) {
					values.put("unit_id", 0); // 選択されたラジオボタンのID
				} else {
					values.put("unit_id", 1); // 選択されたラジオボタンのID
				}
				dao3.insert("weight_control", values);
				Intent intent = new Intent();
				intent.putExtra("catID_id", catID_id);
				setResult(Activity.RESULT_OK, intent);

				finish();

			}// ②終わり**************************************************************************

		}// 新規追加終わり

		/*
		 * 編集からの処理 ④DBがnullでないとき→同じ日付がDBに登録されているとき→update
		 * ⑤DBがnullでないとき→同じ日付がDBに登録されていないとき→insert
		 * ⑥DBがnullのとき→何もないときはonItemClickは押せないので処理無し、
		 */
		if (v == btn2) {
			
			dao3 = new LovecatDao3(this);
//**		r3 = dao3.getRecord(catID_id);
			r3 = dao3.getRecord3(catID_id, w_day);
			
			if (r3 != null) {
				// ④DBがnullでないとき→同じ日付がDBに登録されているとき→update
//**			if (w_day.equals(r3.w_day)) {
					rBtn = (RadioButton) findViewById(r_group
							.getCheckedRadioButtonId());
					ContentValues values = new ContentValues();
					values.put("catID_id", catID_id);
					values.put(
							"w_day",
							year
									+ ". "
									+ new DecimalFormat("00")
											.format(monthOfYear + 1)
									+ ". "
									+ new DecimalFormat("00")
											.format(dayOfMonth)); // 日付
					values.put("year", year);
					values.put("monthOfYear", monthOfYear);
					values.put("dayOfMonth", dayOfMonth);
					values.put("weight", edtWeight.getText().toString());
					values.put("unit", rBtn.getText().toString());// 選択されたラジオボタンの文字列
					// 選択されたラジオボタンのID
					if (rBtn.getText().toString().equals("g")) {
						values.put("unit_id", 0); // 選択されたラジオボタンのID
					} else {
						values.put("unit_id", 1); // 選択されたラジオボタンのID
					}
					dao3.update("weight_control", values, r3.weight_Id);
					Toast.makeText(this,
							"編集でnullでないときかつ日付が同じとき、weight_Id " + weight_Id,
							Toast.LENGTH_LONG).show();
					if (r3.weight_Id != weight_Id){
						dao3.delete(weight_Id);	
					}
					
				
				
				Intent intent = new Intent();
				intent.putExtra("catID_id", catID_id);
				intent.putExtra("weight_Id", r3.weight_Id);
				setResult(Activity.RESULT_OK, intent);

				finish();
				// nullでないときの処理終わり**********************************************************
				
			} else {
				// 追加：日付の変更処理を行う＠門角
				rBtn = (RadioButton) findViewById(r_group
						.getCheckedRadioButtonId());
				ContentValues values = new ContentValues();
				values.put("catID_id", catID_id);
				values.put(
						"w_day",
						year
								+ ". "
								+ new DecimalFormat("00")
										.format(monthOfYear + 1)
								+ ". "
								+ new DecimalFormat("00")
										.format(dayOfMonth)); // 日付
				values.put("year", year);
				values.put("monthOfYear", monthOfYear);
				values.put("dayOfMonth", dayOfMonth);
				values.put("weight", edtWeight.getText().toString());
				values.put("unit", rBtn.getText().toString());// 選択されたラジオボタンの文字列
				// 選択されたラジオボタンのID
				if (rBtn.getText().toString().equals("g")) {
					values.put("unit_id", 0); // 選択されたラジオボタンのID
				} else {
					values.put("unit_id", 1); // 選択されたラジオボタンのID
				}
				dao3.update("weight_control", values, weight_Id);
				
				Intent intent = new Intent();
				intent.putExtra("catID_id", catID_id);
				intent.putExtra("weight_Id", weight_Id);
				setResult(Activity.RESULT_OK, intent);

				finish();
				
				
				// ⑥DBがnullのとき→何もないときはonItemClickは押せないので処理無し、**************
			}// ⑥終わり**************************************************************************

		}// 編集終わり

	}// 「onClick」メソッド終了

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		this.year = year;
		this.monthOfYear = monthOfYear;
		this.dayOfMonth = dayOfMonth;

		// 年月日曜日の表示設定
		calendar.set(year, monthOfYear, dayOfMonth);
		SimpleDateFormat s = new SimpleDateFormat(
				"yyyy'年 'MM'月 'dd'日（'EEE'曜日）'", Locale.JAPANESE);
		Date currentTime = calendar.getTime();

		date.setText(s.format(currentTime));// 「****年**月**日*曜日の表示ができる」

	}

}
