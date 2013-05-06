package com.src.pieces;

import java.util.ArrayList;

import com.src.game.Game;
import com.src.grid.Location;


/**
 * Queens can move in any direction along ranks, files, and diagonals. They are
 * worth about nine pawns, equal to about three minor pieces but slightly
 * weaker than two rooks.
 * @author Kyle
 */
public class Queen extends Piece {

	public Queen(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
	}
	
	@Override
	public Piece copy() {
		return new Queen(isWhite(), getGame(), getLocation().copy());
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'Q' : 'q';
	}

	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int dir = 0; dir < 360; dir += 45) {
			Location l = getLocation().getAdjacentLocation(dir);
			while(getGrid().isValid(l)) {
				locs.add(l);
				if(getGrid().isOccupied(l))
					break;
				l = l.getAdjacentLocation(dir);
			}
		}
		return locs;
	}

	@Override
	public ArrayList<Location> getMoveLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int dir = 0; dir < 360; dir += 45) {
			Location l = getLocation().getAdjacentLocation(dir);
			while(getGrid().isValid(l)) {
				if(getGrid().isOccupied(l)) {
					if(!isSameColor(getGrid().get(l))) {
						locs.add(l);
					}
					break;	
				}
				locs.add(l);
				l = l.getAdjacentLocation(dir);
			}
		}
		return locs;
	}
	
	@Override
	public int getValue() {
		return 9;
	}
}