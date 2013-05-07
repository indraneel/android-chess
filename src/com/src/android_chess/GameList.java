package com.src.android_chess;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//import android.content.Context;
//import android.widget.Toast;
//
//import com.src.game.Playback;
//
///**
// * Implements a list of saved games that the user can select from.
// * @author Kyle
// */
//public class GameList {
//	/**
//	 * The file where saved games are stored.
//	 */
//	public static final String GAMES_LIST = "games.txt";
//	private static GameList instance;
//	
//	private Comparator<Playback> comparator;
//	private Context ctx;
//	private boolean sortByTitle;
//	
//	/**
//	 * Create a new empty game list.
//	 */
//	private GameList() {
//		super();
//		sortByTitle = true;
//		setComparator();
//	}
//	
//	/**
//	 * Add a new playback.
//	 */
//	public void add(Playback game) {
//		Globals.getGameList().add(game);
////		if(game == null)
////			throw new IllegalArgumentException("Parameters cannot be null.");
////		
////		if(games.isEmpty()) {
////			games.add(game);
////		}
////		else {
////			int lo = 0, hi = games.size() - 1;
////			int result = 0, mid = 0;
////			while (lo <= hi) {
////				mid = (lo + hi) / 2;
////				result = comparator.compare(game, games.get(mid));
////				if (result == 0) {
////					break;
////				}
////				if (result < 0) {
////					hi = mid - 1;
////				} else {
////					lo = mid + 1;
////				}
////			}
////			int pos = result <= 0 ? mid : mid+1;
////			games.add(pos,game);
////		}
////		
////		try {
////			save();
////			System.out.println("Written");
////		}
////		catch(IOException e) {
////			System.out.println("An error occurred while saving.");
////			e.printStackTrace(System.out);
////		}
//	}
//	
//	/**
//	 * Loads in all saved games from the file.
//	 * @throws IOException If an unknown I/O exception occurs.
//	 */
//	public void load() throws IOException {
//		try {
//			FileInputStream fs = ctx.openFileInput(GAMES_LIST);
//			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
////			File f = new File(GAMES_LIST);
////			BufferedReader br = new BufferedReader(new FileReader(f));
//			
//			String seq;
//			while((seq = br.readLine()) != null) {
//				Playback r = Playback.fromString(seq);
//				games.add(r);
//			}
//			br.close();
//			Toast.makeText(ctx, "Loaded from file!", Toast.LENGTH_SHORT).show();
//		}
//		catch(FileNotFoundException e) {
////			 Create a new empty file for next time
//			FileOutputStream fs = ctx.openFileOutput(GAMES_LIST, Context.MODE_PRIVATE);
//			PrintWriter pw = new PrintWriter(fs);
//			
////			File f = new File(GAMES_LIST);
////			f.createNewFile();
////			System.out.println("New file created");
//			
//			pw.print("");
//			pw.close();
//		}
//	}
//	
//	/**
//	 * Saves the game list to a file on the device.
//	 * @throws IOException If an unknown I/O exception occurs.
//	 */
//	public void save() throws IOException {
//		FileOutputStream fs = ctx.openFileOutput(GAMES_LIST, Context.MODE_PRIVATE);
//		PrintWriter pw = new PrintWriter(fs);
////		File f = new File(GAMES_LIST);
////		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//		
//		for(Playback g : games) {
//			pw.println(g.toString());
////			bw.write(g.toString());
////			bw.write('\n');
//		}
////		bw.close();
//		System.out.println("Saved");
//		pw.close();
//		fs.close();
//	}
//	
//	/**
//	 * @param ctx The application context for this list.
//	 */
//	public void setContext(Context ctx) {
//		this.ctx = ctx;
//	}
//	
//	/**
//	 * Toggles between sorting games by title and by date. This method 
//	 * automatically resorts the list. 
//	 */
//	public void toggleSorting() {
//		sortByTitle = !sortByTitle;
//		setComparator();
//		Collections.sort(games, comparator);
//	}
//	
//	private void setComparator() {
//		comparator = sortByTitle ? Playback.getTitleComparator() : 
//			Playback.getDateComparator();
//	}
//	
//	/**
//	 * @return The singleton instance of this class.
//	 */
//	public static GameList getInstance() {
//		if(instance == null)
//			instance = new GameList();
//		
//		return instance;
//	}
//	
//	/**
//	 * @param ctx The application context.
//	 * @return The singleton instance of this class.
//	 * @throws IOException If an error occurs while loading.
//	 */
//	public static GameList getInstance(Context ctx) throws IOException {
//		if(instance == null) {
//			instance = new GameList();
//			instance.ctx = ctx;
//			instance.load();
//		}
//		
//		return instance;
//	}
//}
public class GameList {
	
}