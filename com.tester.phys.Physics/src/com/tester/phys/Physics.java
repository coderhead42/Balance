package com.tester.phys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Physics extends Activity {

	// this is the main activity of the app i wrote. the methods here need to be
	// placed inside whatever activity you use to call the game.
	// the only view in this, as i wrote it, is the simulator.

	// this declares a view object of class PhysicsWorld in this activity. Refer
	// other file.
	int[] flags = new int[] { R.drawable.shape1, R.drawable.shape2,
			R.drawable.shape3, R.drawable.shape4, R.drawable.shape5,
			R.drawable.shape6, R.drawable.shape7 };

	PhysicsView mView;

	public int B = 1;

	// creates a handler to handle the timing of update calls to mWorld.
	private Handler mHandler;
	public int W, H;

	@TargetApi(4)
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// *************************
		// GENERAL
		// *************************

		// uncomment the below for fullscreen:
		//
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// removes title-bar.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// below two lines get absolute pixel metrics of the screen.

		// *******************************
		// DIMENSIONS OF SIMULATION VIEW
		// *******************************

		// creates view object, with dimensions sized to the screen.
		// the dimensions being passed are for the sake of the simulator 'world'
		// as well as the canvas.
		// modify the constructor of PhysicsWorld, if you want to separate these
		// two uses.
		//
		// it's mostly unnecessary to do that, though. just change the second
		// and third arguments to reflect the desired size of the view.
		// eg.: creates a view object of 1/2 the height of the screen:
		//
		W = dm.widthPixels;
		H = dm.heightPixels;

		Resources res = getResources();
		DisplayMetrics metrics = res.getDisplayMetrics();
		float px = 65 * (metrics.densityDpi / 160f);
		W = W - (int) px;

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("width", W);
		editor.putInt("height", H);
		editor.commit();

		// mView = new PhysicsView(this);
		//
		// mWorld = new PhysicsWorld(this, dm.widthPixels, dm.heightPixels);

		// mView.setOnTouchListener(new MyTouchListener());

		// ******************************
		// SETCONTENTVIEW
		// ******************************

		// this is the line that sets mWorld as the view tied to this activity.
		// you probably have your own version of this in your main activity;
		// replace the part of your layout/view-group on which you're "dropping"
		// with mWorld, then delete this. should work fine.
		// this.setContentView(findViewById(R.id.main));
		this.setContentView(R.layout.main);

		List<HashMap<String, Integer>> alist = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < 7; i++) {
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			hm.put("flag", flags[i]);
			alist.add(hm);
		}

		String[] from = { "flag" };
		int[] to = { R.id.flag };

		SimpleAdapter adapter = new SimpleAdapter(this, alist,
				R.layout.listview_layout, from, to);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				B = position;

			}

		});

		Button b1, b2, b3;
		b1 = (Button) this.findViewById(R.id.restart);
		b2 = (Button) this.findViewById(R.id.pause);
		b3 = (Button) this.findViewById(R.id.resume);

		b1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent Game = new Intent(Physics.this, MainActivity.class);
				startActivity(Game);
				finish();
			}
		});

		b2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPause();
			}
		});

		b3.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				onResume();
			}
		});

		mView = (PhysicsView) this.findViewById(R.id.physics_world);
		mView.setOnTouchListener(new MyTouchListener());
		// this.addContentView(findViewById(R.id.test_button),findViewById(R.id.test_button).getLayoutParams());

		// ******************************
		// GAME LOOP (ADD BLOCK)
		// ******************************

		// This is the addBlock() loop. change to a 'while' loop:
		// eg.:
		// while (Loss()==false):
		//
		// and we have our game loop!

		// for (int i = 0; i < 4; i++) {

		// This is the function that needs to be changed to take user input.
		// arguments to addBlock need to be x and y coordinates (relative to
		// the view, with origin at base of fulcrum),
		// with origin at bottom left of window.
		// given x and y arguments at present use the loop to create an
		// offset of position to the left.
		// the tower always falls.

		// mView.mWorld
		// .addBlock((int) (dm.widthPixels / 2),
		// (int) (40.0 + (i * mView.mWorld.width)
		// + mView.mWorld.width / 2 + 1), 1);
		//
		// }

		// ******************************
		// UPDATE SIMULATION - START
		// ******************************

		// Start Regular Update
		// (simulation update start.)

		mHandler = new Handler();

		mHandler.post(update);

	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mView.drawBlock(motionEvent.getRawX(),
						H - motionEvent.getRawY(), B, 0);
				mView.mWorld.setBlockDraw(true);
					break;
			case MotionEvent.ACTION_MOVE:
				mView.drawBlock(motionEvent.getRawX(),
						H - motionEvent.getRawY(), B, 0);
				break;
			case MotionEvent.ACTION_UP:
				mView.mWorld.addBlock(motionEvent.getRawX(),
						H - motionEvent.getRawY(), B);
				mView.mWorld.setBlockDraw(false);
				break;
			}
			return true;
		}
	}

	@Override
	protected void onPause() {

		super.onPause();

		// this stops simulation update on pausing.
		// resume starts it again.
		//
		mHandler.removeCallbacks(update);

	}

	// ******************************
	// UPDATE FUNCTION
	// ******************************

	// this runnable calls simulation update, then itself after a delay.
	// look for its start/stop in onCreate, onPause, onResume.
	private Runnable update = new Runnable() {

		public void run() {

			mView.update();

			mHandler.postDelayed(update, (long) (10 / mView.mWorld.timeStep));
			// if(!mView.mWorld.isTower()){
			// onPause();
			// }
		}

	};

	@Override
	protected void onResume()

	{

		super.onResume();
		// continues simulation from where it was left.
		mHandler.post(update);

	}

	@Override
	protected void onStop()

	{

		super.onStop();

	}

}
