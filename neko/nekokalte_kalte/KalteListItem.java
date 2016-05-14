package neko.neko.nekokalte_kalte;

public class KalteListItem {
	public int k_listId;
	public String day;
	public String genre;
	public String symptom;


	// コンストラクタ
	public KalteListItem(int k_listId,String day,String genre,String symptom) {
		this.k_listId=k_listId;
		this.day = day;
		this.genre = genre;
		this.symptom = symptom;
	}
}
