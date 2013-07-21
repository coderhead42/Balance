package com.tester.phys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

@TargetApi(5)
public class PhysicsMult_BT extends Physics {

	private boolean desPlayerMP = false;
	public boolean isRunning = false;

	private int mTeamScore;

	private OutputStream mOutputStream;
	private PrintWriter mPrintWriterOUT;
	private BufferedReader mBufferedReader;

	private static BluetoothSocket mSocket;
	
	private int ID;

	private void initBT() {

		try {

			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			mOutputStream = mSocket.getOutputStream();
			mPrintWriterOUT = new PrintWriter(mOutputStream);
		} catch (UnknownHostException e) {

			Toast.makeText(this, "UNKNOWN SERVER ADDRESS", 100).show();
		} catch (IOException e) {

			Toast.makeText(this, "ERROR CREATING SOCKET", 100).show();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBT();
		mView.setOnTouchListener(new MyTouchListener1());
		startReceiverThread();
	}

	public String receiveMessage() {

		String receivedMessage;
//		Toast.makeText(this, "RECEIVE MESSAGE CALLED", 100).show();
		try {
			receivedMessage = new String(mBufferedReader.readLine() + "\n");
			receivedMessage.trim();
			return receivedMessage;
		} catch (IOException e) {
			// Debug.d("error reading stream");
			isRunning = false;
			Toast.makeText(this, "NOT ABLE TO RECEIVE MESSAGE", 100).show();
			// endGame(0);
		}

		return null;

	}

	public void sendMessage(String str) {

		mPrintWriterOUT.println(str);
		mPrintWriterOUT.flush();
	}

	
	private final class MyTouchListener1 implements OnTouchListener {

		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mView.drawBlock(motionEvent.getRawX(),
						H - motionEvent.getRawY(), B, ID);
				sendMessage("nghost " + Float.toString(motionEvent.getRawX()/W)
						+ " " + Float.toString((H - motionEvent.getRawY())/H));
				mView.mWorld.setBlockDraw(true);
				break;
			case MotionEvent.ACTION_MOVE:
				mView.drawBlock(motionEvent.getRawX(),
						H - motionEvent.getRawY(), B, ID);
				sendMessage("ghost " + Float.toString(motionEvent.getRawX()/W)
						+ " " + Float.toString((H - motionEvent.getRawY())/H));
				break;
			case MotionEvent.ACTION_UP:
				mView.mWorld.addBlock(motionEvent.getRawX(),
						H - motionEvent.getRawY(), B);
				mView.mWorld.setBlockDraw(false);
				sendMessage("add " + Float.toString(motionEvent.getRawX()/W)
						+ " " + Float.toString((H - motionEvent.getRawY())/H));
				break;
			}

			return true;
		}
	}

	private void startReceiverThread() {
		isRunning = true;
	//	Toast.makeText(this, "RECEIVER THREAD RUNNING", 100).show();
		(new Thread() {

			public void run() {

				while (true) {
					if (isRunning) {
						String[] msgArray = new String[3];
						msgArray = receiveMessage().split(" ", 3);

						// Debug.d("incoming: " + msg);

						handleReceivedMessage(msgArray);

					} else
						break;
				}

			}
		}).start();

	}
	private void handleReceivedMessage(String[] msgArray) {
        if (msgArray[0].contains("nghost")) {
        	mView.drawBlock(Float.parseFloat(msgArray[1])*mView.mWorld.World_W, Float.parseFloat(msgArray[2])*mView.mWorld.World_H, B, 0);
        	mView.mWorld.setBlockDraw(true);
        }
        if (msgArray[0].contains("ghost")) {
        	mView.drawBlock(Float.parseFloat(msgArray[1])*mView.mWorld.World_W, Float.parseFloat(msgArray[2])*mView.mWorld.World_H, B, 0);
        }
        if (msgArray[0].contains("add")) {
        	mView.mWorld.addBlock(Float.parseFloat(msgArray[1])*mView.mWorld.World_W, Float.parseFloat(msgArray[2])*mView.mWorld.World_H, B);
        	mView.mWorld.setBlockDraw(false);
			
        }
}

	
	public static void setBluetoothSocket(BluetoothSocket _BTSocket) {
		mSocket = _BTSocket;
	}

}
