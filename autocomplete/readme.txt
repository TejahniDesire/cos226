Programming Assignment 3: Autocomplete


/* *****************************************************************************
 *  Describe how your firstIndexOf() method in BinarySearchDeluxe.java
 *  finds the first index of a key that is equal to the search key.
 **************************************************************************** */

Step1: conducting the regular binarysearch to find one target key. 
Step2: Instead of stopping there, conduct further rounds of binarysearch 
       to the left. 
Step3: Iterate Step1 and 2, until lo > high 


/* *****************************************************************************
 *  Identify which sorting algorithm (if any) that your program uses in the
 *  Autocomplete constructor and instance methods. Choose from the following
 *  options:
 *
 *    none, selection sort, insertion sort, mergesort, quicksort, heapsort
 *
 *  If you are using an optimized implementation, such as Arrays.sort(),
 *  select the principal algorithm.
 **************************************************************************** */

Autocomplete() : Arrays,sort(), or Dual-Pivot Quicksort 
reference: https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html

allMatches() : Arrays,sort(), or Dual-Pivot Quicksort 

numberOfMatches() : None. 

/* *****************************************************************************
 *  How many compares (in the worst case) does each of the operations in the
 *  Autocomplete data type make, as a function of both the number of terms n
 *  and the number of matching terms m? Use Big Theta notation to simplify
 *  your answers.
 *
 *  Recall that with Big Theta notation, you should discard both the
 *  leading coefficients and lower-order terms, e.g., Theta(m^2 + m log n).
 **************************************************************************** */

Autocomplete():     Theta(  n log n  )

allMatches():       Theta(  log n + m log m  )

numberOfMatches():  Theta(  log n  )




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

N/A

/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */

N/A 


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** */
First Group Assignment. Excited. 
