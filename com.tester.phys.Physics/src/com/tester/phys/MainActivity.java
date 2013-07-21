package com.tester.phys;

/*import android.os.Bundle;
 import android.app.Activity;
 import android.view.Menu;

 public class MainActivity extends Activity {

 @Override
 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);
 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
 getMenuInflater().inflate(R.menu.activity_main, menu);
 return true;
 }
 }
 */
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**********************************************************************************
 * 
 * Start Menu activity
 * 
 **********************************************************************************/

public class MainActivity extends Activity implements OnClickListener {
	public static boolean settingsChanged = false;

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// settingsChanged=false;Intent openMP_MapMenu_BT = new Intent(MainActivity.this, PhysicsM.class);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Typeface tf = Typeface.createFromAsset(getAssets(), "ANKLEPAN.TTF");
		Typeface tf1 = Typeface.createFromAsset(getAssets(), "AltamonteNF.ttf");

		TextView txv_single = (TextView) findViewById(R.id.txv_single);
		TextView txv_multi = (TextView) findViewById(R.id.txv_multi);

		TextView txv_about = (TextView) findViewById(R.id.txv_about);
		TextView txv_exit = (TextView) findViewById(R.id.txv_exit);

		txv_single.setTypeface(tf);
		txv_multi.setTypeface(tf);

		txv_about.setTypeface(tf);
		txv_exit.setTypeface(tf);

		txv_single.setOnTouchListener(new MenuTextTouchListener());
		txv_multi.setOnTouchListener(new MenuTextTouchListener());

		txv_about.setOnTouchListener(new MenuTextTouchListener());
		txv_exit.setOnTouchListener(new MenuTextTouchListener());

		txv_single.setOnClickListener(this);
		txv_multi.setOnClickListener(this);

		txv_about.setOnClickListener(this);
		txv_exit.setOnClickListener(this);

		// Toast.makeText(this, "Press Menu to Start Game",
		// Toast.LENGTH_LONG).show();

	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.txv_single:
			Intent Game = new Intent(MainActivity.this, Physics.class);
			startActivity(Game);
			finish();
			break;

		case R.id.txv_multi:
			Intent Game_BT = new Intent(MainActivity.this,
					PhysicsM.class);
			startActivity(Game_BT);
			break;

		case R.id.txv_about:
			makeAboutDialog();
			break;

		case R.id.txv_exit:
			finish();
			break;
		}

	}

	private void makeAboutDialog() {
		// Linkify the message
		final SpannableString strAbout = new SpannableString(
				"This Game has been made by Team Equilibrium: Aman, Tanuj & Sahiti");
		Linkify.addLinks(strAbout, Linkify.ALL);

		AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(this);

		aboutDialogBuilder.setMessage(strAbout);
		aboutDialogBuilder.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});

		AlertDialog aboutDialog = aboutDialogBuilder.create();

		aboutDialog.show();

		// Make the textview clickable. Must be called after show()
		((TextView) aboutDialog.findViewById(android.R.id.message))
				.setMovementMethod(LinkMovementMethod.getInstance());
	}


	public static void settingsChanged() {
		settingsChanged = true;
	}

	// ImageView imageView= (ImageView)findViewById(R.id.imageview);
	// Animation fadeInAnimation = AnimationUtils.loadAnimation(this,
	// R.layout.anim);
	// Now Set your animation
	// imageView.startAnimation(fadeInAnimation );
	private class MenuTextTouchListener implements View.OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {

			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				((TextView) view).setTextColor(0xFF6A5ceD);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				((TextView) view).setTextColor(0xFF6A5ceD);
				break;
			}

			return false;
		}
	}

}
