public class DatabaseMine implements DatabaseInterface {
	private int N; // this is a prime number that gives the number of addresses
	private Entry [] map;
	private int n=0;
	private int probes=0;
	private int displacements=0;
	
	// these constructors must create your hash tables with enough positions N
	// to hold the entries you will insert; you may experiment with primes N
	public DatabaseMine() {
		N=51199;//we pick 51199 because we know the default amount we will have is 51194 combinations so its a good spread(we have a couple null values) and we dont have to rehash(which is costly)
		map=new Entry [N];
		//I populate the initial array with string null values because i needed it for the toString
		for (int i=0;i<map.length;i++){
			map[i]=new Entry ("null","null");
		}
	} 
	// here you pick suitable default N
	public DatabaseMine(int N) {
		if (N<0){
			throw new IllegalArgumentException ();
		}
		this.N=N;
		map= new Entry [N];
		//I populate the initial array with string null values because i needed it for the toString
		//ex. [("null","null"),("null","null"),("null","null")]
		for (int i=0;i<map.length;i++){
			map[i]=new Entry ("null","null");
		}
	} // here the N is given by the user
	
	//hash function that takes the key and returns the HashKey of string
	public int hashFunction(String key) {
		if (key==null){
			throw new IllegalArgumentException ();
		}
		int address=key.hashCode()%N;
		return (address>=0)?address:(address+N);
	}
	
	// Stores plainPassword and corresponding encryptedPassword in a map.
	// if there was a value associated with this key, it is replaced, 
	// and previous value returned; otherwise, null is returned
	// The key is the encryptedPassword the value is the plainPassword
	public String save(String plainPassword, String encryptedPassword){
		if (plainPassword==null || encryptedPassword==null){
			throw new IllegalArgumentException ();
		}
		int hash=hashFunction (encryptedPassword);
		int probeCount=0;//keeps count of number of probes
		//exits while loop if we went through all n elems and the database is full so we dont have anywhere to place and return null;
		while ((probeCount)<N){
			int index=(hash+probeCount)%N;
			//we have a found a null value and know its not a duplicate
			if (map[index].getValue ().equals("null")&&map[index].getKey ().equals ("null")){
				map [index]=new Entry (encryptedPassword,plainPassword);
				if (probeCount>0){//if count is bigger 0 we know that this element is being probed to another location so its a displacment
					displacements ++;
				}
				probes = probes +probeCount;
				n++;
				return null;
			}
			//this if statement takes care of adding duplicates
			//swaps old plain password with new(done due to description of assignment and java Hash map)
			else if ((map[index].getValue ().equals (plainPassword))){
				String tmp=map[index].getValue ();
				map[index].setValue (plainPassword);
				probes = probes +probeCount;
				return tmp;
			}
			probeCount++;
		}
		probes = probes +probeCount;
		//we went through all elements and didnt find a empty spot because hash map if full so return null
		return null;
	}
	

	
	// returns plain password corresponding to encrypted password
	public String decrypt(String encryptedPassword){
		if (encryptedPassword==null){
			throw new IllegalArgumentException ();
		}
		int hash = hashFunction (encryptedPassword);
		int count=0;
		while (count<N){
			int index= (hash + count) % N;
			//if the current elem in the map is null than u know the elem is not there so u return empty string
			if (map[index].getValue ().equals("null")&&map[index].getKey ().equals ("null")){
				return "";
			}
			//enters this if statement, when we are going through the elems and reach the key and the encryptedPassword equalls the one we are looking for 
			//returns the value corresponding with the same encrypted password
			else if (map[index].getKey ().equals (encryptedPassword)){
				return map[index].getValue ();
			}
			count++;
		}
		//if this statement is printed than we have traversed every elem and we know for a fact there is no elem in 
		System.out.println ("went through all items and did not find");
		return "";
	}
	
	// returns the number of password pairs stored in the database
	public int size(){
    	return n;
    }
    
    //prints hash map statisitics
    // printed here: size, number of indexes, load factor, average number of probes
	// and average number of displacements
    public void printStatistics(){
    	System.out.println ("*** DatabaseMine Statistics ***\nSize is "+ size () +
    		" passwords\nLoad Factor is "+getAlpha()
    		+"\nAverage Number of Probes is "+getAvgNumberProbes ()+"\nNumber of displacements (due to collisions) "+displacements+"\n*** End DatabaseMine Statistics ***");
    }
    //returns Database representation
    public String toString (){
    	String s="";
    	for (int i=0;i<map.length;i++){
    		//(string == null) ? "" : string)
    		String value=map[i].getValue();
    		String key=map[i].getKey();
    		s=s+"("+value+","+key+")\n";
    	}
    	return s;
    }
    //returns Alpha 
  	public float getAlpha (){
  		return (float)n/N;
  	}
  	public float getAvgNumberProbes (){
  		return (float)probes/N;
  	}


}