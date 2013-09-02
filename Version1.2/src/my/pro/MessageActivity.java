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
	//当短信内容是预先设定的密码时，不进行转发
	private String passwordStr = new String();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("自定义查询");
		// 获取短信
		Button myButton = (Button) findViewById(R.id.sms);
		//myButton.setOnClickListener(new myButtonListener());
		//利用线程处理短信
		myButton.setOnClickListener(new ThreadSMSPro());
		// 获取联系人
		// Button contacts_button = (Button) findViewById(R.id.contacts);
		// contacts_button.setOnClickListener(new contacts_buttonListener());
		// 发短信
		Button sendSMS = (Button) findViewById(R.id.sendSMS);
		sendSMS.setOnClickListener(new sendMessage());
		
		/*
		// 设置
		Button settingBtn = (Button) findViewById(R.id.settingButton);
		settingBtn.setOnClickListener(new settingActivity());
		//退出
		Button exitBtn = (Button)findViewById(R.id.exitButton);
		exitBtn.setOnClickListener(new exitListener());
*/
		

	}

	// 读取短信列表
	class myButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			TextView myShow = (TextView) findViewById(R.id.showview);
			// 设置TextView的滚动条
			myShow.setMovementMethod(ScrollingMovementMethod.getInstance());

			// 读取输入的电话号码，查找相应的短信
			EditText phoneNumSMS = (EditText) findViewById(R.id.searchSMS);

			Uri uri = Uri.parse("content://sms//inbox");
			String[] projection = new String[] { "_id", "read", "address", "person", "body","data" };
			// person字段是一个不稳定的值，无法通过person值获得相应的联系人姓名。
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

				// 在类中，不能定义Cursor对象，只能将Cursor对象作为参数传递
				contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// 字符串的比较比较特殊，切记
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

	// 读取联系人列表//在此程序中没有用到
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
					// 通过从ContactsContract.Comtacts表中取得电话的_ID号，然后根据_ID号从CommonDataKinds.Phone表中找到对应的姓名
					name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// 筛选条件可能有问题
					String selection = ContactsContract.CommonDataKinds.Phone._ID + " = " + phoneID;

					Cursor phoneCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection, null, null);
					try {

						// 当一个联系人有多个电话号码时，只能分开显示，无法显示在 一组中
						// 需要进一步测试
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

	// 发送短信
	class sendMessage implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// String sendNumber = new String();
			TextView myShow = (TextView) findViewById(R.id.showview);
			// 设置TextView的滚动条
			myShow.setMovementMethod(ScrollingMovementMethod.getInstance());

			EditText toPhoneNumber = (EditText) findViewById(R.id.toPhoneNum);
			String toSendNumber = toPhoneNumber.getText().toString();

			SendSMS sms_send = new SendSMS();
			if (toSendNumber.equals("") == false)
				sms_send.sendSMS(sendStr, toSendNumber);
			else
				Toast.makeText(getApplicationContext(), "请输入要转发的号码！", Toast.LENGTH_SHORT).show();
			myShow.setText(sendStr);
			myShow.setTextColor(Color.BLACK);

		}

	}
/*
	// 设置
	class settingActivity implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent settingIntent = new Intent(MessageActivity.this, SettingActivity.class);
			startActivity(settingIntent);
		}
	}

	// 退出
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
	// 线程程序设计
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
			// 设置TextView的滚动条
			myShow.setMovementMethod(ScrollingMovementMethod.getInstance());

			// 读取输入的电话号码，查找相应的短信
			EditText phoneNumSMS = (EditText) findViewById(R.id.searchSMS);

			Uri uri = Uri.parse("content://sms//inbox");
			String[] projection = new String[] { "_id", "read", "address", "person", "body" };
			// person字段是一个不稳定的值，无法通过person值获得相应的联系人姓名。
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

				// 在类中，不能定义Cursor对象，只能将Cursor对象作为参数传递
				contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// 字符串的比较比较特殊，切记
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
    android:text="设置"
    android:background="@drawable/button_selector" />

<Button
    android:id="@+id/exitButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_alignParentRight="true"
    android:layout_alignParentTop="true"
    android:text="退出"
    android:background="@drawable/button_selector" />

</RelativeLayout>

*/





