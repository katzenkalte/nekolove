package neko.neko.nekokalte_kalte;

import java.util.List;

import neko.neko.nekokalte.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KalteListAdapter extends ArrayAdapter<KalteListItem> {

	private List<KalteListItem> items2;

	// コンストラクタ
	public KalteListAdapter(Context context, List<KalteListItem> items) {
		super(context, -1, items);
		this.items2 = items;
	}

	// コンストラクタ終わり

	// 要素ビューが生成される時に呼ばれる
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = getContext();
		KalteListItem item2 = items2.get(position);

		// 要素ビューの情報がnullの時はレイアウトを生成
		if (convertView == null) {
			// コンテキストからインフレーターを取得
			LayoutInflater inflater = LayoutInflater.from(context);

			// 要素ビューにXMLレイアウトを配置
			convertView = inflater.inflate(R.layout.kalte_listitem, null);
		}

		// TextViewの関連づけ、データ設定
		TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
		textView1.setText(item2.day);

		// TextViewの関連づけ、データ設定
		TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
		textView2.setText(item2.genre);

		// TextViewの関連づけ、データ設定
		TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
		textView3.setText(item2.symptom);

		// 要素ビューを返す
		return convertView;
	}
}
