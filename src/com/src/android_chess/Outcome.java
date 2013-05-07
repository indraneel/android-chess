package com.src.android_chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Outcome extends Activity {

	private Button save, quit;
	private LinearLayout linearLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outcome);
		linearLayout = (LinearLayout) findViewById(R.layout.outcome);
		linearLayout.addView(save);
		linearLayout.addView(quit);
		save.setText("Save Game");
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		quit.setText("Quit");
		quit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), Start.class);
				startActivity(intent);
			}
		});
		
	}

	/*
	 * This populates the ListView from the GameList 
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_game, menu);
		return true;
	}
	
}