package neko.neko.nekokalte_cat;

import android.graphics.Bitmap;

/*
 * LovecatDaoのレコードデータを保持するクラス
 * リストビューのアダプタ内やDAOで管理しやすいように、レコードのデータ保持クラスです。
 * LovecatDaoのデータ構成と同じにします。
 */
public class TblLovecatRecord {

	// 保持データ以下項目がテーブルデータの１レコード。
	public int catID_id;
	public int sexID;
	public int ageUnitID;
	public int weightUnitID;

	public String name;
	public String sex;
	public String age;
	public String ageUnit;
	public String weight;
	public String weightUnit;
	public String hairColor;

	public Bitmap facePhoto;
	public Bitmap thumnail;


	// コンストラクタ
	public TblLovecatRecord(int catID_id, int sexID, int ageUnitID, int weightUnitID,
			String name,  String sex, String age,
			String ageUnit, String weight, String weightUnit, String hairColor,
			Bitmap facePhoto, Bitmap thumnail) {

		this.catID_id = catID_id;
		this.sexID = sexID;
		this.ageUnitID = ageUnitID;
		this.weightUnitID = weightUnitID;


		this.name = name;
		this.sex = sex;
		this.age = age;
		this.ageUnit = ageUnit;
		this.weight = weight;
		this.weightUnit = weightUnit;
		this.hairColor = hairColor;

		this.facePhoto = facePhoto;
		this.thumnail = thumnail;

	}
}