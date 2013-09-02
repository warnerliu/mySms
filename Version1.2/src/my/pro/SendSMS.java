package my.pro;

//import java.util.ArrayList;

import java.util.ArrayList;

import android.app.Activity;
import android.telephony.SmsManager;

public class SendSMS extends Activity {

	public void sendSMS(String sendText, String toSendNumber) {

		// 在发送短信的时候，一次发送的字符不能超过70，否则会报错
		// 创建一个SmsManager对象
		SmsManager sendSMS = SmsManager.getDefault();

		// 对于得到的短信组合，通过分割后无需判断每条短信的长度
		// 要不要进行判断呢？如果“姓名”+“电话号码”+“短信内容”超过能够发送的最大长度呢？
		
		String[] partSMS = sendText.split("/");
		//在for循环中，判断条件为什么是partSMS.length-1,如果是partSMS.length,则会发送一条空短信
		//partSMS.length-1是由读取短信的格式决定的，分割后，最后一部分包含回车符，所以最后一条会是空的，因此要减1
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
