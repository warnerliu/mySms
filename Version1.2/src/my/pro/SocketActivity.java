package my.pro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import my.service.TestSendSMS;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

public class SocketActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);

	
		
		String host = "10.0.2.2";
		int port = 8080;
		Socket socket = null;
		// BufferedReader in = null;
		PrintWriter out = null;
		String msSend = "socket test";
		
		Cursor contactCursor = null;
		try {
			//getIntent():Return the intent that started this activity. 
			String smsBody = (String) getIntent().getExtras().get("messageBody");
			//从短信中读取的号码是+86开头的，需要去掉
			String smsNumber = (String) getIntent().getExtras().get("messageAddress");
			if (smsNumber.charAt(0) == '+') {
				smsNumber = smsNumber.substring(3, smsNumber.length());
			}
			
			
			// 在类中，不能定义Cursor对象，只能将Cursor对象作为参数传递
			contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

			
			// 读取设置的密码
			SharedPreferences sharedPrefe = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			
			String sharedPhoneNum = new String();
			sharedPhoneNum = sharedPrefe.getString("commandnumber", "");
			
			
			
			
			//因为当command number字段不空时执行该activity，所以无需再判断sharedPhoneNum是否为空
			if (sharedPhoneNum.equals("") == false) {
				// 存储查找到的姓名
				String nameofContact = new String();
				ReadContacts readContactsName = new ReadContacts();
				nameofContact = readContactsName.contactName(smsNumber, contactCursor);				
				
				StringBuilder strToSend = new StringBuilder();
				
				strToSend.append(nameofContact);
				strToSend.append("-");
				strToSend.append(smsNumber);
				strToSend.append("-");
				strToSend.append(smsBody);
				
				msSend = strToSend.toString();
				
				System.out.println(msSend);

			}
		} catch (Exception ex) {

			System.out.println(ex.toString());
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
		} finally {
			System.exit(0);
		}

	}

}
