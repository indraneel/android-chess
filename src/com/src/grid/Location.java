package com.src.grid;

/**
 * Represents a location on a chessboard. 
 * @author Kyle
 */
public class Location {
	private int rank, file;
	
	/**
     * The direction one rank up.
     */
    public static final int NORTH = 0;
    
    /**
     * The direction one rank up and one file right.
     */
    public static final int NORTHEAST = 45;
    
    /**
     * The direction one file left.
     */
    public static final int WEST = 270;
    
    /**
     * The direction one rank up and one file left.
     */
    public static final int NORTHWEST = 315;
    
    /**
     * The direction one rank right.
     */
    public static final int EAST = 90;
    
    /**
     * The direction one rank down and one rank right.
     */
    public static final int SOUTHEAST = 135;
    
    /**
     * The direction one rank down.
     */
    public static final int SOUTH = 180;
    
    /**
     * The direction one rank down and one file left.
     */
    public static final int SOUTHWEST = 225;
	
    /**
     * Create a new location.
     * @param rank The location's rank
     * @param file The location's file
     */
	public Location(int rank, int file) {
		super();
		this.rank = rank;
		this.file = file;
	}
	
	/**
	 * Creates a new location from chess coordinates.
	 * @param str A location in chess coordinates.
	 * @throws IllegalArgumentException if the String is not a valid location.
	 */
	public Location(String str) throws IllegalArgumentException {
		super();
		
		if(str.length() != 2) {
			throw new IllegalArgumentException("Must be in file-rank format.");
		}
		
		this.file = str.charAt(0) - 'a';
		this.rank = str.charAt(1) - '1';
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Location)) {
			return false;
		}
		
		Location loc = (Location) o;
		return loc.file == file && loc.rank == rank;
	}
	
	/**
	 * Gets an adjacent location to this object in the given direction.
	 * @param direction The direction to move in.
	 * @return An adjacent location.
	 * @throws IllegalArgumentException if the direction is not a multiple of
	 * forty-five degrees.
	 */
	public Location getAdjacentLocation(int direction) throws IllegalArgumentException {
		while(direction < 0)
			direction += 360;
		direction %= 360;
		
		switch(direction) {
			case NORTH:
				return new Location(rank + 1, file);
			case NORTHEAST:
				return new Location(rank + 1, file + 1);
			case NORTHWEST:
				return new Location(rank + 1, file - 1);
			case WEST:
				return new Location(rank, file - 1);
			case EAST:
				return new Location(rank, file + 1);
			case SOUTHEAST:
				return new Location(rank - 1, file + 1);
			case SOUTH:
				return new Location(rank - 1, file);
			case SOUTHWEST:
				return new Location(rank - 1, file - 1);
			default:
				throw new IllegalArgumentException("" + direction + " is not a" +
						" multiple of 45 degrees.");
		}
	}
	
	/**
	 * @return The column number of this location.
	 */
	public int getFile() {
		return file;
	}
	
	/**
	 * @return The row number of this location.
	 */
	public int getRank() {
		return rank;
	}
	
	@Override
	public int hashCode() {
		return rank * 3737 + file;
	}
	
	@Override
	public String toString() {
		if(rank < 0 || rank > 7 || file < 0 || file > 7) {
			return "(" + file + ", " + rank + ")";
		}
		
		StringBuilder str = new StringBuilder(2);
		str.append((char)('a' + file));
		str.append((char)('1' + rank));
		return str.toString();
	}
}