package neko.neko.nekokalte_cat;

import android.graphics.Bitmap;

/*
 * リストビューの要素内情報を管理するクラス（独自クラス）
 */
public class CatListItem {
	public int catID_id;
	public Bitmap thumnail;
	public String name;

	// コンストラクタ
	public CatListItem(int catID_id,Bitmap thumnail, String name) {
		this.catID_id=catID_id;
		this.thumnail = thumnail;
		this.name = name;
	}
}
