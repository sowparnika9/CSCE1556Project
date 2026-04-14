package com.fmt;

public class ListNode<T> {

    private ListNode<T> next;
    private ListNode<T> previous;
    private T item;

    public ListNode(T item) {
        this.item = item;
        this.next = null;
    }

    public T getNode() {
        return item;
    }

    public ListNode<T> getNext() {
        return this.next;
    }
    
    public ListNode<T> getPrevious() {
        return this.previous;
    }
    

    public void setNext(ListNode<T> next) {
        this.next = next;
    }

}
