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

	

	//����ʵ���˶��ŵļ�ʱת��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autostart);

		TextView myShowStr = (TextView) findViewById(R.id.widget33);
		// ����TextView�Ĺ�����
		myShowStr.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		Cursor contactCursor = null;
		try {
			//getIntent():Return the intent that started this activity. 
			String smsBody = (String) getIntent().getExtras().get("messageBody");
			//�Ӷ����ж�ȡ�ĺ�����+86��ͷ�ģ���Ҫȥ��
			String smsNumber = (String) getIntent().getExtras().get("messageAddress");
			if (smsNumber.charAt(0) == '+') {
				smsNumber = smsNumber.substring(3, smsNumber.length());
			}
			
			
			// �����У����ܶ���Cursor����ֻ�ܽ�Cursor������Ϊ��������
			contactCursor = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

			
			// ��ȡ���õ�����
			SharedPreferences sharedPrefe = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			
			String sharedPhoneNum = new String();
			sharedPhoneNum = sharedPrefe.getString("commandnumber", "");
			
			
			
			
			//��Ϊ��command number�ֶβ���ʱִ�и�activity�������������ж�sharedPhoneNum�Ƿ�Ϊ��
			if (sharedPhoneNum.equals("") == false) {
				// �洢���ҵ�������
				String nameofContact = new String();
				ReadContacts readContactsName = new ReadContacts();
				nameofContact = readContactsName.contactName(smsNumber, contactCursor);				
				
				StringBuilder strToSend = new StringBuilder();
				
				strToSend.append(nameofContact);
				strToSend.append("\n");
				strToSend.append(smsNumber);
				strToSend.append("\n");
				strToSend.append(smsBody);
				
				// ���Ͷ���
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
