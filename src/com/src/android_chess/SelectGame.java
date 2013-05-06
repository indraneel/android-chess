package com.src.android_chess;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class SelectGame extends Activity {

	private GameList gameList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_game);
		
		gameList = Globals.getInstance().getGameList();
		// Load the games list.
		try {
	        gameList.load();
        }
        catch (IOException e) {
	        Toast.makeText(this, "An error occured while loading the saved games list.", Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_game, menu);
		return true;
	}
}