package com.src.android_chess;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chessboard);
		Globals.getInstance().toaster(getApplicationContext(), "setContentView done!");
		generateBoard();
		Globals.getInstance().toaster(getApplicationContext(), "generated Board!");
		game = new Game(new Chessboard());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	private void generateBoard(){
		 LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.chessboard);
		 linearLayout.removeAllViews();
	     TableLayout table = new TableLayout(this);
	     linearLayout.addView(table);
	     Globals.getInstance().toaster(getApplicationContext(), "added table to linearlayout!");
	     table.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	     table.setStretchAllColumns(true);
	     table.setOrientation(LinearLayout.VERTICAL);
	     setupBoard();
	     
	     //this is 1-8
         for (int r=7; r>=0; r--){
             TableRow tr = new TableRow(this);
             table.addView(tr);
             tr.setLayoutParams(new TableLayout.LayoutParams(
                     LayoutParams.WRAP_CONTENT,
                     LayoutParams.WRAP_CONTENT));
             
             //this is a-h
             for (int c=0; c<8; c++){
//            	 TextView im = new TextView(this);
            	 final int x = r, y = c;
            	 
                 ImageView im = squares[r][c];
                 im.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("running onclick!");
						if (selected != null){
							System.out.println("running onclick - first if!");
							//unselect this piece
							if (squares[x][y] == selected){
								Toast.makeText(getBaseContext(), "Unselecting a square.", Toast.LENGTH_SHORT).show();
								squares[x][y].setBackgroundColor(squares[x][y].isWhite() ? Color.WHITE : Color.BLACK);
								selected = null;
								return;
							}
							else {
								Location src = selectedLocation;
								Location dest = new Location(x, y);
								try {
									game.move(src, dest);
									Toast.makeText(getBaseContext(), "Moving to location " + dest, Toast.LENGTH_SHORT).show();
									
								}
								catch(IllegalMoveException e) {
									Toast.makeText(getBaseContext(), "That move is not allowed.", Toast.LENGTH_SHORT).show();
									return;
								}
								finally {
									selected.setBackgroundColor(selected.isWhite() ? Color.WHITE : Color.BLACK);
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
                 im.setImageResource(R.drawable.ic_launcher);
//                 im.setPadding(0, 0, 0, 0); //padding in each image if needed
        		 im.setAdjustViewBounds(true);
        		 
        		 // Grabbing the width
        		 Display mDisplay = this.getWindowManager().getDefaultDisplay();
        		 int height = mDisplay.getHeight();
        		 
        		 im.setMaxHeight(height / 10);
        		 im.setMaxWidth(20);
            	 if (squares[r][c].isWhite()){
            		 im.setBackgroundColor(Color.WHITE);
            	 }
            	 else {
            		 im.setBackgroundColor(Color.BLACK);
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
           		squares[r][c] = new Square(this, (((r+c) % 2) == 0) ? true : false, location);

			}
		}
	}
	
	private void addPieces(){
		
	}
	
	public void playGame(View view){
		System.out.println("Play game!");
	}

}
