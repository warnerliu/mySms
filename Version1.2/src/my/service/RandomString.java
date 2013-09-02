package my.service;

import java.util.Random;

public class RandomString {
	
	//生成随机字符串
	public String randomStr(){
		
		//候选字符串，采用26个字母的大小写
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
