package neko.neko.nekokalte_cat;

import java.util.List;

import neko.neko.nekokalte.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//•ImageView.java
//◦Intentのインスタンスを生成する.
//◦Intent.setTypeメソッドで, 画像全般("image/*")を指定する. jpegに限定する場合は, "image/jpeg"と指定.
//◦Intent.setActionメソッドで, Intent.ACTION_GET_CONTENTを指定する.
//◦startActivityForResultメソッドで, リクエストコードを指定してインテント呼出しする.
//◦ギャラリーでの選択結果を受け取るために, onActivityResultメソッドをオーバーライドする.
//◦リクエストコードをチェックし, ギャラリーからのイベントか判断する.
//◦Intent.getDataメソッドを使って選択された画像へのパスを取得する.
//◦ContentResolver.openInputStremaメソッドで, InputStreamをオープンする.
//◦BitmapFactory.decodeStreamメソッドで, ビットマップに変換する.
//◦InputStream.closeメソッドで, InputStreamをクローズする.

public class CatListAdapter extends ArrayAdapter<CatListItem> {
	private List<CatListItem> items;



	// コンストラクタ
	public CatListAdapter(Context context, List<CatListItem> items) {
		super(context, -1, items);
		this.items = items;
	}

	//コンストラクタ終わり




	// 要素ビューが生成される時に呼ばれる
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = getContext();
		CatListItem item = items.get(position);



		// 要素ビューの情報がnullの時はレイアウトを生成
		if (convertView == null) {
			// コンテキストからインフレーターを取得
			LayoutInflater inflater = LayoutInflater.from(context);

			// 要素ビューにXMLレイアウトを配置
			convertView = inflater.inflate(R.layout.listitem, null);
		}

		// ImageViewの関連づけ、データ設定
		ImageView imageView = (ImageView)convertView.findViewById(R.id.faceThumbNail);
		imageView.setImageBitmap(item.thumnail);


		// TextViewの関連づけ、データ設定
		TextView textView = (TextView)convertView.findViewById(R.id.nameList);
		textView.setText(item.name);

		// 要素ビューを返す
		return convertView;
	}
}