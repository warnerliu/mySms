package my.pro;

//import java.util.ArrayList;

import java.util.ArrayList;

import android.app.Activity;
import android.telephony.SmsManager;

public class SendSMS extends Activity {

	public void sendSMS(String sendText, String toSendNumber) {

		// �ڷ��Ͷ��ŵ�ʱ��һ�η��͵��ַ����ܳ���70������ᱨ��
		// ����һ��SmsManager����
		SmsManager sendSMS = SmsManager.getDefault();

		// ���ڵõ��Ķ�����ϣ�ͨ���ָ�������ж�ÿ�����ŵĳ���
		// Ҫ��Ҫ�����ж��أ������������+���绰���롱+���������ݡ������ܹ����͵���󳤶��أ�
		
		String[] partSMS = sendText.split("/");
		//��forѭ���У��ж�����Ϊʲô��partSMS.length-1,�����partSMS.length,��ᷢ��һ���ն���
		//partSMS.length-1���ɶ�ȡ���ŵĸ�ʽ�����ģ��ָ�����һ���ְ����س������������һ�����ǿյģ����Ҫ��1
		for (int i = 0; i < partSMS.length - 1; i++) {
			if (partSMS[i].equals("") == false) {
				if (partSMS[i].length() > 70) {
					ArrayList<String> texts = sendSMS.divideMessage(partSMS[i]);
					for (String text : texts)
						sendSMS.sendTextMessage(toSendNumber, null, text, null, null);
				} else
					sendSMS.sendTextMessage(toSendNumber, null, partSMS[i], null, null);

			}
		}

	}

}
