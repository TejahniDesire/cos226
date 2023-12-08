Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

 	The overal all method was to treat the energy matrix as an implicit DAG
 	Digraph and relax the edges in topological order. This was down by
 	treating the energy values as weighted edges between nodes and scanning
 	left to right (for horizontal seams) and top to bottom (for vertical
 	seams) for all edges in the current Column/Row iteration

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

	Images that work well for seam carving are those with distincted
	features along side large areas of non distinctive ones. Bad images
	would be those with no distinctive features, that is a uniform
	distribution of RGB values for all pixels. 

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
1000		.388		 
2000		0.669		 1.7242	      0.7859
4000		1.37 		 2.047        1.0335
8000		2.317		 1.69	      0.7570
16000		4.969		 2.144	      1.1003
32000		9.93		 1.998	      0.9985
				       mean: 0.93504

(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
1000		0.391
2000		0.674		 1.7237	      0.7855
4000		1.25		 1.8545	      0.8910
8000		2.5		 2            1
16000		5.173		 5.173	      2.3710
32000		13.209		 2.553	      1.3521

				       mean: 1.27992
/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula 
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ 1.58759816*10^-7 * H^0.93504 * W^1.27992
    
    This time was recieved by gaining the exponent of H and W through the 
    average log base 2 of the ratio of doubled inputs. The coefficient
    in front was found by solving for it through the formula
    .388 = Coeff * H^0.93504 * W^1.27992
    where Coeff = 1.58759816*10^-7
       _______________________________________




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

	None


/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */

	None


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** */
 
	This was a highly difficult but stimulating project. The fact that 
	there was only one file to submit meant we (two partners) had to
	put more thought into how we divided the work. In the end it lead
	to use having to extensively read eachothers code more than usual,
	forcing an understanding I believe to be more representative of
	coding in the field. 
