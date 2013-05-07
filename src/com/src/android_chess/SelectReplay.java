package com.src.android_chess;

import java.io.IOException;

import com.src.game.Playback;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SelectReplay extends Activity {
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
		try {
	        gameList.load();
        }
        catch (IOException e) {
	        Toast.makeText(this, "An error occured while loading the saved games list.", Toast.LENGTH_SHORT).show();
        }
		catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
		linearLayout = (LinearLayout) findViewById(R.id.gamelistlayout);
		listView = new ListView(getApplicationContext());
		linearLayout.addView(listView);
		
		populateList();
		
	}

	/**
	 * This populates the ListView from the GameList 
	 */
	private void populateList() {
		for(int i = 0; i < gameList.getGames().size(); i++) {
			Playback p = gameList.getGames().get(i);
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
			listView.addView(b, i);
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
		gameList.toggleSorting();
		populateList();
	}
}
