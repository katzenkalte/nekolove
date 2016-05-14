package neko.neko.nekokalte_kalte;

import android.graphics.Bitmap;

/*
 * tblSampleのレコードデータを保持するクラス
 * リストビューのアダプタ内やDAOで管理しやすいように、レコードのデータ保持クラスです。
 * tblSampleのデータ構成と同じにします。
 */
public class TblLovecatRecord2 {

	// 保持データ以下項目がテーブルデータの１レコード。
	public int k_listId;
	public int catID_id;
	public int genreID;
	public int year;
	public int monthOfYear;
	public int dayOfMonth;
	public int fee;

	public String day;
	public String genre;
	public String symptom;
	public String diagnosis;
	public String prescription;

	public Bitmap symptomPhoto1;
	public Bitmap symptomPhoto2;
	public Bitmap symptomPhoto3;

	public Bitmap thumnail1;
	public Bitmap thumnail2;
	public Bitmap thumnail3;

	// コンストラクタ
	public TblLovecatRecord2(int k_listId, String day, int year,
			int monthOfYear, int dayOfMonth, String genre, String symptom,
			String diagnosis, String prescription, int fee, int catID_id,
			int genreID, Bitmap symptomPhoto1, Bitmap symptomPhoto2,
			Bitmap symptomPhoto3, Bitmap thumnail1, Bitmap thumnail2,
			Bitmap thumnail3) {

		this.k_listId = k_listId;
		this.catID_id = catID_id;
		this.genreID = genreID;
		this.year = year;
		this.monthOfYear = monthOfYear;
		this.dayOfMonth = dayOfMonth;

		this.day = day;
		this.genre = genre;
		this.symptom = symptom;
		this.diagnosis = diagnosis;
		this.prescription = prescription;
		this.fee = fee;

		this.symptomPhoto1 = symptomPhoto1;
		this.symptomPhoto2 = symptomPhoto2;
		this.symptomPhoto3 = symptomPhoto3;

		this.thumnail1 = thumnail1;
		this.thumnail2 = thumnail2;
		this.thumnail3 = thumnail3;
		;

	}
}