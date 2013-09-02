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



//�����̴߳�����ţ���ֹ�������
public class SMSThreadPro extends Activity implements Runnable {

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

			// �����У����ܶ���Cursor����ֻ�ܽ�Cursor������Ϊ��������
			contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

			// �ַ����ıȽϱȽ����⣬�м�
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
