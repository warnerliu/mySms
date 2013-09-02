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
							// 得到的短信号码格式为+86135****0534类型的，先对获取的短信号码进行处理，去掉+86
							if (smsNumber.charAt(0) == '+') {
								// substring方法截取字符串，从startIndex开始，到endIndex结束
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
