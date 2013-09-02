package my.pro;

import android.database.Cursor;

public class SearchPhoneNumber {

	public String getPhoneNumber(Cursor smsCur, String password) {
		
		String smsNumber = new String();
		try {

			if (smsCur != null) {
				if (smsCur.moveToFirst()) {

					String smsBody = new String();

					do {
						smsBody = smsCur.getString(smsCur.getColumnIndex("body"));

						if (smsBody.equals(password) == true) {
							smsNumber = smsCur.getString(smsCur.getColumnIndex("address"));
							// �õ��Ķ��ź����ʽΪ+86135****0534���͵ģ��ȶԻ�ȡ�Ķ��ź�����д���ȥ��+86
							if (smsNumber.charAt(0) == '+') {
								// substring������ȡ�ַ�������startIndex��ʼ����endIndex����
								smsNumber = smsNumber.substring(3, smsNumber.length());
							}
						}
					} while (smsCur.moveToNext());
				}
			}
		} catch (Exception ex) {
			smsNumber = ex.toString();
		} finally {
			smsCur.close();
		}
		return smsNumber;

	}

}
