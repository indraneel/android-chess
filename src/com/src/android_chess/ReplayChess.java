package com.src.android_chess;

import android.app.Activity;
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
import com.src.game.Playback;
import com.src.grid.Chessboard;
import com.src.grid.Location;
import com.src.pieces.Piece;

public class ReplayChess extends Activity {

	private Square[][] squares = new Square[8][8];
	private Game game;
	private LinearLayout linearLayout;
	private TableLayout table;
	private TableRow tr;
	private Display mDisplay;
	private int displayHeight, displayWidth;
	private Button next, previous;
	private Playback playback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDisplay = this.getWindowManager().getDefaultDisplay();
		displayHeight = mDisplay.getHeight();
		setContentView(R.layout.playback);
		generateBoard();
		game = new Game(new Chessboard());
		playback = Globals.getPlayback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playbackmenu, menu);
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
            	 im.setImageResource(R.drawable.transparent);
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
         next = new Button(this);
         previous = new Button(this);
         linearLayout.addView(next);
         linearLayout.addView(previous);
         next.setText("Next");
         next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				next();
			}
		});
         
         previous.setText("Previous");
         previous.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				previous();
			}
		});
	     next.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	     previous.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
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
	
	
	/*
	 * switches to next game board if possible
	 * and calls renderboard();
	 */
	public void next(){
		if(playback.hasNext()) {
			playback.next();
			renderBoard();
		}
		else
			Toast.makeText(this, "There are no more moves forward in the playback.",
					Toast.LENGTH_LONG).show();
	}
	
	/*
	 * switches to previous game board if possible
	 * and calls renderboard();
	 */
	public void previous(){
		if (playback.hasPrevious()) {
			playback.previous();
			renderBoard();
		}
		else
			Toast.makeText(this, "There are no more moves backward in the playback.", 
					Toast.LENGTH_LONG).show();
	}
	
	
	/*
	 * what does this do
	 */
	public void playGame(View view){
		System.out.println("Play game!");
	}

}
