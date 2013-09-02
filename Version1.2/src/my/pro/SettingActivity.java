package my.pro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		setTitle("设置");
		Button saveBtn = (Button) findViewById(R.id.saveBtn);
		Button changeBtn = (Button) findViewById(R.id.button1);

		saveBtn.setOnClickListener(new saveListener());
		changeBtn.setOnClickListener(new changePasswordListener());
	}

	class saveListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			EditText edittext1 = (EditText) findViewById(R.id.editText1);

			// getSharedPreferences(name,mode)方法的第一个参数用于指定该文件的名称，名称不用带后缀，后缀会由Android自动加上。
			// 方法的第二个参数指定文件的操作模式
			// parameters用来保存现用密码
			SharedPreferences sharedPref = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			// 如果已经设定过密码，先把密码读取出来
			String existPassword = sharedPref.getString("password", "");

			// 先判断是否设定过密码，如果尚未设定密码，则执行以下操作
			if (existPassword.equals("") == true) {

				Editor sharedata = sharedPref.edit();

				sharedata.putString("password", edittext1.getText().toString());
				//sharedata.putString("originalPassword", "");

				sharedata.commit();

				Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(getApplicationContext(), "您已经设定过密码，如有疑问请修改密码！", Toast.LENGTH_SHORT).show();
			}
			// 清除文本框的内容
			edittext1.setText("");

		}

	}

	class changePasswordListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			SharedPreferences sharedPref = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			String existPassword = sharedPref.getString("password", "");

			EditText textPWD = (EditText) findViewById(R.id.editText2);
			EditText textPWD1 = (EditText) findViewById(R.id.editText3);
			EditText textPWD2 = (EditText) findViewById(R.id.editText4);

			String originalPWD = textPWD.getText().toString();
			String newPWD1 = textPWD1.getText().toString();
			String newPWD2 = textPWD2.getText().toString();

			if (originalPWD.equals(existPassword) == true) {

				if (newPWD1.equals(newPWD2) == false) {
					Toast.makeText(getApplicationContext(), "新密码与确认密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
					textPWD1.setText("");
					textPWD2.setText("");
				} else {
					Editor sharedata = sharedPref.edit();
					sharedata.putString("password", newPWD1);
					sharedata.commit();
					Toast.makeText(getApplicationContext(), "密码修改成功！", Toast.LENGTH_SHORT).show();
					textPWD.setText("");
					textPWD1.setText("");
					textPWD2.setText("");
				}

			} else {
				Toast.makeText(getApplicationContext(), "密码错误！", Toast.LENGTH_SHORT).show();
				textPWD.setText("");
			}

		}

	}

}
