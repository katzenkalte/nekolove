package neko.neko.nekokalte_weight;

import java.util.List;

import neko.neko.nekokalte.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeightListAdapter extends ArrayAdapter<WeightListItem> {

	private List<WeightListItem> items3;

	// コンストラクタ
	public WeightListAdapter(Context context, List<WeightListItem> items) {
		super(context, -1, items);
		this.items3 = items;
	}

	// コンストラクタ終わり

	// 要素ビューが生成される時に呼ばれる
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = getContext();
		WeightListItem item3 = items3.get(position);

		// 要素ビューの情報がnullの時はレイアウトを生成
		if (convertView == null) {
			// コンテキストからインフレーターを取得
			LayoutInflater inflater = LayoutInflater.from(context);

			// 要素ビューにXMLレイアウトを配置
			convertView = inflater.inflate(R.layout.weight_listitem, null);
		}

		// TextViewの関連づけ、データ設定
		TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
		textView1.setText(item3.day);

		// TextViewの関連づけ、データ設定
		TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
		textView2.setText(item3.weight);

		// TextViewの関連づけ、データ設定
		TextView textView3 = (TextView) convertView.findViewById(R.id.textView3);
		textView3.setText(item3.unit);

		// 要素ビューを返す
		return convertView;
	}
}
