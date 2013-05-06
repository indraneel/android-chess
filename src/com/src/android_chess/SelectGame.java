package com.src.android_chess;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SelectGame extends Activity {

	private GameList gameList;
	private LinearLayout linearLayout;
	private ListView listView;
	private Button listButton;
	private boolean sortByTitle = true;
	private SimpleAdapter mSchedule;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_game);
		
		gameList = Globals.getInstance().getGameList();
		// Load the games list.
//		try {
//	        gameList.load();
//        }
//        catch (IOException e) {
//	        Toast.makeText(this, "An error occured while loading the saved games list.", Toast.LENGTH_SHORT).show();
//        }
		linearLayout = (LinearLayout) findViewById(R.id.gamelistlayout);
		listView = new ListView(getApplicationContext());
		linearLayout.addView(listView);
		
		populateList();
		
	}

	/*
	 * This populates the ListView from the GameList 
	 */
	private void populateList() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_game, menu);
		return true;
	}
	
	public void listButtonClick(View view){
		sortByTitle = !sortByTitle;
		listButton = (Button) findViewById(R.id.gamelistbtn);
		listButton.setText(sortByTitle ? "Sort by Title" : "Sort by Date");
		//sort by date
		if (listButton.getText().equals("Sort by Date")) {
			sortByDate();
		}
		//sort by text
		else {
			sortByTitle();
		}
	}
	
	/*
	 * Sorts list by date
	 */
	private void sortByDate(){
		
	}
	
	/*
	 * Sorts list by title
	 */
	private void sortByTitle(){
		
	}
}