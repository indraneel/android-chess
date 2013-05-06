package com.src.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.StringTokenizer;

import com.src.grid.Chessboard;
import com.src.move.Move;
import com.src.move.MoveSequence;
import com.src.util.CalendarUtils;

/**
 * Allows you to replay a game, one move at a time.
 * @author kyle
 */
public class Playback implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<Chessboard> boards;
	private Calendar date;
	private Game game;
	private MoveSequence moves;
	private String title;
	private int count;
	
	/**
	 * Create a new record of a game.
	 * @param moves The moves played in sequence.
	 * @param title The title of the game.
	 */
	public Playback(MoveSequence moves, String title) {
		super();
		this.moves = moves;
		this.title = title;
		this.date = Calendar.getInstance();
		this.boards = new ArrayList<Chessboard>();
		this.count = 0;
	}
	
	public String getDate() {
		StringBuilder sb = new StringBuilder();
		sb.append(date.get(Calendar.MONTH));
		sb.append('/');
		sb.append(date.get(Calendar.DAY_OF_MONTH));
		sb.append('/');
		sb.append(date.get(Calendar.YEAR));
		return sb.toString();
	}
	
	public Game getGame() {
		return game;
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return True if there is another move to play in the simulation; false
	 * otherwise.
	 */
	public boolean hasNext() {
		return moves.currentMove() != null;
	}
	
	/**
	 * @return True if there is a move to undo in the simulation; false
	 * otherwise.
	 */
	public boolean hasPrevious() {
		return moves.hasPrevious();
	}
	
	/**
	 * Begins playback of this game.
	 */
	public void startPlayback() {
		game = new Game(new Chessboard(), false);
		moves.reset();
		count = 0;
		boards.clear();
	}
	
	/**
	 * Plays the next game in the simulation.
	 */
	public void next() {
		if(moves.currentMove() == null)
			return;
		
		//boards.set(count, new Chessboard(game.getGrid().getEntrySet()));
		boards.add(new Chessboard(game.getGrid().getEntrySet()));
		Move m = moves.currentMove();
		if (m.isPromotion()) {
			game.move(m.getSource(), m.getDestination(),
					m.getSpecial().getDescription().charAt(0));
		}
		else
			game.move(m.getSource(), m.getDestination());
		
		count++;
		moves.nextMove();
	}
	
	/**
	 * Reverts to the last move in the simulation.
	 */
	public void previous() {
		if(!moves.hasPrevious())
			return;
		
		System.out.println("Undoing move " + moves.currentMove());
		game.setGrid(boards.get(--count));
		moves.lastMove();
	}
	
	/**
	 * Saves both the data for this game and its move sequence in a convenient
	 * string.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append('#');
		sb.append(CalendarUtils.convertToString(date));
		sb.append('#');
		sb.append(moves.toString());
		return sb.toString();
	}
	
	/**
	 * Parses a game from a string.
	 * @param str The string to parse.
	 * @return A new recorded game.
	 */
	public static Playback fromString(String str) {
		StringTokenizer st = new StringTokenizer(str, "#");
		
		String title = st.nextToken();
		Calendar c = CalendarUtils.parseCalendar(st.nextToken());
		MoveSequence m = MoveSequence.fromString(st.nextToken());
		
		Playback r = new Playback(m, title);
		r.date = c;
		return r;
	}
	
	/**
	 * @return A Comparator to use to sort games by date.
	 */
	public static final Comparator<Playback> getDateComparator() {
		return new Comparator<Playback>() {
            public int compare(Playback l, Playback r) {
            	Calendar lhs = l.date, rhs = r.date;
            	return CalendarUtils.convertToString(lhs).compareTo(
            			CalendarUtils.convertToString(rhs));
            }
		};
	}
	
	/**
	 * @return A Comparator to use to sort games by title.
	 */
	public static final Comparator<Playback> getTitleComparator() {
		return new Comparator<Playback>() {
			public int compare(Playback lhs, Playback rhs) {
				return lhs.title.compareTo(rhs.getTitle());
			}
		};
	}
}