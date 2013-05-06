package com.src.android_chess;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PlayChess extends Activity {

	private Square[][] squares = new Square[8][8];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chessboard);
		Globals.getInstance().toaster(getApplicationContext(), "setContentView done!");
		generateBoard();
		Globals.getInstance().toaster(getApplicationContext(), "generated Board!");
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
	     //this is 1-8
         for (int r=7; r>=0; r--){
             TableRow tr = new TableRow(this);
             table.addView(tr);
             tr.setLayoutParams(new TableLayout.LayoutParams(
                     LayoutParams.WRAP_CONTENT,
                     LayoutParams.WRAP_CONTENT));
             setupBoard();
             
             //this is a-h
             for (int c=0; c<8; c++){
//            	 TextView im = new TextView(this);
                 ImageView im = new ImageView (this);
//            	 ImageButton im = new ImageButton(this);
                 im.setImageResource(R.drawable.ic_launcher);
//                 im.setPadding(0, 0, 0, 0); //padding in each image if needed
        		 im.setAdjustViewBounds(true);
        		 im.setMaxHeight(40);
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
           		squares[r][c].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (Globals.getInstance().isSelectedPiece()){
							//unselect this piece
							if (squares[r][c].isSelected()){
								squares[r][c].toggleSelected();
								Globals.getInstance().toggleSelected();
								squares[r][c].setBackgroundColor(squares[r][c].isWhite() ? Color.WHITE : Color.BLACK);
							}

							//DO I NEED THIS? select this piece instead

							//move here
							if (!squares[r][c].isSelected()){
								Globals.getSelectedSquare().setBackgroundColor(squares[r][c].isWhite() ? Color.WHITE : Color.BLACK);
								Globals.getSelectedSquare().toggleSelected();
								Globals.getInstance().toggleSelected();
								squares[r][c].toggleSelected();
								Globals.getInstance().toggleSelected(squares[r][c]);
								squares[r][c].setBackgroundColor(Color.RED);
								squares[r][c].setBackgroundColor(squares[r][c].isWhite() ? Color.WHITE : Color.BLACK);
							}
						}
						else {	
							//select this piece
							squares[r][c].toggleSelected();
							Globals.getInstance().toggleSelected(squares[r][c]);
							squares[r][c].setBackgroundColor(Color.RED);
						}
						return;
					}
				});
			}
		}
	}
	
	private void addPieces(){
		
	}
	
	public void playGame(View view){
		System.out.println("Play game!");
	}

}
