package my.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import my.pro.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestNetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		setTitle("Õ¯¬Á≤‚ ‘");

		// http test
		Button netTestBtn = (Button) findViewById(R.id.networktest);
		netTestBtn.setOnClickListener(new NetWorkTestListener());

		// socket test
		Button socketBtn = (Button) findViewById(R.id.sendsocket);
		socketBtn.setOnClickListener(new SocketListener());

	}

	// http listener
	class NetWorkTestListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView showNet = (TextView) findViewById(R.id.showcontent);
			String urlAdd = "http://10.0.2.2:8080/database/information.txt";
			NetProcess myTest = new NetProcess(urlAdd);
			showNet.setText("show something");
			try {

				InputStream is = myTest.getNetInputStream();
				// showNet.setText(is.read());
				// Œ Ã‚”Ôæ‰
				InputStreamReader isr = new InputStreamReader(is, "GBK");
				BufferedReader br = new BufferedReader(isr);
				StringBuilder str = new StringBuilder();
				String s;
				while ((s = br.readLine()) != null) {
					str.append(s);
				}
				showNet.setText(str);
				// System.out.println(str.toString());
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// System.out.println(e.toString());
			}
		}
	}

	// socket listener
	class SocketListener implements OnClickListener {

		private static final String host = "10.0.2.2";
		private static final int port = 8080;
		private Socket socket = null;
		private BufferedReader in = null;
		private PrintWriter out = null;
		private String msSend = "socket test";
		
		TextView showNet = (TextView) findViewById(R.id.showcontent);
		
		@Override
		public void onClick(View v) {				
			// TODO Auto-generated method stub
			try {
				socket = new Socket(host, port);
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				if (socket.isConnected()) {
					if (!socket.isOutputShutdown()) {
						out.println(msSend);
						showNet.setText(msSend);
					} else
						System.out.println("the channel is closed!");
				} else
					System.out.println("cannot connect to the host!");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
