package com.src.android_chess;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
	private Button ai, undo, resign, draw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO reverting to sideways view calls onCreate and restarts
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
	
	/**
	 * Renders the entire board to the screen.
	 */
	private void renderBoard() {
		Chessboard board = game.getGrid();
		for(int rank = 7; rank >= 0; rank--) {
			for(int file = 0; file < 8; file++) {
				Square s = squares[rank][file];
				Location loc = new Location(rank, file);
				if(board.isOccupied(loc)) {
					Piece p = board.get(loc);
					if(p.getAlgebraicName().equals("")) {
						s.setImageResource(p.isWhite() ? R.drawable.whitepawn : R.drawable.blackpawn);
					}
					else if(p.getAlgebraicName().equalsIgnoreCase("K")) {
						s.setImageResource(p.isWhite() ? R.drawable.whiteking : R.drawable.blackking);
					}
					else if(p.getAlgebraicName().equalsIgnoreCase("Q")) {
						s.setImageResource(p.isWhite() ? R.drawable.whitequeen : R.drawable.blackqueen);
					}
					else if(p.getAlgebraicName().equalsIgnoreCase("R")) {
						s.setImageResource(p.isWhite() ? R.drawable.whiterook : R.drawable.blackrook);
					}
					else if(p.getAlgebraicName().equalsIgnoreCase("N")) {
						s.setImageResource(p.isWhite() ? R.drawable.whiteknight : R.drawable.blackknight);
					}
					else if(p.getAlgebraicName().equalsIgnoreCase("B")) {
						s.setImageResource(p.isWhite() ? R.drawable.whitebishop : R.drawable.blackbishop);
					}
					else
						System.out.println("Found an unknown piece");
				}
				else
					s.setImageResource(R.drawable.transparent);
			}
		}
	}
	
	/**
	 * Generates the board and adds click listeners to each view.
	 */
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
	     ai = new Button(this);
	     undo = new Button(this);
	     resign = new Button(this);
	     draw = new Button(this);
	     linearLayout.addView(ai);
	     linearLayout.addView(undo);
	     linearLayout.addView(resign);
	     linearLayout.addView(draw);
	     ai.setText("AI");
	     ai.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ai();
			}
		});
	     undo.setText("Undo");
	     undo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				undo();
			}
		});
	     resign.setText("Resign");
	     resign.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resign();
			}
		});
	     draw.setText("Draw");
	     draw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				draw();
			}
		});
	     
	     
	     
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
						if (selected != null){
							if (squares[x][y] == selected){
								squares[x][y].setBackgroundColor(squares[x][y].isWhite() ? Color.WHITE : Color.GRAY);
								selected = null;
								return;
							}
							else {
								Location src = selectedLocation;
								Location dest = new Location(x, y);
								try {
									boolean inCheck = game.move(src, dest);
									if(inCheck)
										Toast.makeText(getBaseContext(), "Check!", Toast.LENGTH_LONG).show();
//									squares[dest.getRank()][dest.getFile()].removePiece();
//									squares[dest.getRank()][dest.getFile()].addPiece(squares[x][y].getPiece());
//									squares[src.getRank()][src.getFile()].removePiece();
//									switchImage(squares[dest.getRank()][dest.getFile()]);
//									switchImage(squares[src.getRank()][src.getFile()]);
									renderBoard();
								}
								catch(IllegalMoveException e) {
									StringBuilder sb = new StringBuilder();
									sb.append(e.getPiece() + " cannot move to " + e.getLocation() + ".");
									sb.append('\n');
									
									ArrayList<Location> locs = e.getPiece().getMoves();
									if(locs.isEmpty())
										sb.append("This piece has no available moves.");
									else {
										sb.append(e.getPiece() + " can move to: ");
										for(Location loc : e.getPiece().getMoves()) {
											sb.append(loc);
											sb.append(' ');
										}
									}
									Toast.makeText(getBaseContext(), sb.toString(), Toast.LENGTH_LONG).show();
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
								return;
							}
							
							Piece p = game.getGrid().get(loc);
							if(p.isWhite() != game.isWhitesTurn()) {
								Toast.makeText(getBaseContext(), "You can only move your own pieces.\nIt's " +
										(game.isWhitesTurn() ? "white's" : "black's") + " turn.", Toast.LENGTH_SHORT).show();
								return;
							}
							
							//select this piece
							squares[x][y].toggleSelected();
//							Globals.getInstance().toggleSelected(squares[x][y]);
							selected = squares[x][y];
							selectedLocation = new Location(x, y);
							squares[x][y].setBackgroundColor(Color.RED);
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
        		 
        		 im.setMinimumHeight(displayHeight/8);
        		 im.setMinimumWidth(20);
        		 im.setMaxHeight(displayHeight / 8);
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
	
	@Deprecated
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
	/*
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
			Square[][] temp = new Square[8][8];
			
			for(int file = 0; file < 8; file++) {
				Location loc = new Location(rank, file);
				temp[rank][file] = squares[rank][file];
				if(game.getGrid().isOccupied(loc)) {
					Piece p = game.getGrid().get(loc);
					String type = p.getAlgebraicName();
					if(type.equals("")) {
						temp[rank][file].setImageResource(p.isWhite() ? R.drawable.whitepawn : R.drawable.blackpawn);
					}
					else if(type.equals("N")) {
						temp[rank][file].setImageResource(p.isWhite() ? R.drawable.whiteknight : R.drawable.blackknight);
					}
					else if(type.equals("B")) {
						temp[rank][file].setImageResource(p.isWhite() ? R.drawable.whitebishop : R.drawable.blackbishop);
					}
					else if(type.equals("R")) {
						temp[rank][file].setImageResource(p.isWhite() ? R.drawable.whiterook : R.drawable.blackrook);
					}
					else if(type.equals("Q")) {
						temp[rank][file].setImageResource(p.isWhite() ? R.drawable.whitequeen : R.drawable.blackqueen);
					}
					else if(type.equals("K")) {
						temp[rank][file].setImageResource(p.isWhite() ? R.drawable.whiteking : R.drawable.blackking);
					}
				}
				else {
					temp[rank][file].setImageResource(R.drawable.transparent);
				}
				
				temp[rank][file].setAdjustViewBounds(true);
				temp[rank][file].setMinimumHeight(displayHeight/10);
       		 	temp[rank][file].setMinimumWidth(20);
       		 	temp[rank][file].setMaxHeight(displayHeight / 10);
       		 	temp[rank][file].setMaxWidth(20);
       		 	if (squares[rank][file].isWhite()){
       		 		temp[rank][file].setBackgroundColor(Color.WHITE);
       		 	}
       		 	else {
       		 		temp[rank][file].setBackgroundColor(Color.GRAY);
       		 	}
           	 
                temp[rank][file].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                
//                newTR.removeAllViews();
//                newTR.invalidate();
//                newTR.refreshDrawableState();
                View parent = ((View) temp[rank][file].getParent());
                
                newTR.removeView(temp[rank][file]);
                newTR.addView(temp[rank][file]);
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

	
		// TODO new view to report result
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
		renderBoard();
	}
	
	public void playGame(View view){
		System.out.println("Play game!");
	}

	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing Activity")
	        .setMessage("Are you sure you want to quit this game? The game will not be saved.")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
	}
	
}
