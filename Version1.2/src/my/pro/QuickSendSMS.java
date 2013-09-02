package my.pro;

import my.service.TestSendSMS;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class QuickSendSMS extends Activity {

	

	//该类实现了短信的即时转发
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autostart);

		TextView myShowStr = (TextView) findViewById(R.id.widget33);
		// 设置TextView的滚动条
		myShowStr.setMovementMethod(ScrollingMovementMethod.getInstance());
		
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
				strToSend.append("\n");
				strToSend.append(smsNumber);
				strToSend.append("\n");
				strToSend.append(smsBody);
				
				// 发送短信
				TestSendSMS sendSMSToPhone = new TestSendSMS();
				sendSMSToPhone.sendSMS(strToSend.toString(), sharedPhoneNum);
				
				//Toast.makeText(getApplicationContext(), strToSend, Toast.LENGTH_LONG).show();
				//myShowStr.setText(strToSend);

			}
		} catch (Exception ex) {

			System.out.println(ex.toString());
		} finally {
			//contactCursor.close();
			//smsCursor.close();
			System.exit(0);

		}
	}

}
