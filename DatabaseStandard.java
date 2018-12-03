import java.util.HashMap;
import java.math.BigInteger;
import java.util.Set;

public class DatabaseStandard implements DatabaseInterface {
					// k.      v.    
	private HashMap <String, String> map;
	private int customSize=16;
//*************************************constructors*******************************************************
	public DatabaseStandard (){
		map = new HashMap <>();
		customSize=16;
	}
	public DatabaseStandard	(int size) {
		if (size<0){
			throw new IllegalArgumentException ();
		}
		map= new HashMap <> (size);
		customSize=size;
	}
//************************************constructor end****************************************************	
	
	public String save(String plainPassword, String encryptedPassword){
		if ((plainPassword==null) || (encryptedPassword==null)){
			throw new IllegalArgumentException ();

		}
		return map.put (encryptedPassword,plainPassword);
	}
	 // Stores plainPassword and corresponding encryptedPassword in a map.
	 // if there was a value associated with this key, it is replaced, 
	 // and previous value returned; otherwise, null is returned
	 // The key is the encryptedPassword the value is the plainPassword

	public String decrypt(String encryptedPassword){
		if (encryptedPassword==null){
			throw new IllegalArgumentException ();
		}
		String returnVal=map.get (encryptedPassword);
		if (returnVal==null){
			return "";
		}
		return returnVal;
	}
	// returns plain password corresponding to encrypted password
	
    public int size(){
    	return map.size ();
    }
    // returns the number of password pairs stored in the database
	
    public void printStatistics(){
    	System.out.println ("***DatabaseStandard Statistics***"+"\nSize is "+size()+" passwords");
    	
    	System.out.println ("Initial Number of Indexes when Created : "+customSize);
    	
    	System.out.println ("*** End DatabaseStandard Statistics ***");
    } // print statistics based on type of Database

    /*returns the mod 256 of the sha1....h=h2(h1(sha1)) h1=sha1mod256 */
    // protected int h1 (String sha1){
    // 	BigInteger bi = new BigInteger(sha1, 16);
    // 	BigInteger bi2 =bi.mod (new BigInteger ("256"));
    // 	return bi2.intValue ();

    // }




















}