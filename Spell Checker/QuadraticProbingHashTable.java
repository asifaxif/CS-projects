

// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// AnyType get( x )       --> Return x  
// void makeEmpty( )      --> Remove all items


/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class QuadraticProbingHashTable<AnyType>
{
    /**
     * Construct the hash table.
     */
    public QuadraticProbingHashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public QuadraticProbingHashTable( int size )
    {
        allocateArray( size );
        makeEmpty( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
    			
    	// Insert x as active
        int currentPos = findPos( x );
        
        
        if( isActive( currentPos ) )
            return;

        array[ currentPos ] = new HashEntry<AnyType>( x, true );

            // Rehash; see Section 5.5
        if( ++currentSize > array.length / 2 )
            rehash( );
    }

    /**
     * Expand the hash table.
     */
    private void rehash( )
    {
    	//System.out.println("in rehash");
        HashEntry<AnyType> [ ] oldArray = array;

            // Create a new double-sized, empty table
        allocateArray( nextPrime( 2 * oldArray.length ) );
        currentSize = 0;

            // Copy table over
        for( int i = 0; i < oldArray.length; i++ )
            if( oldArray[ i ] != null && oldArray[ i ].isActive )
                insert( oldArray[ i ].element );
    }

    /**
     * Method that performs quadratic probing resolution.
     * @param x the item to search for.
     * @return the position (index) where the search terminates.
     */
    private int findPos( AnyType x )
    {
    	
    	int index = myhash(x); //original hash function (hash(key))
    	//System.out.println("myHash is: "+index);
    	
    	/**
    	 * To Do:
    	 * Apply quadratic probing to find x's location
    	 **/
		/*
		 * if(array[index] == null) { index = index; } else if
		 * (array[index].element.equals(x)) { index = index; } else { for (int i = 1; i
		 * < array.length; i++) { int index1 = (myhash(x)+i*i)%array.length;
		 * if(array[index1] == null) { index = index1; } else
		 * if(array[index1].element.equals(x)) { index = index1; } } }
		 */
    	
    	int i = 1;
    	while (array[index] != null && !array[index].element.equals(x)) {
    		
    		int quadratic = i*i;
    		index += quadratic;
    		
    		i++;
    		
    		if (index >= array.length) {
    			index -= array.length;
    		}
    	}
        
    	//The following line is for testing only.
    	//To test, run this code (there is a main() method below).
    	//The expected output is in a comment in the main() method below.
    	//Comment the following line out when you feel you have this method working correctly.
    	//System.out.println("final index for "+x+": "+index);
    	
        return index;
    }

   
    
    
    /**
     * Remove from the hash table.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        int currentPos = findPos( x );
        if( isActive( currentPos ) )
            array[ currentPos ].isActive = false;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public boolean contains( AnyType x )
    {
        int currentPos = findPos( x );
        return isActive( currentPos );
    }

    /**
    * Get an item from the hash table.
    * @param x the item to search for.
    * @return null if x is not found.
    */
    public AnyType get(AnyType x)
    {
    	if(contains(x)){
    		int currenPos = findPos(x);
    		return (AnyType) array[currenPos].element;
    	}
    	else
    		return null;
    }
    
    
    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive( int currentPos )
    {
        return array[ currentPos ] != null && array[ currentPos ].isActive;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        currentSize = 0;
        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }

    private int myhash( AnyType x )
    {
        int hashVal = x.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        
        return hashVal;
    }
	
    private static class HashEntry<AnyType>
    {
        public AnyType  element;   // the element
        public boolean isActive;  // false if marked deleted

        public HashEntry( AnyType e )
        {
            this( e, true );
        }

        public HashEntry( AnyType e, boolean i )
        {
            element  = e;
            isActive = i;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 11;

    private HashEntry<AnyType> [ ] array; // The array of elements
    private int currentSize;              // The number of occupied cells

    
    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    @SuppressWarnings("unchecked")
	private void allocateArray( int arraySize )
    {
        array = new HashEntry[ nextPrime( arraySize ) ];
           
    }

   
    
    
    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }


    // Simple main
    public static void main( String [ ] args )
    {
        QuadraticProbingHashTable<String> H = new QuadraticProbingHashTable<String>( );

       
        System.out.println("inserting...");
        H.insert(""+12);
        H.insert(""+3);
        H.insert(""+5);
        H.insert(""+7);
        H.insert(""+28);
        H.insert(""+1);
        H.insert(""+19);
 
        System.out.println("removing...");
        H.remove(""+3);
        H.remove(""+7);
        H.remove(""+1);
        
        System.out.println("calling contains...");
        H.contains(""+3);
        H.contains(""+5);
        H.contains(""+7);
        H.contains(""+28);
        H.contains(""+1);
        H.contains(""+19);
        
       /*
        * Expected Output:
        
        inserting...
		final index for 12: 7
		final index for 3: 8
		final index for 5: 9
		final index for 7: 0
		final index for 28: 1
		final index for 1: 5
		final index for 7: 9
		final index for 28: 19
		final index for 1: 3
		final index for 12: 5
		final index for 3: 6
		final index for 5: 7
		final index for 19: 12
		removing...
		final index for 3: 6
		final index for 7: 9
		final index for 1: 3
		calling contains...
		final index for 3: 6
		final index for 5: 7
		final index for 7: 9
		final index for 28: 19
		final index for 1: 3
		final index for 19: 12

        * 
        */
       
    }

}
