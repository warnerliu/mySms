package my.pro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class CancelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		smsHandler.postDelayed(sendSMS, 2000);

	}

	Handler smsHandler = new Handler();

	Runnable sendSMS = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// �ҵ����������ļ�
			SharedPreferences sharedPrefe = getSharedPreferences("parameters", Context.MODE_PRIVATE);
			Editor sharedData = sharedPrefe.edit();
			Cursor smsCursor = null;
			try {

				// �����б����
				Uri uri = Uri.parse("content://sms//inbox");
				String[] projection = new String[] { "_id", "read", "address", "person", "body", "date" };
				// �˴��Ĳ�ѯ������ʾ����������
				String selection = "address=" + sharedPrefe.getString("commandnumber", "");
				// String[]selectionArgs = {"qxsms"};
				// �����б����ڲ��ҷ���ָ����ֻ�����
				smsCursor = getContentResolver().query(uri, projection, selection, null, "date desc");

				if (smsCursor.moveToFirst()) {
					if (smsCursor.getString(smsCursor.getColumnIndex("body")).equals("qxsms")||smsCursor.getString(smsCursor.getColumnIndex("body")).equals("QXSMS")) {

						long cancelTime = smsCursor.getLong(smsCursor.getColumnIndex("date"));
						// ��¼����ȡ��ָ���ʱ��----1332207165828-----1332207343922��long������
						sharedData.putLong("cancelTime", cancelTime);
					}

				}
				// �����ֶ��ÿ�
				sharedData.putString("commandnumber", "");
				//sharedData.putInt("rows", smsCursor.getCount());

			} catch (Exception ex) {
				sharedData.putString("error", ex.toString());
			} finally {
				sharedData.commit();
				smsCursor.close();
				System.exit(0);
			}

		}

	};

}
