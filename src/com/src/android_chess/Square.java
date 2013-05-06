package com.src.android_chess;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.src.grid.Location;
import com.src.pieces.Piece;

public class Square extends ImageView {
	private boolean isWhite, isSelected;
	private String loc;
	private Piece piece;
	private Location location;
	
	public Square(Context context, Boolean isWhite, String loc) {
		super(context);
		this.isWhite = isWhite;
		this.location = new Location(loc);
		this.setClickable(true);
	}
	
	public String getLocation(){
		return location.toString();
	}
	
	public boolean isWhite(){
		return this.isWhite;
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	public Piece getPiece(){
		return this.piece;
	}
	
	public void addPiece(Piece p){ 
		this.piece = p;
	}
	
	public boolean removePiece(){
		if (this.piece == null){
			return false;
		}
		this.piece = null;
		return true;
	}
	
	public void toggleSelected(){
		isSelected = !isSelected;
//		Globals.getInstance().toggleSelected();
	}
	

}
