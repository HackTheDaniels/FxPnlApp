package com.anz.org.fxtradepnlapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import com.anz.org.fxtradepnlapp.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
	public MyService() {
	}

	//used for getting the handler from other class for sending messages
	public static Handler mMyServiceHandler 			= null;
	//used for keep track on Android running status
	public static Boolean 		mIsServiceRunning 			= false;

	MathProcessor processor = null;

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
		processor = new MathProcessor(this);
	}

	@Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		// Perform your long running operations here.
		if(!mIsServiceRunning) {
			Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
			ReadRawFile();
			final Thread t = new Thread(new RepeatingThread(processor));
			t.start();
			mIsServiceRunning = true;
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		mIsServiceRunning = false;

	}

	private void ReadRawFile() {
		BufferedReader br = null;
		try {

			String sCurrentLine;
			List<String> rawData=new ArrayList<String>();
			InputStream is = this.getResources().openRawResource(R.raw.testdata);
			br = new BufferedReader(new InputStreamReader(is));
			if (is != null) {
				while ((sCurrentLine = br.readLine()) != null) {
					rawData.add(sCurrentLine);
				}
			}
			is.close();
			BufferedData.SetData(rawData);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}