package neko.neko.nekokalte_weight;

public class TblLovecatRecord3 {

	// 保持データ以下項目がテーブルデータの１レコード。
	public int weight_Id;
	public int catID_id;
	public int unit_id;
	public int year;
	public int monthOfYear;
	public int dayOfMonth;

	public String w_day;
	public float weight;
	public String unit;

	// コンストラクタ
	public TblLovecatRecord3(int weight_Id, int catID_id, int unit_id,
			int year, int monthOfYear, int dayOfMonth, String w_day, float weight,
			String unit) {

		this.weight_Id = weight_Id;
		this.catID_id = catID_id;
		this.unit_id = unit_id;
		this.year = year;
		this.monthOfYear = monthOfYear;
		this.dayOfMonth = dayOfMonth;

		this.w_day = w_day;
		this.weight = weight;
		this.unit = unit;

	}
}