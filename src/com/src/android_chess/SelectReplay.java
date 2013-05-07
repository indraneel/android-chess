package com.src.android_chess;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.src.game.Playback;

public class SelectReplay extends Activity {
	private GameList gameList;
	private LinearLayout linearLayout;
	private ListView listView;
	private Button listButton;
	private boolean sortByTitle = true;
	private SimpleAdapter mSchedule;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("Creating selection list");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_game);
		
		linearLayout = (LinearLayout) findViewById(R.id.gamelistlayout);
		listView = new ListView(getApplicationContext());
		linearLayout.addView(listView);
		
		populateList();
		
	}

	/**
	 * This populates the ListView from the GameList 
	 */
	private void populateList() {
		System.out.println("Populating");
		ArrayList<Playback> games = Globals.getSavedGames();
		for(int i = 0; i < games.size(); i++) {
			Playback p = games.get(i);
			Button b = new Button(this);
			b.setText(p.getTitle() + " - " + p.getDate());
			b.setTag(p);
			b.setOnClickListener(new OnClickListener() {
				@Override
                public void onClick(View v) {
	                Globals.setPlayback((Playback) v.getTag());
	                Intent intent = new Intent(getBaseContext(), ReplayChess.class);
	                startActivity(intent);
                }
			});
			listView.addView(b);
		}
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
		listView.removeAllViewsInLayout();
		listView.invalidate();
		listView.refreshDrawableState();
		// TODO
		populateList();
	}
}
