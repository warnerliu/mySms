package my.pro;

import my.net.TestNetActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		setTitle("短信精灵");
		Button settingBtn = (Button)findViewById(R.id.setting);
		settingBtn.setOnClickListener(new settingActivity());
		
		Button selfBtn = (Button)findViewById(R.id.self);
		selfBtn.setOnClickListener(new selfActivity());
		
		Button netBtn = (Button)findViewById(R.id.net);
		netBtn.setOnClickListener(new NetActivity());
	}
	
	
	class NetActivity implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent netIntent = new Intent(Welcome.this,TestNetActivity.class);
			startActivity(netIntent);
		}
		
	}
	
	// 设置
		class settingActivity implements OnClickListener {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent settingIntent = new Intent(Welcome.this, SettingActivity.class);
				startActivity(settingIntent);
			}
		}

		//自定义功能
		class selfActivity implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent messageIntent = new Intent(Welcome.this,MessageActivity.class);
				startActivity(messageIntent);
			}
			
		}
		
		// 退出//暂时不用
		class exitListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent autoStartIntent = new Intent(MessageActivity.this,AutoStartActivity.class);
				//startActivity(autoStartIntent);

				System.exit(0);
				
			}
		}
		

}
