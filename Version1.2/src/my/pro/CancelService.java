package my.pro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class CancelService extends Service {

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

		cancelHandler.postDelayed(process, 2000);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	Handler cancelHandler = new Handler();
	Runnable process = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

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
				// sharedData.putInt("rows", smsCursor.getCount());

			} catch (Exception ex) {
				sharedData.putString("error", ex.toString());
			} finally {
				sharedData.commit();
				smsCursor.close();
			}

		}

	};

}
