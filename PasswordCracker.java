import java.util.ArrayList;
import java.time.Year;
import java.io.UnsupportedEncodingException;

/*Things to clean up if i get oppurtunity :
	-do we really need to be adding into the new arrayList? if not just initiazlize the array at the end
	-add if statment for when if counter >1 to reduce another useless calculation to speed up process
*/

public class PasswordCracker {
	private ArrayList <String> passwords;
	private DatabaseInterface database;
	
	public void createDatabase (ArrayList <String>commonPasswords, DatabaseInterface database){
		passwords=new ArrayList ();//we initialize our ArrayList (reason for making our own arrayList was to see how many duplicates i was generating because hashMap doesnt add duplicates)
		/*for loop goes through commonPasswords and augments the list by the following rules......
			augment this list by applying the following rules:
			1. Capitalize the first letter of each word starting with a letter, e.g. dragon becomes Dragon
			2. Add the current year to the word, e.g. dragon becomes dragon2018
			3. Use @ instead of a, e.g. dragon becomes dr@gon
			4. Use 3 instead of e, e.g. baseball becomes bas3ball
			5. Use 1 instead of i, e.g. michael becomes m1chael
		While traversing through each element in commonPasswords it adds it to the data base 
		*/
		for (int i =0; i <commonPasswords.size (); i++){//we go through all the all elems in arraylist given in argument
			String pass=commonPasswords.get (i);//contains the string rep of the password
			String passHash;
			try {
				passwords.add (pass);//add first combination(the password itself into) into our ArrayList
				passHash=Sha1.hash (pass);//stores the Sha1 of the password
				database.save (pass,passHash);//saving the pasword and encrypted password into Database
				if (!Character.isDigit (pass.charAt (0))){//if the first element is part of the alphabet
					String str=new String (pass.substring(0, 1).toUpperCase() + pass.substring(1));//capitalizes the first char
					passwords.add (str);//adding to array list
					database.save (str,Sha1.hash (str));//adding to database
					
				}
				if (pass.contains ("a")){//we check if an "a" exists in the password (if true goes into statement) else next statement
					/*in this if statement we do all permuatations of the password that involve "a" and swap it with "@"*/
					String string=pass;//we make a new string of the password
					String str=pass.replace ('a','@');//first combination is swap all 'a' with '@'
					passwords.add (str);//add to arraylist
					database.save (str,Sha1.hash (str));//add to database
					int counter = string.length() - string.replaceAll("a","").length();//checks how many times 'a' occurs in the password
					//to be more efficient i can put this into an if statment and only do this if counter is >1 (However i dont have the time atm)
					//for loop goes through and swaps each individual 'a' with '@'
					for (int j = 0; j<counter-1;j++){//reason why we do counter -1 is that we already added the string that contains all 'a' to '@' so we reduce the amount of repatition to speed up algorithm
						int index= string.indexOf ('a'); //we find the index of the value 'a' 
						string=PasswordCracker.swapElemWith (string,'@',index);//swap the 'a' in the specified index in the string with '@'
						passwords.add (string);//add that permutation into our arrraylist
						database.save (string, Sha1.hash (string));//add into database
					}
					String s=str;//we make new string that contains all the '@' instead 'a'
					//for loop goes through and swaps each individual '@' with 'a'
					for (int j=0; j<counter-1;j ++){//reason why we do counter -1 is that we already added the string that contains all 'a' to '@' so we reduce the amount of repatition to speed up algorithm
						int index =str.indexOf ('@');//we find the index of the value '@' in string
						str=PasswordCracker.swapElemWith (str,'a',index);//swap '@' in the specified index
						passwords.add (str);// add to arrayList
						database.save (str,Sha1.hash (str));//add into databse
					}
				}
				//same situation that occured in the if statement with 'a' happens here if the string contains 'e'
				//this statement swaps 'e' with '3'
				if (pass.contains ("e")){
					String string=pass;
					String str=pass.replace ('e','3');
					passwords.add (str);
					database.save (str,Sha1.hash (str));
					int counter = string.length() - string.replaceAll("e","").length();
					for (int j = 0; j<counter-1;j++){
						int index= string.indexOf ('e');
						string=PasswordCracker.swapElemWith (string,'3',index);
						passwords.add (string);
						database.save (string, Sha1.hash (string));

					}
					String s=str;
					for (int j=0; j<counter-1;j ++){
						int index =str.indexOf ('3');
						str=PasswordCracker.swapElemWith (str,'e',index);
						passwords.add (str);
						database.save (str,Sha1.hash (str));
					}
				}
				//same situation that occured in the if statement with 'a' happens here if the string contains 'i'
				//this statement swaps 'i' with '1'
				if (pass.contains ("i")){
					String string=pass;
					String str=pass.replace ('i','1');
					passwords.add (str);
					database.save (str,Sha1.hash (str));
					int counter = string.length() - string.replaceAll("i","").length();
					for (int j = 0; j<counter-1;j++){
						int index= string.indexOf ('i');
						string=PasswordCracker.swapElemWith (string,'1',index);
						passwords.add (string);
						database.save (string, Sha1.hash (string));
					}
					String s=str;
					for (int j=0; j<counter-1;j ++){
						int index =str.indexOf ('1');
						str=PasswordCracker.swapElemWith (str,'i',index);
						passwords.add (str);
						database.save (str,Sha1.hash (str));
					}

				}
				//adds the year at the end of the string 
				String str=pass+Year.now().getValue();
				passwords.add (str);//add to our arrayList
				database.save (str,Sha1.hash (str));// add to database

				/*this step does all rules into one */
				str=new String (str.substring(0, 1).toUpperCase() + str.substring(1));//captilize first char
				str=str.replace('a','@');//replace all 'a' with '@'
				str=str.replace ('i','1');//replace all 'i' with '1'
				str=str.replace ('e','3');//replace all 'e' with '3'
				passwords.add (str);//add to arraylist
				database.save (str,Sha1.hash (str));//add to database


			}
			catch(UnsupportedEncodingException e){
				System.out.println ("UnsupportedEncodingException");
				return;
			}
		}
		this.database=database;//initialize our database stored in class with the populated database
	}
	/*helper method designed to swap the char in the string at the given index*/
	private static String swapElemWith (String string ,char swap, int index){
		if (string==null){
			throw new IllegalArgumentException ();
		}
		if (index<0||index>string.length ()){
			throw new IllegalArgumentException ();
		}
		char [] charAr=string.toCharArray ();
		charAr[index]=swap;
		return new String (charAr);
	}
	/*	returns value password that was stored in database if it existed
	*/
	public String crackPassword (String encryptedPassword, DatabaseInterface database){
		database=this.database;
		return (database.decrypt (encryptedPassword));
	}
	/*	returns database that was initialized if createDatabase || crack passWord was called
		if null than database hasnt been initialized
	*/
	public DatabaseInterface getDatabase (){return database;}
	/*
	returns Arraylist that was initialzed if createdDatabase was called
	if null than arraylist hasnt been initialized which means create database || crack passWord wasnt called 
	*/
	public ArrayList getCommonPasswords (){return passwords;}
}

                                          
























