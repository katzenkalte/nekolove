package neko.neko.nekokalte;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class NekoKalteTitle extends Activity{

//■「onCreate」メソッド開始
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.title);
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 2000);

	}

//■【ローカルクラス】
	class splashHandler implements Runnable {
		public void run() {
			//インテント・インスタンスの生成
			Intent i = new Intent(getApplication(), TopMenu.class);
			startActivity(i);
			NekoKalteTitle.this.finish();
		}


	}


}