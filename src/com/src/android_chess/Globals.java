package com.src.android_chess;

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
	private static Square selectedSquare = null;
	private static boolean selected = false;
	static {
        instance = new Globals();
    }
	public static Globals getInstance() {
    	if(instance == null)
    		instance = new Globals();
    	
        return Globals.instance;
    }

    private boolean saveGames = false;
    
    private Globals() {
    	super();
    }

    public boolean getValue() {
        return this.saveGames;
    }

    public boolean isSelectedPiece(){
    	return selected;
    }

    public void setValue(boolean saveGames) {
        this.saveGames = saveGames;
    }

    public void toaster(Context context, CharSequence text){
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
    
    public void toggle() {
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
