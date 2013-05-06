package com.src.android_chess;

import java.io.IOException;
import java.util.NoSuchElementException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.src.game.Game;
import com.src.grid.Chessboard;
import com.src.grid.Location;
import com.src.move.IllegalMoveException;
import com.src.pieces.Piece;

public class PlayChess extends Activity {

	private Square[][] squares = new Square[8][8];
	private Square selected;
	private Location selectedLocation;
	private Game game;
	private LinearLayout linearLayout;
	private TableLayout table;
	private TableRow tr;
	private Display mDisplay;
	private int displayHeight, displayWidth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDisplay = this.getWindowManager().getDefaultDisplay();
		displayHeight = mDisplay.getHeight();
		setContentView(R.layout.chessboard);
		generateBoard();
		game = new Game(new Chessboard());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gamemenu, menu);
		return true;
	}
	
	private void generateBoard(){
		 linearLayout = (LinearLayout) this.findViewById(R.id.chessboard);
		 linearLayout.removeAllViews();
		 linearLayout.invalidate();
		 linearLayout.refreshDrawableState();
	     table = new TableLayout(this);
	     table.removeAllViews();
	     table.invalidate();
	     table.refreshDrawableState();
	     linearLayout.addView(table);
	     table.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	     table.setStretchAllColumns(true);
	     table.setOrientation(LinearLayout.VERTICAL);
	     setupBoard();
	     
	     //this is 1-8
         for (int r=7; r>=0; r--){
             tr = new TableRow(this);
             tr.removeAllViews();
             tr.invalidate();
             tr.refreshDrawableState();
             table.addView(tr);
             tr.setLayoutParams(new TableLayout.LayoutParams(
                     LayoutParams.WRAP_CONTENT,
                     LayoutParams.WRAP_CONTENT));
             
             //this is a-h
             for (int c=0; c<8; c++){
//            	 TextView im = new TextView(this);
            	 final int x = r, y = c;
            	 
                 Square im = squares[r][c];
                 im.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (selected != null){
							//unselect this piece
							if (squares[x][y] == selected){
								Toast.makeText(getBaseContext(), "Unselecting a square.", Toast.LENGTH_SHORT).show();
								squares[x][y].setBackgroundColor(squares[x][y].isWhite() ? Color.WHITE : Color.GRAY);
								selected = null;
								return;
							}
							else {
								Location src = selectedLocation;
								Location dest = new Location(x, y);
								try {
									game.move(src, dest);
									Toast.makeText(getBaseContext(), "Moving to location " + dest, Toast.LENGTH_SHORT).show();
									squares[dest.getRank()][dest.getFile()].removePiece();
									squares[dest.getRank()][dest.getFile()].addPiece(squares[x][y].getPiece());
									squares[src.getRank()][src.getFile()].removePiece();
									switchImage(squares[dest.getRank()][dest.getFile()]);
									switchImage(squares[src.getRank()][src.getFile()]);
//									renderBoard();
								}
								catch(IllegalMoveException e) {
									Toast.makeText(getBaseContext(), "That move is not allowed.", Toast.LENGTH_SHORT).show();
									return;
								}
								finally {
									selected.setBackgroundColor(selected.isWhite() ? Color.WHITE : Color.GRAY);
									selected = null;
									selectedLocation = null;
								}
							}
						}
						else {
							Location loc = new Location(x, y);
							if(game.getGrid().get(loc) == null) {
								Toast.makeText(getBaseContext(), "Cannot select an empty location.", Toast.LENGTH_SHORT).show();
								return;
							}
							
							Piece p = game.getGrid().get(loc);
							if(p.isWhite() != game.isWhitesTurn()) {
								Toast.makeText(getBaseContext(), "You can only move your own pieces. It's " +
										(game.isWhitesTurn() ? "white's" : "black's") + "turn.", Toast.LENGTH_SHORT).show();
								return;
							}
							
							//select this piece
							squares[x][y].toggleSelected();
//							Globals.getInstance().toggleSelected(squares[x][y]);
							selected = squares[x][y];
							selectedLocation = new Location(x, y);
							squares[x][y].setBackgroundColor(Color.RED);
							Toast.makeText(getBaseContext(), "Selected " + game.getGrid().get(selectedLocation).toString(), Toast.LENGTH_SHORT).show();
						}
					}
				});
                 
//            	 ImageButton im = new ImageButton(this);
                 //black back row
                 if (r==7){
                     //rook
                     if (c==0 || c==7){
                    	 im.setImageResource(R.drawable.blackrook);
                     }
                     //knight
                     else if (c==1 || c==6){
                    	 im.setImageResource(R.drawable.blackknight);
                     }
                     //bishop
                     else if (c==2 || c==5){
                    	 im.setImageResource(R.drawable.blackbishop);
                     }
                     //queen
                     else if (c==3){
                    	 im.setImageResource(R.drawable.blackqueen);
                     }
                     //king
                     else if (c==4){
                    	 im.setImageResource(R.drawable.blackking);
                     }
                     else {
                    	 im.setImageResource(R.drawable.ic_launcher);
                     }
                 }
                 //black pawns
                 else if (r==6){
                	 im.setImageResource(R.drawable.blackpawn);
                 }
                 //white pawns
                 else if (r==1){
                	 im.setImageResource(R.drawable.whitepawn);
                 }
                 //white back row
                 else if (r==0){
                     //rook
                     if (c==0 || c==7){
                    	 im.setImageResource(R.drawable.whiterook);
                     }
                     //knight
                     else if (c==1 || c==6){
                    	 im.setImageResource(R.drawable.whiteknight);
                     }
                     //bishop
                     else if (c==2 || c==5){
                    	 im.setImageResource(R.drawable.whitebishop);
                     }
                     //queen
                     else if (c==3){
                    	 im.setImageResource(R.drawable.whitequeen);
                    	 
                     }
                     //king
                     else if (c==4){
                    	 im.setImageResource(R.drawable.whiteking);
                     }
                     else {
                    	 im.setImageResource(R.drawable.ic_launcher);
                     }                     
                 }
                 else {
                	 im.setImageResource(R.drawable.transparent);
                 }
//                 im.setPadding(0, 0, 0, 0); //padding in each image if needed
        		 im.setAdjustViewBounds(true);
        		 
        		 // Grabbing the width
        		 
        		 im.setMinimumHeight(displayHeight/10);
        		 im.setMinimumWidth(20);
        		 im.setMaxHeight(displayHeight / 10);
        		 im.setMaxWidth(20);
            	 if (squares[r][c].isWhite()){
            		 im.setBackgroundColor(Color.WHITE);
            	 }
            	 else {
            		 im.setBackgroundColor(Color.GRAY);
            	 }
            	 
                 im.setLayoutParams(new TableRow.LayoutParams(
                         TableRow.LayoutParams.WRAP_CONTENT,
                         TableRow.LayoutParams.WRAP_CONTENT));
                 //add here on click event etc for each image...
                 //...
//                 tr.addView(im, im.getWidth(),im.getHeight());
                 tr.addView(im);
             }

         }
         setContentView(linearLayout);
         return;
	}
	
	private void setupBoard(){
		for (int x=7; x>=0; x--){
			for (int y=0; y<8; y++){
				final int r = x;
				final int c = y;
				String location;
				location = "" + ((char)(c+97)) + "" + (r+1);
           		squares[r][c] = new Square(this, (((r+c) % 2) != 0) ? true : false, location);

			}
		}
	}
	
	private void switchImage(Square im){
		Location loc = new Location(im.getLocation());
		Globals.getInstance().toaster(getApplicationContext(), "location:" + loc);
		if(game.getGrid().isOccupied(loc)) {
			Piece p = game.getGrid().get(loc);
			String type = p.getAlgebraicName();
			Globals.getInstance().toaster(getApplicationContext(), "type: " + type);
			if(type.equals("")) {
				im.setImageResource(p.isWhite() ? R.drawable.whitepawn : R.drawable.blackpawn);
			}
			else if(type.equals("N")) {
				im.setImageResource(p.isWhite() ? R.drawable.whiteknight : R.drawable.blackknight);
			}
			else if(type.equals("B")) {
				im.setImageResource(p.isWhite() ? R.drawable.whitebishop : R.drawable.blackbishop);
			}
			else if(type.equals("R")) {
				im.setImageResource(p.isWhite() ? R.drawable.whiterook : R.drawable.blackrook);
			}
			else if(type.equals("Q")) {
				im.setImageResource(p.isWhite() ? R.drawable.whitequeen : R.drawable.blackqueen);
			}
			else if(type.equals("K")) {
				im.setImageResource(p.isWhite() ? R.drawable.whiteking : R.drawable.blackking);
			}
		}
		else {
			im.setImageResource(R.drawable.transparent);
		}
		
		im.setAdjustViewBounds(true);
		 
		im.setMinimumHeight(displayHeight/10);
		im.setMinimumWidth(20);
		im.setMaxHeight(displayHeight / 10);
		im.setMaxWidth(20);
    	if (im.isWhite()){
    		im.setBackgroundColor(Color.WHITE);
    	}
    	else {
    		im.setBackgroundColor(Color.GRAY);
    	}
	}
//	/*
	private void renderBoard() {
//		linearLayout = (LinearLayout) this.findViewById(R.id.chessboard);
		linearLayout.removeView(table);
		linearLayout.removeAllViews();
		linearLayout.invalidate();
		linearLayout.refreshDrawableState();
		TableLayout newTable = new TableLayout(this);
		newTable.removeAllViews();
		linearLayout.addView(newTable);
		newTable.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		newTable.setStretchAllColumns(true);
		newTable.setOrientation(LinearLayout.VERTICAL);
		
		for(int rank = 7; rank >= 0; rank--) {
			tr.removeAllViews();
			TableRow newTR = new TableRow(this);
			newTable.addView(newTR);
			newTR.setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			for(int file = 0; file < 8; file++) {
				Location loc = new Location(rank, file);
				Square im = squares[rank][file];
				if(game.getGrid().isOccupied(loc)) {
					Piece p = game.getGrid().get(loc);
					String type = p.getAlgebraicName();
					if(type.equals("")) {
						im.setImageResource(p.isWhite() ? R.drawable.whitepawn : R.drawable.blackpawn);
					}
					else if(type.equals("N")) {
						im.setImageResource(p.isWhite() ? R.drawable.whiteknight : R.drawable.blackknight);
					}
					else if(type.equals("B")) {
						im.setImageResource(p.isWhite() ? R.drawable.whitebishop : R.drawable.blackbishop);
					}
					else if(type.equals("R")) {
						im.setImageResource(p.isWhite() ? R.drawable.whiterook : R.drawable.blackrook);
					}
					else if(type.equals("Q")) {
						im.setImageResource(p.isWhite() ? R.drawable.whitequeen : R.drawable.blackqueen);
					}
					else if(type.equals("K")) {
						im.setImageResource(p.isWhite() ? R.drawable.whiteking : R.drawable.blackking);
					}
				}
				else {
					im.setImageResource(R.drawable.transparent);
				}
				
				im.setAdjustViewBounds(true);
				im.setMinimumHeight(displayHeight/10);
       		 	im.setMinimumWidth(20);
       		 	im.setMaxHeight(displayHeight / 10);
       		 	im.setMaxWidth(20);
       		 	if (squares[rank][file].isWhite()){
       		 		im.setBackgroundColor(Color.WHITE);
       		 	}
       		 	else {
       		 		im.setBackgroundColor(Color.GRAY);
       		 	}
           	 
                im.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                
//                newTR.removeAllViews();
//                newTR.invalidate();
//                newTR.refreshDrawableState();
                newTR.addView(im);
				//** end here ***
			}
		}
	}
	
//	*/
	
	/**
	 * Offers a draw.
	 */
	public void draw(){
		// TODO Dialog yes/no box
	}
	
	/**
	 * Attempts to make a move on behalf of the current player.
	 */
	public void ai() {
		try {
			game.makeAutomaticMove();
		}
		catch(NoSuchElementException e) {
			Toast.makeText(this, "The computer has no moves for you.", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Resigns from the game.
	 */
	public void resign() {
		boolean white = game.isWhitesTurn();
		Toast.makeText(this, white ? "White resigned!" : "Black resigned!", Toast.LENGTH_SHORT).show();
		if(Globals.getInstance().isSavingGames()) {
			
		}
	}

	/**
	 * Takes back the last move, if possible.
	 */
	public void undo() {
		if(game.canUndo()) {
			game.undo();
			Toast.makeText(this, "Last move taken back. It's " + 
					(game.isWhitesTurn() ? "white's" : "black's") + " turn.", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(this, "You cannot take back any more moves.", Toast.LENGTH_SHORT).show();
	}
	
	public void playGame(View view){
		System.out.println("Play game!");
	}

}
