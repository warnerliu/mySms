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
		setTitle("����");
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

			// getSharedPreferences(name,mode)�����ĵ�һ����������ָ�����ļ������ƣ����Ʋ��ô���׺����׺����Android�Զ����ϡ�
			// �����ĵڶ�������ָ���ļ��Ĳ���ģʽ
			// parameters����������������
			SharedPreferences sharedPref = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			// ����Ѿ��趨�����룬�Ȱ������ȡ����
			String existPassword = sharedPref.getString("password", "");

			// ���ж��Ƿ��趨�����룬�����δ�趨���룬��ִ�����²���
			if (existPassword.equals("") == true) {

				Editor sharedata = sharedPref.edit();

				sharedata.putString("password", edittext1.getText().toString());
				//sharedata.putString("originalPassword", "");

				sharedata.commit();

				Toast.makeText(getApplicationContext(), "����ɹ���", Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(getApplicationContext(), "���Ѿ��趨�����룬�����������޸����룡", Toast.LENGTH_SHORT).show();
			}
			// ����ı��������
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
					Toast.makeText(getApplicationContext(), "��������ȷ�����벻һ�£����������룡", Toast.LENGTH_SHORT).show();
					textPWD1.setText("");
					textPWD2.setText("");
				} else {
					Editor sharedata = sharedPref.edit();
					sharedata.putString("password", newPWD1);
					sharedata.commit();
					Toast.makeText(getApplicationContext(), "�����޸ĳɹ���", Toast.LENGTH_SHORT).show();
					textPWD.setText("");
					textPWD1.setText("");
					textPWD2.setText("");
				}

			} else {
				Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_SHORT).show();
				textPWD.setText("");
			}

		}

	}

}
