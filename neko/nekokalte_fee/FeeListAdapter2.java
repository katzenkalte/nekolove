package neko.neko.nekokalte_fee;

import java.util.List;

import neko.neko.nekokalte.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeeListAdapter2 extends ArrayAdapter<FeeListItem2> {

	private List<FeeListItem2> items2;
	private int magic;

	// コンストラクタ
	public FeeListAdapter2(Context context, List<FeeListItem2> items) {
		super(context, -1, items);
		this.items2 = items;
	}

	// コンストラクタ終わり

	// 要素ビューが生成される時に呼ばれる
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Context context = getContext();
		FeeListItem2 item2 = items2.get(position);

		// 要素ビューの情報がnullの時はレイアウトを生成
		if (convertView == null) {
			// コンテキストからインフレーターを取得
			LayoutInflater inflater = LayoutInflater.from(context);

//			if(magic==31){
//				// 要素ビューにXMLレイアウトを配置
//				convertView = inflater.inflate(R.layout.fee_listitem_month, null);
//			}
//			if(magic!=31){
//				// 要素ビューにXMLレイアウトを配置
//				convertView = inflater.inflate(R.layout.fee_listitem_year, null);
//			}
			
//			修正＠もん
//			magicは使用していないので下記に修正
			convertView = inflater.inflate(R.layout.fee_listitem_year, null);
		}

		// TextViewの関連づけ、データ設定
		TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
		textView1.setText(""+item2.year);

		// TextViewの関連づけ、データ設定
		TextView textView2 = (TextView) convertView.findViewById(R.id.textView2);
		textView2.setText(""+item2.fee);

		// 要素ビューを返す
		return convertView;
	}
}
