package com.src.move;

import java.io.Serializable;

/**
 * Represents a linked list of moves. Allows one user to iterate both forwards
 * and backwards; however there is no support for concurrent iteration.
 * @author Kyle
 */
public class MoveSequence implements Serializable {
	/**
	 * A standard bidirectional linked list node.
	 * @author Kyle
	 * @param <T> The type of data to store.
	 */
	private static class Node<T> {
		T data;
		Node<T> next, prev;
		
		/**
		 * Create a new node with the given data. 
		 * @param data The data to store.
		 */
		public Node(T data) {
			super();
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}
	
	/**
	 * Version 1 of the linked list.
	 */
	private static final long serialVersionUID = 1L;
	
	private Node<Move> head, current, last;
	
	/**
	 * Creates a new empty move sequence.
	 */
	public MoveSequence() {
		super();
		this.head = null;
		this.current = null;
	}
	
	/**
	 * Appends a move to the end of this move list.
	 */
	public void append(Move m) {
		if(head == null) {
			head = new Node<Move>(m);
			last = head;
		}
		else {
			last.next = new Node<Move>(m);
			Node<Move> prev = last;
			last = last.next;
			last.prev = prev;
		}
	}
	
	/**
	 * @return The current move in the iteration. If the sequence has no moves
	 * or the iteration has reached the end, this method returns
	 * <code>null</code>.
	 */
	public Move currentMove() {
		return current.data;
	}
	
	/**
	 * @return True if there is another move in the sequence; false otherwise.
	 */
	public boolean hasNext() {
		return current != null && current.next != null;
	}
	
	/**
	 * @return True if there is a move currently in the iteration.
	 */
	public boolean has() {
		return current != null;
	}
	
	/**
	 * Moves the iteration backwards to the previous move in the sequence.
	 * @return The last move played, or <code>null</code> if at the beginning
	 * of the sequence.
	 */
	public Move lastMove() {
		if(current == head || current == null)
			return null;
		
		Node<Move> node = current;
		current = current.prev;
		return node.data;
	}
	
	/**
	 * @return True if there are no moves stored in this sequence; false
	 * otherwise.
	 */
	public boolean isEmpty() {
		return head == null;
	}
	
	/**
	 * Moves the iteration forward to the next move in the sequence.
	 * @return The next move played, or <code>null</code> if at the end of the
	 * sequence.
	 */
	public Move nextMove() {
		if(current == null)
			return null;
		
		Node<Move> node = current;
		current = current.next;
		return node.data;
	}
	
	/**
	 * Resets the iteration back to the start of the move sequence.
	 */
	public void reset() {
		current = head;
	}
}