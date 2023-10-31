Programming Assignment 5: K-d Trees


/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */

	Node Contains a variable for the point coordinates, the stored value, its
	right and left child and the rectangle it contains

/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */

    Starting from the root, recursively search along the 2-d tree. In every
    search, if a target point is found, enqueue that point into a Queue. Then,
    check if the splitting life segment intersects with the rectangle. If it
    does, search both subtrees; otherwise prune the subtree that is NOT on the
    same side with the rectangle. Eventually, return the Queue.


/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */

	recursively search the 2D BST while maintaining a champion variable
	with the end condition to return if no changes have been made to the
	current champion or if the current node being checked is null. The
	child to be searched next is either the one whose rectangle contains
	the point in question or the only child. Children whose rectangle
	is at a larger distance to the point in interest than the current
	champion are not searched.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:	100		14.3			7

KdTreeST:	10,000,000      39.8			251,237.3

Note: more calls per second indicates better performance.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

	None

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

	General tracing of recursive loops was tricky to find bugs efficiently

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
