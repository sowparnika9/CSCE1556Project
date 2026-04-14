package com.fmt;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A linked list implementation for <code>Invoice</code> instances.
 *
 */
public class MyLinkedList<T> implements Iterable<T> {

	private int size;
	private ListNode<T> head;
	private final Comparator<T> comparator;

	public MyLinkedList(Comparator<T> comparator) {
		this.comparator = comparator;
		this.size = 0;
		this.head = null;
	}

	/**
	 * This function returns the size of the list, the number of elements currently
	 * stored in it.
	 * 
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * This function clears out the contents of the list, making it an empty list.
	 */
	public void clear() {
		this.head = null;
		this.size = 0;
	}

	/**
	 * Adds element while using given comparator
	 * 
	 * @param item
	 */
	public void addElement(T item) {
		if (item == null) {
			throw new IllegalArgumentException("Cannot insert null elements");
		}
		ListNode<T> newNode = new ListNode<T>(item);

		if (head == null) {
			head = newNode;
		} else if (this.comparator.compare(head.getNode(), item) >= 0) {
			ListNode<T> temp = head;
			head = newNode;
			head.setNext(temp);
		} else {
			ListNode<T> curr = head;
			ListNode<T> prev = null;
			while (curr != null && this.comparator.compare(curr.getNode(), item) < 0) {
				prev = curr;
				curr = curr.getNext();
			}

			if (curr == null) {
				prev.setNext(newNode);
			} else if (this.comparator.compare(curr.getNode(), item) >= 0) {
				prev.setNext(newNode);
				newNode.setNext(curr);
			}
		}
		this.size++;
	}

	/**
	 * This method removes the {@link Invoice} from the given <code>position</code>,
	 * indices start at 0. Implicitly, the remaining elements' indices are reduced.
	 * 
	 * @param position
	 */
	public void remove(int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid position: " + position);
		} else if (position == 0) {
			this.head = this.head.getNext();
		} else {
			ListNode<T> previous = this.getListNode(position - 1);
			ListNode<T> current = previous.getNext();
			ListNode<T> next = current.getNext();
			previous.setNext(next);
		}
		this.size--;
	}

	/**
	 * This is a private helper method that returns a {@link ListNode} corresponding
	 * to the given position. Implementing this method is optional but may help you
	 * with other methods.
	 * 
	 * @param position
	 * @return
	 */
	private ListNode<T> getListNode(int position) {
		ListNode<T> current = this.head;
		for (int i = 0; i < position; i++) {
			current = current.getNext();
		}
		return current;
	}

	/**
	 * Returns the {@link Invoice} element stored at the given
	 * <code>position</code>.
	 * 
	 * @param position
	 * @return
	 */
	public T get(int position) {
		if (position < 0 || position > this.size) {
			throw new IllegalArgumentException("Invalid position: " + position);
		} else {
			T p = this.getListNode(position).getNode();
			return p;
		}

	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private ListNode<T> current = head;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				T value = current.getNode();
				current = current.getNext();
				return value;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
