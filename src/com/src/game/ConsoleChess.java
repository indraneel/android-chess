package com.src.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.src.grid.Chessboard;
import com.src.grid.Location;
import com.src.move.IllegalMoveException;
import com.src.move.InCheckException;
import com.src.move.InvalidLocationException;
import com.src.move.Move;
import com.src.pieces.Piece;


/**
 * Plays the game of chess.
 * @author Kyle
 */
public class ConsoleChess {
	/**
	 * Indicates a result of 1-0.
	 */
	public static final int WHITE_VICTORY = 0;
	
	/**
	 * Indicates a result of 0-1.
	 */
	public static final int BLACK_VICTORY = 1;
	
	/**
	 * Indicates a result of 1/2-1/2.
	 */
	public static final int DRAW = 2;
	
	private static Scanner scanner;
	static {
		scanner = new Scanner(System.in);
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to Chess!");
		Game game = new Game(new Chessboard());
		boolean draw_offered = false;
		boolean auto_invert = true;
		int result = -1;
		
		System.out.println("\n" + game.getGrid() + "\n");
		
		do {
			System.out.print("> ");
			StringTokenizer st = new StringTokenizer(scanner.nextLine(), " \n\t");
			int count = st.countTokens();
			if(count < 1 || count > 4) {
				System.out.println("Not a valid input.");
				continue;
			}
			if(count == 1) {
				String input = st.nextToken();
				if(input.equalsIgnoreCase("help"))
					showHelp();
				else if(input.equalsIgnoreCase("undo")) {
					if(!game.canUndo())
						System.out.println("You can't undo anymore.\n");
					else {
						game.undo();
						printBoard(auto_invert, game);
					}
				}
				else if(input.equalsIgnoreCase("save")) {
					System.out.print("Enter a title for this game: ");
					String in = scanner.nextLine();
					Playback p = game.toPlayback(in);
					if(p == null)
						System.out.println("Game is not saving.");
					else {
						try {
							BufferedWriter br = new BufferedWriter(new FileWriter(in + ".txt"));
							br.write(p.toString());
							br.close();
							System.out.println("Saved to " + in + ".txt!");
						}
						catch(IOException e) {
							System.out.println("An error occurred while saving.");
						}
					}
				}
				else if(input.contains("available-moves")) {
					int index = input.indexOf('(');
					String loc = input.substring(index + 1, index + 3);
					Piece p =  game.getGrid().get(new Location(loc));
					ArrayList<Location> moves = p.getMoves();
					System.out.println("Available moves for " + p + ":");
					if(moves.isEmpty())
						System.out.println("none");
					else
						for(Location l : moves) {
							System.out.println(l);
						}
				}
				else if(input.equalsIgnoreCase("show-board"))
					System.out.print("\n" + game.getGrid() + "\n\n");
				else if(input.equalsIgnoreCase("show-moves"))
					System.out.println(game.toString() + "\n");
				else if(input.equalsIgnoreCase("toggle-invert")) {
					auto_invert = !auto_invert;
					System.out.println("Auto-invert is now set to " + (auto_invert ? "on." : "off."));
					printBoard(auto_invert, game);
				}
				else if(input.equalsIgnoreCase("draw") || input.equalsIgnoreCase("draws")) {
					if(draw_offered) {
						result = DRAW;
						break;
					}
					else {
						System.out.println("To offer a draw, make a move and append 'draw?' afterwards.");
						System.out.println("For example, 'f3 f7 draw?'\n");
						continue;
					}
				}
				else
					System.out.println("That's not a valid input. Type 'help' to see a list of valid commands.");
				continue;
			}

			boolean reset_draw = draw_offered;
			draw_offered = false;
			Location src, dest;
			try {
				src = new Location(st.nextToken());
				dest = new Location(st.nextToken());
			}
			catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
				continue;
			}
			
			// Checking for extra input
			String third = null;
			if(st.hasMoreTokens())
				third = st.nextToken();
			if(third != null) {
				if(third.equalsIgnoreCase("draw?")) {
					if(st.hasMoreTokens()) {
						System.out.println("You've entered too many things.");
						draw_offered = reset_draw;
						continue;
					}
					draw_offered = true;
				}
				else if(third.equalsIgnoreCase("draw")) {
					System.out.println("Append 'draw?' to a move to offer a draw.");
					draw_offered = reset_draw;
					continue;
				}
				else if(third.length() != 1) {
					System.out.println("That's not a valid input. Type 'help' to see a list of valid commands.");
					draw_offered = reset_draw;
					continue;
				}
			}
			
			// This only happens when you offer a draw on a pawn promotion move
			String fourth = null;
			if(st.hasMoreTokens())
				fourth = st.nextToken();
			if(fourth != null) {
				if(fourth.equalsIgnoreCase("draw?"))
					draw_offered = true;
				else if(fourth.equalsIgnoreCase("draw")) {
					System.out.println("Append 'draw?' to a move to offer a draw.");
					draw_offered = reset_draw;
					continue;
				}
				else {
					System.out.println("That's not a valid input. Type 'help' to see a list of valid commands.");
					draw_offered = reset_draw;
					continue;
				}
			}
			
			Piece p = game.getGrid().get(src);
			if(p == null) {
				System.out.println("There's no piece at location " + src + ".");
				draw_offered = reset_draw;
				continue;
			}
			if(p.isWhite() != game.isWhitesTurn()) {
				System.out.print("It's " + (game.isWhitesTurn() ? "white" : "black") + "'s turn. ");
				System.out.println("You can't move your opponent's pieces.");
				draw_offered = reset_draw;
				continue;
			}
			try {
				boolean check;
				if(third != null && third.length() == 1)
					check = game.move(src, dest, third.charAt(0));
				else
					check = game.move(src, dest);
				if(game.isCheckmate()) {
					result = game.isWhitesTurn() ? BLACK_VICTORY : WHITE_VICTORY;
					break;
				}
				else if(check)
					System.out.println("Check!");
			}
			catch(IllegalMoveException e) {
				System.out.println(e.getMessage());
				Piece piece = e.getPiece();
				ArrayList<Location> locs = piece.getMoves();
				if(locs.isEmpty())
					System.out.println("This piece has no locations it can move to.");
				else {
					System.out.print("This piece can move to: ");
					for(Location loc : locs) {
						System.out.print(loc + " ");
					}
					System.out.println();
				}
				draw_offered = reset_draw;
				continue;
			}
			catch(InvalidLocationException e) {
				System.out.println(e.getMessage());
				draw_offered = reset_draw;
				continue;
			}
			catch(InCheckException e) {
				System.out.println(e.getMessage());
				System.out.println("The following moves are allowed:");
				for(Move m : e.getAvailableMoves())
					System.out.println(m);
				draw_offered = reset_draw;
				continue;
			}
			
			printBoard(auto_invert, game);
		}
		while(true);
		
		if(result == WHITE_VICTORY)
			System.out.println("White wins!\n1-0");
		else if(result == BLACK_VICTORY)
			System.out.println("Black wins!\n0-1");
		else
			System.out.println("It's a draw!\n1/2-1/2");
	}
	
	private static void printBoard(boolean auto_invert, Game g) {
		if(!auto_invert && g.getGrid().isInverted())
			g.getGrid().invert();
		else if(g.getGrid().isInverted() == g.isWhitesTurn())
			g.getGrid().invert();
		System.out.println("\n" + g.getGrid() + "\n");
	}
	
	private static void showHelp() {
		StringBuilder sb = new StringBuilder(700);
		sb.append("# Movement #\n");
		sb.append("To move a piece, type the piece's current square, then its destination.\n");
		sb.append("For example, the move 1. e4 would be 'e2 e4'.\n");
		sb.append("Type 'show-moves' to list all moves that have been played.");
		sb.append("Type 'undo' to undo the last move.");
		sb.append('\n');
		sb.append("# Draw #\n");
		sb.append("To offer a draw, move a piece and append 'draw?'.\n");
		sb.append("e.g. 'c7 c5 draw?'\n");
		sb.append("If you've been offered a draw, accept it by typing 'draw'.\n");
		sb.append("Reject it by simply making a move.\n");
		sb.append('\n');
		sb.append("# Displaying the Board #\n");
		sb.append("To re-display the board, type 'show-board'.\n");
		sb.append("The board is automatically inverted after every move.\n");
		sb.append("Type 'toggle-invert' to turn it on or off.");
		System.out.println(sb.toString());
	}
}