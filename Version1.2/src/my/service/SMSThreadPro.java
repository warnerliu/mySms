package my.service;

import my.pro.R;
import my.pro.ReadSMS;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



//利用线程处理短信，防止程序假死
public class SMSThreadPro extends Activity implements Runnable {

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

		String passwordStr = new String();
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
				smsCursor = managedQuery(uri, projection, null, null, "date desc");
			} else {
				selection = "address=" + select_number + " and read=1";
				smsCursor = managedQuery(uri, projection, selection, null, "date desc");
			}
			StringBuilder str = new StringBuilder();

			ReadSMS messagePro = new ReadSMS();

			str = messagePro.smsNumber(smsCursor, contactCursor,passwordStr);
			myShow.setText(str);

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();

		} finally {
			smsCursor.close();
			contactCursor.close();
		}
	}

}
