package my.pro;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private String sendStr = new String();
	//������������Ԥ���趨������ʱ��������ת��
	private String passwordStr = new String();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("�Զ����ѯ");
		// ��ȡ����
		Button myButton = (Button) findViewById(R.id.sms);
		//myButton.setOnClickListener(new myButtonListener());
		//�����̴߳������
		myButton.setOnClickListener(new ThreadSMSPro());
		// ��ȡ��ϵ��
		// Button contacts_button = (Button) findViewById(R.id.contacts);
		// contacts_button.setOnClickListener(new contacts_buttonListener());
		// ������
		Button sendSMS = (Button) findViewById(R.id.sendSMS);
		sendSMS.setOnClickListener(new sendMessage());
		
		/*
		// ����
		Button settingBtn = (Button) findViewById(R.id.settingButton);
		settingBtn.setOnClickListener(new settingActivity());
		//�˳�
		Button exitBtn = (Button)findViewById(R.id.exitButton);
		exitBtn.setOnClickListener(new exitListener());
*/
		

	}

	// ��ȡ�����б�
	class myButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			TextView myShow = (TextView) findViewById(R.id.showview);
			// ����TextView�Ĺ�����
			myShow.setMovementMethod(ScrollingMovementMethod.getInstance());

			// ��ȡ����ĵ绰���룬������Ӧ�Ķ���
			EditText phoneNumSMS = (EditText) findViewById(R.id.searchSMS);

			Uri uri = Uri.parse("content://sms//inbox");
			String[] projection = new String[] { "_id", "read", "address", "person", "body","data" };
			// person�ֶ���һ�����ȶ���ֵ���޷�ͨ��personֵ�����Ӧ����ϵ��������
			// String selection = "read=?";
			// String[] selectionArgs = { "1" };

			String select_number = phoneNumSMS.getText().toString();
			String selection = new String();

			Cursor smsCursor = null;
			Cursor contactCursor = null;
			
			
			try {
				Context context = createPackageContext("my.pro", Context.CONTEXT_IGNORE_SECURITY);
				SharedPreferences sharedPrefe = context.getSharedPreferences("parameters", Context.MODE_PRIVATE);
				passwordStr = sharedPrefe.getString("password", "");
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			try {

				// �����У����ܶ���Cursor����ֻ�ܽ�Cursor������Ϊ��������
				contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// �ַ����ıȽϱȽ����⣬�м�
				if (select_number.equals("") == true) {
					smsCursor = managedQuery(uri, null, null, null, "date desc");
				} else {
					selection = "address=" + select_number + " and read=1";
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
				}
				StringBuilder str = new StringBuilder();

				ReadSMS messagePro = new ReadSMS();

				str = messagePro.smsNumber(smsCursor, contactCursor,passwordStr);
				myShow.setText(str);

				sendStr = str.toString();

			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();

			} finally {
				smsCursor.close();
				contactCursor.close();
			}
		}
	}

	// ��ȡ��ϵ���б�//�ڴ˳�����û���õ�
	class contacts_buttonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView contacts_show = (TextView) findViewById(R.id.showview);
			contacts_show.setMovementMethod(ScrollingMovementMethod.getInstance());

			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			Cursor contact_cur = null;

			try {
				contact_cur = getContentResolver().query(uri, null, null, null, null);
				StringBuilder str = new StringBuilder();

				str.append(processResult(contact_cur, contacts_show));

				if (contact_cur == null)
					contacts_show.setText("The Contacts Table is Empty!");

			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
			} finally {
				contact_cur.close();
			}

		}

		// method
		private StringBuilder processResult(Cursor cur, TextView show) {
			StringBuilder str = new StringBuilder();

			if (cur.moveToFirst()) {

				String phoneID;
				String name;
				String getPhoneNum;

				do {
					phoneID = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
					// ͨ����ContactsContract.Comtacts����ȡ�õ绰��_ID�ţ�Ȼ�����_ID�Ŵ�CommonDataKinds.Phone�����ҵ���Ӧ������
					name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// ɸѡ��������������
					String selection = ContactsContract.CommonDataKinds.Phone._ID + " = " + phoneID;

					Cursor phoneCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection, null, null);
					try {

						// ��һ����ϵ���ж���绰����ʱ��ֻ�ֿܷ���ʾ���޷���ʾ�� һ����
						// ��Ҫ��һ������
						if (phoneCur.moveToFirst()) {
							do {
								getPhoneNum = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								// getPhoneNum += getPhoneNum;
								str.append("{" + name + "----" + getPhoneNum + "}");
							} while (phoneCur.moveToNext());

						}

					} catch (Exception ex) {
						Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
					} finally {
						// phoneCur.close();
					}

					// str.append(name+":"+getPhoneNum);

					str.append("\n");
					show.setText(str);

				} while (cur.moveToNext());

			}

			return str;
		}

	}

	// ���Ͷ���
	class sendMessage implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// String sendNumber = new String();
			TextView myShow = (TextView) findViewById(R.id.showview);
			// ����TextView�Ĺ�����
			myShow.setMovementMethod(ScrollingMovementMethod.getInstance());

			EditText toPhoneNumber = (EditText) findViewById(R.id.toPhoneNum);
			String toSendNumber = toPhoneNumber.getText().toString();

			SendSMS sms_send = new SendSMS();
			if (toSendNumber.equals("") == false)
				sms_send.sendSMS(sendStr, toSendNumber);
			else
				Toast.makeText(getApplicationContext(), "������Ҫת���ĺ��룡", Toast.LENGTH_SHORT).show();
			myShow.setText(sendStr);
			myShow.setTextColor(Color.BLACK);

		}

	}
/*
	// ����
	class settingActivity implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent settingIntent = new Intent(MessageActivity.this, SettingActivity.class);
			startActivity(settingIntent);
		}
	}

	// �˳�
	class exitListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Intent autoStartIntent = new Intent(MessageActivity.this,AutoStartActivity.class);
			//startActivity(autoStartIntent);

			System.exit(0);
			
		}

	}
*/
	// �̳߳������
	class ThreadSMSPro implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//SMSThreadPro smsRunnable = new SMSThreadPro();
			smsHandler.post(smsRunnable);
			//smsHandler.removeCallbacks(smsRunnable);

		}

	}

	Handler smsHandler = new Handler();
	
	Runnable smsRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			TextView myShow = (TextView) findViewById(R.id.showview);
			// ����TextView�Ĺ�����
			myShow.setMovementMethod(ScrollingMovementMethod.getInstance());

			// ��ȡ����ĵ绰���룬������Ӧ�Ķ���
			EditText phoneNumSMS = (EditText) findViewById(R.id.searchSMS);

			Uri uri = Uri.parse("content://sms//inbox");
			String[] projection = new String[] { "_id", "read", "address", "person", "body" };
			// person�ֶ���һ�����ȶ���ֵ���޷�ͨ��personֵ�����Ӧ����ϵ��������
			// String selection = "read=?";
			// String[] selectionArgs = { "1" };

			String select_number = phoneNumSMS.getText().toString();
			String selection = new String();

			Cursor smsCursor = null;
			Cursor contactCursor = null;

			try {
				Context context = createPackageContext("my.pro", Context.CONTEXT_IGNORE_SECURITY);
				SharedPreferences sharedPrefe = context.getSharedPreferences("parameters", Context.MODE_PRIVATE);
				passwordStr = sharedPrefe.getString("password", "");
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {

				// �����У����ܶ���Cursor����ֻ�ܽ�Cursor������Ϊ��������
				contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// �ַ����ıȽϱȽ����⣬�м�
				if (select_number.equals("") == true) {
					selection = "read = 0";
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
				} else {
					selection = "address=" + select_number + " and read=0";
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
				}
				StringBuilder str = new StringBuilder();

				ReadSMS messagePro = new ReadSMS();

				str = messagePro.smsNumber(smsCursor, contactCursor,passwordStr);
				myShow.setText(str);

				sendStr = str.toString();

			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();

			} finally {
				smsCursor.close();
				contactCursor.close();
			}
		}

	};
	

}
/*
<RelativeLayout
android:id="@+id/relativeLayout1"
android:layout_width="match_parent"
android:layout_height="wrap_content" >

<Button
    android:id="@+id/settingButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_alignParentLeft="true"
    android:layout_alignParentTop="true"
    android:text="����"
    android:background="@drawable/button_selector" />

<Button
    android:id="@+id/exitButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_alignParentRight="true"
    android:layout_alignParentTop="true"
    android:text="�˳�"
    android:background="@drawable/button_selector" />

</RelativeLayout>

*/





