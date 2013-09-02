package my.pro;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ReadContacts extends Activity {

	// 根据短信中的号码查找联系人姓名
	public String contactName(String smsNumber, Cursor contactCur) {
		String nameOfContact = new String();
		String phoneNumber = new String();
		int flag = 0;
		try {
			if (contactCur.moveToFirst()) {
				do {

					phoneNumber = contactCur.getString(contactCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					phoneNumber = phoneNumber.replace("-", "");
					if (smsNumber.equals(phoneNumber) == true) {
						nameOfContact = contactCur.getString(contactCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						flag = 1;
					}

				} while (contactCur.moveToNext());
				if (flag == 0)
					nameOfContact = null;
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());

		} finally {
			//不能在这个类中将contactCur关闭，否则无法获取姓名
			//contactCur.close();
		}
		return nameOfContact;
	}

}
