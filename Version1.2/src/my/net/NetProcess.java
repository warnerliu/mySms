package my.net;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetProcess{
	private String urlAdd;
	public NetProcess(String urlAdd){
		this.urlAdd = urlAdd;
	}
	public InputStream getNetInputStream(){
		try {
			URL url = new URL(urlAdd);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			//System.out.println("not null");
			return is;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println(e.toString());
		}
		//System.out.println("null");
		return null;
		
	}

}
