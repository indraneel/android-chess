package com.src.android_chess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Start extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	/**
	 * Takes user to Chess game activity.
	 * @param view
	 */
	public void playGame(View view){
		System.out.println("Play game!");
		Intent intent = new Intent(this, PlayChess.class);
		startActivity(intent);
	}
	
	/**
	 * Controls the turning on or off of recording games
	 * and displays a Toast pop-up on touch.
	 * @param view
	 */
	public void setRecord(View view){
		Globals.getInstance().toggleSavingGames();
		Context context = getApplicationContext();
		CharSequence text = Globals.getInstance().isSavingGames() ? "Game will be recorded!" : "Recording off!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
