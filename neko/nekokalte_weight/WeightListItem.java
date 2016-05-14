package neko.neko.nekokalte_weight;

public class WeightListItem {
	public int weight_Id;
	public String day;
	public String weight;
	public String unit;


	// コンストラクタ
	public WeightListItem(int weight_Id,String day,String weight,String unit) {
		this.weight_Id=weight_Id;
		this.day = day;
		this.weight = weight;
		this.unit = unit;
	}
}
