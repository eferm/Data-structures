import testSortCol.*;
import datastructures.*;
import java.util.*;

/**
 * Simple implementation of the splay tree data structure. 
 * A splaying step is only performed when an element is searched for.
 * 
 * @author Emanuel Ferm, Miriam Hammarén
 * @version 090224
 */
public class SplayTree<E extends Comparable<? super E>>
			extends BinarySearchTree<E>
			implements CollectionWithGet<E> {
	/**
	 * Find the first occurence of an element in the collection that is equal 
	 * to the argument <tt>e</tt> with respect to its natural order.
	 * I.e. <tt>e.compateTo(element)</tt> is 0.
	 * 
	 * A splaying step is also performed.
	 *
	 * @param 	e The dummy element to compare to.
	 * @return	An element	<tt>e'</tt> in the collection
	 *			satisfying <tt>e.compareTo(e') == 0</tt>.
	 *			If no element is found, <tt>null</tt> is returned
	 */
	public E get( E e ) {
		if ( e == null || root == null ) {
			return null;
		}
		
		Entry x = find( e, root );
		
		if ( x == null ) {
			return null;
		} else {
			while ( x != root ) {
				if ( x.parent == root ) {
					if ( x.parent.right == x ) {
						// System.out.println("zig(" + x.element + ")");
						zig( x.parent );
					} else if ( x.parent.left == x ) {
						// System.out.println("zag(" + x.element + ")");
						zag( x.parent );
					}
					x = x.parent;
				} else {
					if ( x.parent.parent.right != null && 
								x.parent.parent.right.left == x ) {
						// System.out.println("zigzag(" + x.element + ")");
						zigzag( x.parent.parent );
						x = x.parent;
					} else if ( x.parent.parent.left != null && 
								x.parent.parent.left.right == x ) {
						// System.out.println("zagzig(" + x.element + ")");
						zagzig( x.parent.parent );
						x = x.parent;
					} else if ( x.parent.parent.right != null && 
								x.parent.parent.right.right == x ) {
						// System.out.println("zigzig(" + x.element + ")");
						zigzig( x.parent.parent );
						x = x.parent.parent;
					} else if ( x.parent.parent.left != null && 
								x.parent.parent.left.left == x ) {
						// System.out.println("zagzag(" + x.element + ")");
						zagzag( x.parent.parent );
						x = x.parent.parent;
					}
				}
			}
			if ( e.compareTo(root.element) == 0 )
				return root.element;
			else
				// if splaying step was performed on an element not equal to e
				return null;
		}
	}
	
	/*
	 * Overridden method from BinarySearchTree.java, making sure that the 
	 * closest possible element is splayed even though the exact element 
	 * isn't found
	 */
	protected Entry find( E elem, Entry t ) {
		int jfr = elem.compareTo( t.element );
		if ( jfr  < 0 ) {
			if ( t.left != null )
				return find( elem, t.left );
			else
				return t; // greater element than elem, but close enough
		}
		else if ( jfr > 0 ) {
			if ( t.right != null )
				return find( elem, t.right );
			else
				return t; // smaller element than elem, but close enough
		}
		else
			return t; // perfect match
    }
	
	/**
	 *       p              x
	 *      / \            / \
	 *     A   x    ->    p   C
	 *        / \        / \
	 *       B   C      A   B
	 * 
	 * Same as rotateLeft
	 * 
	 * This step is done when p is the root. The tree is rotated on the edge 
	 * between x and p. Zig steps exist to deal with the parity issue and will 
	 * be done only as the last step in a splay operation and only when x has 
	 * odd depth at the beginning of the operation.
	 * 
	 * @param	p The parent of x, the element to be splayed
	 */
	private void zig( Entry p ) {
		Entry x = p.right;
		E e = p.element;
		
		p.element = x.element;
		x.element = e;
		
		p.right = x.right;
		
		if ( p.right != null )
			p.right.parent = p;
		
		x.right = x.left;
		x.left = p.left;
		
		if ( x.left != null )
			x.left.parent = x;
		
		p.left = x;
	}
	
	/**
	 *         p              x
	 *        / \            / \
	 *       x   C    ->    A   p
	 *      / \                / \
	 *     A   B              B   C
	 * 
	 * Same as rotateRight
	 * 
	 * This step is done when p is the root. The tree is rotated on the edge 
	 * between x and p. Zag steps exist to deal with the parity issue and will 
	 * be done only as the last step in a splay operation and only when x has 
	 * odd depth at the beginning of the operation.
	 * 
	 * @param	p The parent of x, the element to be splayed
	 */
	private void zag( Entry p ) {
		Entry x = p.left;
		E e = p.element;
		
		p.element = x.element;
		x.element = e;
		
		p.left = x.left;
		
		if ( p.left != null )
			p.left.parent = p;
		
		x.left = x.right;
		x.right = p.right;
		
		if ( x.right != null )
			x.right.parent = x;
		
		p.right = x;
	}
	

	
	
	/**
	 *         g                x
	 *        / \             /   \
	 *       p   D           p     g
	 *      / \       ->    / \   / \
	 *     A   x           A   B C   D
	 *        / \
	 *       B   C
	 * 
	 * Same as doubleRotateLeft
	 * 
	 * This step is done when p is not the root and x is a right child and p 
	 * is a left child. The tree is rotated on the edge between x and p, 
	 * then rotated on the edge between x and its new parent g.
	 * 
	 * @param g The grandparent of x, the element to be splayed
	 */
	private void zigzag( Entry g ) {
		Entry p = g.right;
		Entry x = g.right.left;
		E e = g.element;
		
		g.element = x.element;
		x.element = e;
		
		p.left = x.right;
		
		if ( p.left != null )
			p.left.parent = p;
		
		x.right = x.left;
		x.left = g.left;
		
		if ( x.left != null )
			x.left.parent = x;
			
		g.left = x;
		x.parent = g;
	}
	
	/**
	 *       g                 x
	 *      / \              /   \
	 *     A   p            g     p
	 *        / \    ->    / \   / \
	 *       x   D        A   B C   D
	 *      / \
	 *     B   C
	 * 
	 * Same as doubleRotateRight
	 * 
	 * This step is done when p is not the root and x is a left child and p 
	 * is a right child. The tree is rotated on the edge between x and p, 
	 * then rotated on the edge between x and its new parent g.
	 * 
	 * @param g The grandparent of x, the element to be splayed
	 */
	private void zagzig( Entry g ) {
		Entry p = g.left;
		Entry x = g.left.right;
		E e = g.element;
		
		g.element = x.element;
		x.element = e;
		
		p.right = x.left;
		
		if ( p.right != null )
			p.right.parent = p;
		
		x.left = x.right;
		x.right = g.right;
		
		if ( x.right != null )
			x.right.parent = x;
			
		g.right = x;
		x.parent = g;
	}
	
	/**
	 *       g                      x
	 *      / \                    / \
	 *     A   p                  p   D
	 *        / \       ->       / \
	 *       B   x              g   C
	 *          / \            / \
	 *         C   D          A   B
	 * 
	 * This step is done when p is not the root and x and p are both right 
	 * children. The tree is rotated on the edge joining p with its parent g, 
	 * then rotated on the edge joining x with p.
	 * 
	 * @param g The grandparent of x, the element to be splayed
	 */
	private void zigzig( Entry g ) {
		Entry p = g.right;
		Entry x = g.right.right;
		E e = g.element;
		
		g.element = x.element;
		x.element = e;
		
		p.right = x.left;
		g.right = x.right;
		
		if ( p.right != null )
			p.right.parent = p;
		if ( g.right != null )
			g.right.parent = g;
		
		x.left = g.left;
		x.right = p.left;
		
		if ( x.left != null )
			x.left.parent = x;
		if ( x.right != null )
			x.right.parent = x;
		
		g.left = p;
		p.left = x;
	}
	
	/**
	 *           g              x
	 *          / \            / \
	 *         p   D          A   p
	 *        / \       ->       / \
	 *       x   C              B   g
	 *      / \                    / \
	 *     A   B                  C   D
	 * 
	 * This step is done when p is not the root and x and p are both left 
	 * children. The tree is rotated on the edge joining p with its parent g, 
	 * then rotated on the edge joining x with p.
	 * 
	 * @param g The grandparent of x, the element to be splayed
	 */
	private void zagzag( Entry g ) {
		Entry p = g.left;
		Entry x = g.left.left;
		E e = g.element;
		
		g.element = x.element;
		x.element = e;
		
		p.left = x.right;
		g.left = x.left;
		
		if ( p.left != null )
			p.left.parent = p;
		if ( g.left != null )
			g.left.parent = g;
		
		x.right = g.right;
		x.left = p.right;
		
		if ( x.right != null )
			x.right.parent = x;
		if ( x.left != null )
			x.left.parent = x;
		
		g.right = p;
		p.right = x;
	}
	
	/*
	 * Prints the given tree with breadth first traversal.
	 */
	private void printTree(SplayTree<E> splay) {
		LinkedQueue<Entry> que = new LinkedQueue<Entry>();
		Entry nextT;
		que.enqueue( splay.root );
		while ( !que.isEmpty() ) {
			nextT = que.dequeue();
			System.out.print( nextT.element.toString() + ", ");
			
			if ( nextT.left != null ) {
				que.enqueue( nextT.left );
			}
			if ( nextT.right != null ) {
				que.enqueue( nextT.right );
			}
		}
		System.out.println("");
	}
	
	public static void main(String[] args) {
		SplayTree<Integer> splay = new SplayTree<Integer>();
		
		System.out.println("Testing add()...\n----------------");
		System.out.println("Input sequence: 1, 5, 2, -1, 3, 0, 4, -2");
		splay.add(new Integer(1));
		splay.add(new Integer(5));
		splay.add(new Integer(2));
		splay.add(new Integer(-1));
		splay.add(new Integer(3));
		splay.add(new Integer(0));
		splay.add(new Integer(4));
		splay.add(new Integer(-2));
		Iterator<Integer> it = splay.iterator();
		System.out.print("Iterator output: "); System.out.flush();
		System.out.print(it.next().toString()); System.out.flush();
		while ( it.hasNext() ) {
			System.out.print(", " + it.next().toString()); System.out.flush();
		}
		System.out.println("\n");
		
		System.out.println("Testing get()...\n----------------");
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(1): " + splay.get(new Integer(1)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(-1): " + splay.get(new Integer(-1)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(5): " + splay.get(new Integer(5)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(2): " + splay.get(new Integer(2)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(3): " + splay.get(new Integer(3)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(null): " + splay.get(null));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(0): " + splay.get(new Integer(0)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(4): " + splay.get(new Integer(4)));
		System.out.print("Current tree: "); splay.printTree(splay);
		System.out.println("get(7): " + splay.get(new Integer(7)));
		System.out.print("Current tree: "); splay.printTree(splay);
	}
}