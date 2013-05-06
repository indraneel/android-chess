package com.src.android_chess;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Singleton class that represents a global variable that controls the 
 * save status of games. This is a hack because I don't know how to do
 * actual global variables in Android. But yay for design patterns!
 * 
 * @author Indraneel Purohit
 */

public class Globals {
	private static Globals instance;
	private static GameList gameList;
	private static Square selectedSquare = null;
	private static boolean selected = false;
	private static boolean saveGames = false;
	
	static {
        instance = new Globals();
        gameList = GameList.getInstance();
    }
	
	/**
	 * @return The GameList manager responsible for storing saved chess games.
	 */
	public GameList getGameList() {
		return gameList;
	}
	
	public static Globals getInstance() {
    	if(instance == null)
    		instance = new Globals();
    	
        return Globals.instance;
    }

    private Globals() {
    	super();
    }

    public boolean isSavingGames() {
        return this.saveGames;
    }

    public boolean isSelectedPiece(){
    	return selected;
    }

    public void setSavingGames(boolean saveGames) {
        this.saveGames = saveGames;
    }

    public void toaster(Context context, CharSequence text){
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
    
    public void toggleSavingGames() {
        saveGames = !saveGames;
    }
    
    public void toggleSelected(Square s){
    	setSelectedSquare(s);
    	selected = !selected;
    }
    
    public void toggleSelected(){
    	setSelectedSquare(null);
    	selected = !selected;
    }

	public static Square getSelectedSquare() {
		return selectedSquare;
	}

	public static void setSelectedSquare(Square selectedSquare) {
		Globals.selectedSquare = selectedSquare;
	}

}
