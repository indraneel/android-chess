package com.src.move;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Represents a linked list of moves. Allows one user to iterate both forwards
 * and backwards; however there is no support for concurrent iteration.
 * @author Kyle
 */
public class MoveSequence implements Serializable {
	/**
	 * A bidirectional linked list node.
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
	
	private Node<Move> head, last, current;
	
	/**
	 * Creates a new empty move sequence.
	 */
	public MoveSequence() {
		super();
		this.head = null;
		this.current = null;
		this.last = null;
	}
	
	/**
	 * Appends a move to the end of this move list.
	 */
	public void append(Move m) {
		if(head == null) {
			head = new Node<Move>(m);
			head.prev = null;
			last = head;
			current = head;
		}
		else {
			last.next = new Node<Move>(m);
			Node<Move> prev = last;
			last = last.next;
			last.prev = prev;
		}
	}
	
	/**
	 * Deletes the last move in the sequence.
	 * @throws NoSuchElementException if there are no moves to remove.
	 */
	public void delete() {
		if(isEmpty())
			throw new NoSuchElementException("Sequence is empty.");
		
		if(last == head) {
			head = null;
			last = null;
			current = null;
		}
		else {
			if(current == last)
				current = last.prev;
			last = last.prev;
			last.next = null;
		}
	}
	
	/**
	 * @return The current move in the iteration. If the sequence has no moves
	 * or the iteration has reached the end, this method returns
	 * <code>null</code>.
	 */
	public Move currentMove() {
		return current == null ? null : current.data;
	}
	
	/**
	 * @return True if there is another move in the sequence; false otherwise.
	 */
	public boolean hasNext() {
		return current != null && current.next != null;
	}
	
	/**
	 * @return True if there is a move before this move in the sequence; false
	 * otherwise.
	 */
	public boolean hasPrevious() {
		return current != null && current.prev != null;
	}
	
	/**
	 * @return True if there are no moves stored in this sequence; false
	 * otherwise.
	 */
	public boolean isEmpty() {
		return head == null;
	}
	
	/**
	 * Moves the iteration backwards to the previous move in the sequence.
	 */
	public void lastMove() {
		if(current == null)
			current = last;
		else
			current = current.prev;
	}
	
	/**
	 * Moves the iteration forward to the next move in the sequence.
	 * @return The next move played, or <code>null</code> if at the end of the
	 * sequence.
	 */
	public void nextMove() {
		if(current == null)
			return;
		
		current = current.next;
	}
	
	/**
	 * Resets the iteration back to the start of the move sequence.
	 */
	public void reset() {
		current = head;
	}
	
	/**
	 * A sequence of moves in extended notation, or the empty string if no
	 * moves are in the sequence.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node<Move> node = head;
		while(node != null) {
			sb.append(node.data.toString());
			sb.append('|');
			node = node.next;
		}
		return sb.toString();
	}
	
	/**
	 * Creates a MoveSequence from a properly-formatted String.
	 * @param str The String to convert, in the format of the toString()
	 * method of this class.
	 * @return A new move sequence.
	 */
	public static MoveSequence fromString(String str) {
		StringTokenizer st = new StringTokenizer(str, "|");
		MoveSequence ms = new MoveSequence();
		
		while(st.hasMoreTokens()) {
			String tok = st.nextToken();
			int pos = tok.indexOf('-');
			Move m = new Move(tok.substring(0, pos), tok.substring(pos + 1));
			pos = tok.indexOf('=');
			if(pos != -1)
				m.setSpecial(Move.Special.parseChar(tok.charAt(pos + 1)));
			ms.append(m);
		}
		return ms;
	}
}