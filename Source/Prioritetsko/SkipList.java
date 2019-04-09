// Skip List Implementation used by Priority Queue
// Written by Ben Faria

package Prioritetsko;

import java.io.*;
import java.util.*;

// Node class which ensures data type extends Comparable.
class Node<AnyType extends Comparable<AnyType>>
{
	AnyType data;
	ArrayList<Node<AnyType>> levels;

	// Initialize new node with passed in height.
	Node(int height)
	{
		this.levels = new ArrayList<>(height);

		for (int i = 0; i < height; i++)
			this.levels.add(null);
	}

	// Initialize new node with passed in height and data.
	Node(AnyType data, int height)
	{
		this.data = data;
		this.levels = new ArrayList<>(height);

		for (int i = 0; i < height; i++)
			this.levels.add(null);
	}

	// Return node value.
	public AnyType value()
	{
		return this.data;
	}

	// Return node height.
	public int height()
	{
		return this.levels.size();
	}

	// Return next node at passed in level.
	public Node<AnyType> next(int level)
	{
		if (level >= 0 && level < height())
			return this.levels.get(level);
		else
			return null;
	}

	// (Suggested) Set next node at passed in level.
	public void setNext(int level, Node<AnyType> node)
	{
		this.levels.set(level, node);
	}

	// (Suggested) Grow node height by 1 level.
	public void grow()
	{
		this.levels.add(null);
	}

	// (Suggested) 50% chance of growing by one level.
	public void maybeGrow()
	{
		int flip = (int)(Math.random() * 2);

		if (flip == 1)
			this.levels.add(null);
	}

	// (Suggested) Trim node height to passed in height.
	public void trim(int height)
	{
		for (int i = height; i < this.levels.size(); i++)
			this.levels.remove(i);

		this.levels.trimToSize();
	}
}

public class SkipList<AnyType extends Comparable<AnyType>>
{
	private Node<AnyType> head;
	private int size;

	// Initialize new Skip List to have a height of 1.
	SkipList()
	{
		head = new Node<AnyType>(1);
		size = 0;
	}

	// Initialize new Skip List to the passed in height.
	SkipList(int height)
	{
		if (height <= 1)
			head = new Node<AnyType>(1);
		else
			head = new Node<AnyType>(height);

		size = 0;
	}

	public int size()
	{
		return size;
	}

	public int height()
	{
		return head.height();
	}

	public Node<AnyType> head()
	{
		return head;
	}

	// This method generates a random height and pass the data and height generated to other insert method.
	public void insert(AnyType data)
	{
		insert(data, generateRandomHeight(getMaxHeight(size + 1)));
	}

	// This method takes in a height and data value to insert a new node into the Skip List,
	// it first checks if the new node will make the max height grow then proceeds to insert the new node.
	public void insert(AnyType data, int height)
	{
		if (getMaxHeight(size + 1) > height())
		{
			head.grow();
			possibleGrowers(head, (height() - 2));
		}

		ArrayList<Node<AnyType>> pointers = pointerArray(data, height);

		Node<AnyType> next = new Node<>(data, height);

		for (int i = 0; i < height; i++)
		{
			next.setNext(i, pointers.get(i).next(i));
			pointers.get(i).setNext(i, next);
		}

		size++;
	}

	// This method deletes the left most node with this data value and then checks if the change 
	// in size results in the max height falling.
	public void delete(AnyType data)
	{
		Node<AnyType> current = getDelete(data);

		if (current != null)
		{
			ArrayList<Node<AnyType>> pointers = pointerArray(data, current.height());

			for (int i = 0; i < current.height(); i++)
			{
				pointers.get(i).setNext(i, current.next(i));
				current.setNext(i, null);
			}

			current = null;
			size--;

			while (height() != getMaxHeight(size))
			{
				possibleTrimmers(head, (height() - 1));
				head.trim(height() - 1);
			}

		}

	}

	// This method searches for the node with the passed in data value in O(log n) time,
	// returning either true or false.
	public boolean contains(AnyType data)
	{
		Node<AnyType> temp = head;
		Node<AnyType> prev = temp;
		int i = height() - 1;

		while (i >= 0)
		{
			temp = prev.next(i);
			if (temp == null)
			{
				i--;
				continue;
			}
			if (data.compareTo(temp.value()) < 0)
			{
				temp = prev.next(--i);
			}
			else if (data.compareTo(temp.value()) == 0)
			{
				return true;
			}
			else if (data.compareTo(temp.value()) > 0)
			{
				prev = temp;
				temp = temp.next(i);
			}
		}

		return false;
	}

	// First node reached in search
	public Node<AnyType> get(AnyType data)
	{
		Node<AnyType> temp = head;
		Node<AnyType> prev = temp;
		Node<AnyType> next = head.next(0);
		int i = height() - 1;

		while (i >= 0)
		{
			temp = prev.next(i);
			if (temp == null)
			{
				i--;
				continue;
			}
			if (data.compareTo(temp.value()) < 0)
			{
				temp = prev.next(--i);
			}
			else if (data.compareTo(temp.value()) == 0)
			{
				return temp;
			}
			else if (data.compareTo(temp.value()) > 0)
			{
				prev = temp;
				temp = temp.next(i);
			}
		}

		return null;
	}

	// (Helper) This method returns the leftmost node which contains the data value.
	public Node<AnyType> getDelete(AnyType data)
	{
		Node<AnyType> temp = head;
		Node<AnyType> prev = temp;
		Node<AnyType> next = head.next(0);
		Node<AnyType> selected = null;
		int i = height() - 1;

		while (i >= 0)
		{
			temp = prev.next(i);
			if (temp == null)
			{
				i--;
				continue;
			}
			if (data.compareTo(temp.value()) < 0)
			{
				temp = prev.next(--i);
			}
			else if (data.compareTo(temp.value()) == 0)
			{
				// If it finds the value continue searching to make sure 
				// it is the leftmost occurence.
				selected = temp;
				i--;
			}
			else if (data.compareTo(temp.value()) > 0)
			{
				prev = temp;
				temp = temp.next(i);
			}
		}

		return selected;
	}

	// (Suggested) Returns the max height of a Skip List with n nodes.
	private static int getMaxHeight(int n)
	{
		//if (n == 0)
		//	return 0;
		if (n <= 1)
			return 1;
		else
			return (int)Math.ceil(Math.log(n) / Math.log(2));
	}

	// (Suggested) Returns a random height up to the max height of the Skip List.
	// 50%, 25%, 12.5%...
	private static int generateRandomHeight(int maxHeight)
	{
		int flip;
		int height = 1;

		for (int i = 0; i < maxHeight; i++)
		{
			flip = (int)(Math.random() * 2);
			if (flip == 1)
				return height;
			else 
				height++;
		}

		return maxHeight;
	}

	// (Helper) If Skip List height is growing, this method will check for all nodes that could
	// possibly grow and gives them the chance to do so.
	private void possibleGrowers(Node<AnyType> head, int level)
	{
		Node<AnyType> prev = head;
		Node<AnyType> next = head.next(level);

		while (next != null)
		{
			next.maybeGrow();

			if (next.height() == level + 2)
			{
				prev.setNext((level + 1), next);
				prev = next;
			}

			next = next.next(level);

		}
	}

	// (Helper) If Skip List height is shrinking, this method will check for all nodes
	// that need to shrink and trim them.
	private void possibleTrimmers(Node<AnyType> head, int level)
	{
		Node<AnyType> next = head.next(level);
		ArrayList<Node<AnyType>> trimmers = new ArrayList<>();

		while (next != null)
		{
			trimmers.add(next);
			next = next.next(level);
		}

		if (!trimmers.isEmpty())
		{
			for (int i = 0; i < trimmers.size(); i++)
				trimmers.get(i).trim(level);
		}
	}

	// (Helper) This method takes in a data value and height and creates an ArrayList that stores
	// all of the previous nodes for this new node at each level.
	private ArrayList<Node<AnyType>> pointerArray(AnyType data, int height)
	{
		Node<AnyType> temp = head;
		Node<AnyType> prev = temp;
		ArrayList<Node<AnyType>> pointers = new ArrayList<>(height);

		for (int j = 0; j < height; j++)
			pointers.add(null);

		int i = height() - 1;
		
		while (i >= 0)
		{
			temp = prev.next(i);
			if (temp == null)
			{
				if (i < height)
					pointers.set(i, prev);
				i--;
			}
			else
			{
				if (data.compareTo(temp.value()) < 0)
				{
					if (i < height)
						pointers.set(i, prev);
					i--;
					continue;
				}
				else if (data.compareTo(temp.value()) == 0)
				{
					if (i < height)
						pointers.set(i, prev);
					i--;
					continue;			
				}
				else if (data.compareTo(temp.value()) > 0)
				{
					prev = temp;
				}
			}
		}

		return pointers;
	}

	// (Helper) This method prints out the Skip List for debugging purposes with format:
	// Level i contains: ([Value of node] [Height of node])
	public void printSkipList()
	{
		Node<AnyType> temp = head;

		if (temp.next(0) == null)
			System.out.println("SkipList is empty!");
		else
		{
			for (int i = 0; i < height(); i++)
			{
				System.out.print("Level " + i + " Contains:" + ((temp.next(i) != null) ? " " : "\n"));
				temp = temp.next(i);
				while (temp != null)
				{
					System.out.print("(" + temp.value() + " " + temp.height() + ")" + ((temp.next(i) != null) ? " " : "\n"));
					temp = temp.next(i);
				}
				temp = head;
			}
		}
		
	}

	public static double difficultyRating()
	{
		return 4.0;
	}

	public static double hoursSpent()
	{
		return 12.0;
	}
}