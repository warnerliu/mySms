package my.service;

import java.util.Random;

public class RandomString {
	
	//��������ַ���
	public String randomStr(){
		
		//��ѡ�ַ���������26����ĸ�Ĵ�Сд
		String preString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder str = new StringBuilder();
		Random randomNum = new Random();
		for(int i = 0;i<6;i++){
			int index = randomNum.nextInt(52);
			str.append(preString.charAt(index));
		}
		return str.toString();
		
	}

}
