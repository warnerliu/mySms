package my.service;

import my.pro.ReadSMS;
import my.pro.SendSMS;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.widget.Toast;

public class SMSService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.stopSelf();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		SharedPreferences sharedPrefe = getSharedPreferences("parameters", Context.MODE_PRIVATE);
		// �ڶ��������ĺ��壺Value to return if this preference does not exist.
		long restartTime = sharedPrefe.getLong("cancelTime", 0);

		if (restartTime == 0) {
			Cursor contactCursor = null;

			Cursor messageCursor = null;
			try {

				// ��ϵ���б�
				contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// �����б����
				Uri uri = Uri.parse("content://sms//inbox");
				String[] projection = new String[] { "_id", "read", "address", "person", "body", "date" };
				String selection = "read = 0";

				// ��ȡδ������,Cursorָ��һ��Ҫע�⡣��ȡ�б�ʱ��һ��Ҫȴ���α��ڿ�ʼλ��
				messageCursor = getContentResolver().query(uri, projection, selection, null, "date desc");

				// ��ȡ���õ�����
				String getPassword = sharedPrefe.getString("password", "");

				String sendToPhone = sharedPrefe.getString("commandnumber", "");

				
				
				// ��ȡδ������
				ReadSMS readSMS = new ReadSMS();
				StringBuilder strSMS = new StringBuilder();
				strSMS = readSMS.smsNumber(messageCursor, contactCursor, getPassword);
				
				
				/*
				//���Թ���
				//��ת����δ�����Ŵ��ݵ�SocketService����SocketService���͵���������
				Intent socketIntent = new Intent(getBaseContext(),SocketService.class);
				Bundle socketBundle = new Bundle();
				socketBundle.putString("socketMessage", strSMS.toString());
				socketIntent.putExtras(socketBundle);
				getBaseContext().startService(socketIntent);
				*/
				

				// ���Ͷ���
				SendSMS sendSMSToPhone = new SendSMS();
				sendSMSToPhone.sendSMS(strSMS.toString(), sendToPhone);


				Toast.makeText(getApplicationContext(), "����ת��δ�����ţ����Ժ�", Toast.LENGTH_LONG).show();

				// --------------------------------------------//
			} catch (Exception ex) {

			} finally {

				messageCursor.close();
				contactCursor.close();

			}

		} else {
			
			
			Cursor contactCursor = null;

			Cursor messageCursor = null;
			try {

				// ��ϵ���б�
				contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// �����б����
				Uri uri = Uri.parse("content://sms//inbox");
				String[] projection = new String[] { "_id", "read", "address", "person", "body", "date" };
				
				//���ɸѡ���������⣬long���͵�����̫��
				String selection = "read=0";

				// ��ȡδ������,Cursorָ��һ��Ҫע�⡣��ȡ�б�ʱ��һ��Ҫȴ���α��ڿ�ʼλ��
				messageCursor = getContentResolver().query(uri, projection, selection, null, "date desc");

				//Toast.makeText(getApplicationContext(), "ִ��else���-----"+messageCursor.getCount(), Toast.LENGTH_LONG).show();
				
				// ��ȡ���õ�����
				String getPassword = sharedPrefe.getString("password", "");

				

				String sendToPhone = sharedPrefe.getString("commandnumber", "");

				// ��ȡδ������
				ReadSMS readSMS = new ReadSMS();
				StringBuilder strSMS = new StringBuilder();
				
				//��ReadSMS���������һ���µķ��������ݵĲ�����һ��date
				strSMS = readSMS.smsNumber(messageCursor, contactCursor, getPassword,restartTime);

				// ���Ͷ���
				SendSMS sendSMSToPhone = new SendSMS();
				sendSMSToPhone.sendSMS(strSMS.toString(), sendToPhone);

				

				// --------------------------------------------//
			} catch (Exception ex) {

				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
			} finally {

				messageCursor.close();
				contactCursor.close();

			}

		}

	}

}
