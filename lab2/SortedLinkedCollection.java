import testSortCol.*;
import datastructures.*;
import java.util.*;

/**
 * An implementation of a collection as a singly linked list. 
 * The elements are sorted, but may contain duplicates.
 * @author	Emanuel Ferm, Miriam Hammarén
 * @version	090209
 */
public class SortedLinkedCollection<E extends Comparable<? super E>> 
			extends LinkedCollection<E> 
			implements CollectionWithGet<E> {
	
	/**
	 * Adds an element to the collection. 
	 * The element is added in its correct place, with regard to it being a 
	 * Comparable element. 
	 * 
	 * @param	element the object to add into the list
	 * @return 	true if the object has been added to the list.
	 * @throws	NullPointerException if parameter <tt>e<tt> is null. 
	 */
	public boolean add( E e ) {
		if ( e == null ) {
			throw new NullPointerException();
		}
		Entry temp = new Entry( e, null );
		if ( head == null ) {
			head = temp;
		}
		// if e is smaller than current head
		else if ( e.compareTo( head.element ) < 0 ) {
			temp.next = head;
			head = temp;
		}
		else {
			Entry i;
			// loop through Entries until finds greater element than e
			for (i = head; i.next != null; i = i.next) {
				if ( e.compareTo( (i.next).element ) < 0 ) {
					break; // if next element is greater
				}
			}
			// squeeze in e between i and the greater element
			temp.next = i.next;
			i.next = temp;
		}
		return true;
	}
	
	/**
	 * Find the first occurence of an element in the collection that is equal 
	 * to the argument <tt>e</tt> with respect to its natural order.
	 * I.e. <tt>e.compateTo(element)</tt> is 0.
	 *
	 * @param 	e The dummy element to compare to.
	 * @return	An element	<tt>e'</tt> in the collection
	 *			satisfying <tt>e.compareTo(e') == 0</tt>.
	 *			If no element is found, <tt>null</tt> is returned
	 */
	public E get( E e ) {
		if ( e == null ) {
			return null;
		}
		Entry temp = head;
		while ( temp != null && temp.element != null ) {
			int i = e.compareTo( temp.element );
			if ( i == 0 ) {
				return temp.element;
			} else if ( i < 0 ) {
				return null; // if e is smaller no need for further search
			}
			temp = temp.next;
		}
		return null;
	}
	
	public static void main(String[] args) {
		SortedLinkedCollection<Integer> slc = 
				new SortedLinkedCollection<Integer>();
		System.out.println("Testing add()\n-------------");
		System.out.println("Input sequence: 3, 0, 4, 1, -1, 5, 2");
		slc.add(new Integer(3));
		slc.add(new Integer(0));
		slc.add(new Integer(4));
		slc.add(new Integer(1));
		slc.add(new Integer(-1));
		slc.add(new Integer(5));
		slc.add(new Integer(2));
		Iterator<Integer> it = slc.iterator();
		System.out.print("Output sequence: "); System.out.flush();
		System.out.print(it.next().toString()); System.out.flush();
		while ( it.hasNext() ) {
			System.out.print(", " + it.next().toString()); System.out.flush();
		}
		System.out.println("\n");
		System.out.println("Testing get()\n-------------");
		System.out.println("get(-1): " + slc.get(new Integer(-1)));
		System.out.println("get(-2): " + slc.get(new Integer(-2)));
		System.out.println("get(4): " + slc.get(new Integer(4)));
		System.out.println("get(10): " + slc.get(new Integer(10)));
		System.out.println("get(0): " + slc.get(new Integer(0)));
		System.out.println("get(null): " + slc.get(null));
	}
}