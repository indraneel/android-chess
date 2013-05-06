package com.src.grid;

/**
 * Represents a grid made of locations that can hold arbitrary objects. 
 * @author Kyle
 * @param <E> The type of object to place on the grid.
 */
public interface Grid<E> {
	/**
	 * Gets the occupant of a given location on the grid.
	 * @param loc A location on this grid.
	 * @return The object occupying it, or <code>null</code> if it is empty.
	 */
	public E get(Location loc);
	
	/**
	 * Inverts the grid so that it is rendered upside-down. Undo the change by
	 * calling this method again.
	 */
	public void invert();
	
	/**
	 * @return True if the grid is displayed upside-down; false otherwise.
	 */
	public boolean isInverted();
	
	/**
	 * @return The number of columns on the grid.
	 */
	public int getCols();
	
	/**
	 * @return The number of rows on the grid.
	 */
	public int getRows();
	
	/**
	 * @param loc The location in question.
	 * @return True if the given location is occupied; false otherwise.
	 */
	public boolean isOccupied(Location loc);
	
	/**
	 * @param loc The location in question.
	 * @return True if the location is valid in this grid; false otherwise.
	 */
	public boolean isValid(Location loc);
	
	/**
	 * Places an object on the given location.
	 * @param loc The location in which to place it in.
	 * @param obj The object to place on the grid.
	 * @return The old occupant of the location, or <code>null</code> if it was
	 * previously unoccupied.
	 */
	public E put(Location loc, E obj);
	
	/**
	 * Removes the object at the given location, if any.
	 * @param loc The location to clear.
	 */
	public void remove(Location loc);
	
	/**
	 * @return "Draws" this grid into a pictorial String representation and
	 * returns it.
	 */
	public String draw();
}