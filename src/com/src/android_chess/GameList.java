package com.src.android_chess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.widget.Toast;

import com.src.game.Playback;
import com.src.move.MoveSequence;

/**
 * Implements a list of saved games that the user can select from.
 * @author Kyle
 */
public class GameList {
	/**
	 * The file where saved games are stored.
	 */
	public static final String GAMES_LIST = "games.dat";
	private static GameList instance;
	
	private ArrayList<Playback> games;
	private Comparator<Playback> comparator;
	private Context ctx;
	private boolean sortByTitle;
	
	/**
	 * Create a new empty game list.
	 */
	private GameList() {
		super();
		games = new ArrayList<Playback>();
		sortByTitle = true;
		setComparator();
	}
	
	/**
	 * Creates a new recorded game and adds it to the list.
	 * @param m The sequence of moves to save.
	 * @param title The title of the game.
	 * @return A reference to the newly-created object.
	 */
	public Playback add(MoveSequence m, String title) {
		if(m == null || title == null)
			throw new IllegalArgumentException("Parameters cannot be null.");
		
		Playback game = new Playback(m, title);
		
		if(games.isEmpty()) {
			games.add(game);
			return game;
		}
		
		int lo = 0, hi = games.size() - 1;
		int result = 0, mid = 0;
		while (lo <= hi) {
			mid = (lo + hi) / 2;
			result = comparator.compare(game, games.get(mid));
			if (result == 0) {
				break;
			}
			if (result < 0) {
				hi = mid - 1;
			} else {
				lo = mid + 1;
			}
		}
		int pos = result <= 0 ? mid : mid+1;
		games.add(pos,game);
		
		try {
			save();
			return game;
		}
		catch(IOException e) {
			return null;
		}
	}
	
	/**
	 * @return All games in the list.
	 */
	public ArrayList<Playback> getGames() {
		return games;
	}
	
	/**
	 * Loads in all saved games from the file.
	 * @throws IOException If an unknown I/O exception occurs.
	 */
	public void load() throws IOException {
		try {
			FileInputStream fs = ctx.openFileInput(GAMES_LIST);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			
			String seq;
			while((seq = br.readLine()) != null) {
				Playback r = Playback.fromString(seq);
				games.add(r);
			}
			fs.close();
			br.close();
			Toast.makeText(ctx, "Loaded from file!", Toast.LENGTH_SHORT).show();
		}
		catch(FileNotFoundException e) {
			// Create a new empty file for next time
			FileOutputStream fs = ctx.openFileOutput(GAMES_LIST, Context.MODE_PRIVATE);
			PrintWriter pw = new PrintWriter(fs);
			pw.print("");
			pw.close();
			fs.close();
		}
	}
	
	/**
	 * Saves the game list to a file on the device.
	 * @throws IOException If an unknown I/O exception occurs.
	 */
	public void save() throws IOException {
		FileOutputStream fs = ctx.openFileOutput(GAMES_LIST, Context.MODE_PRIVATE);
		PrintWriter pw = new PrintWriter(fs);
		for(Playback g : games) {
			pw.println(g.toString());
		}
		pw.close();
		fs.close();
	}
	
	/**
	 * @param ctx The application context for this list.
	 */
	public void setContext(Context ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * Toggles between sorting games by title and by date. This method 
	 * automatically resorts the list. 
	 */
	public void toggleSorting() {
		sortByTitle = !sortByTitle;
		setComparator();
		Collections.sort(games, comparator);
	}
	
	private void setComparator() {
		comparator = sortByTitle ? Playback.getTitleComparator() : 
			Playback.getDateComparator();
	}
	
	/**
	 * @return The singleton instance of this class.
	 */
	public static GameList getInstance() {
		if(instance == null)
			instance = new GameList();
		
		return instance;
	}
	
	/**
	 * @param ctx The application context.
	 * @return The singleton instance of this class.
	 * @throws IOException If an error occurs while loading.
	 */
	public static GameList getInstance(Context ctx) throws IOException {
		if(instance == null) {
			instance = new GameList();
			instance.ctx = ctx;
			instance.load();
		}
		
		return instance;
	}
}