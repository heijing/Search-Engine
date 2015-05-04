package edu.pitt.sis.infsci2140.analysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;






public class StopwordsRemover  {
	
	  
	  /** The hash set containing the list of stopwords */
	Set<String> m_Words = new HashSet<String>();
	  String line=null;
	  /*public static void main(String[] args) throws FileNotFoundException{
		  try {
				FileInputStream fis = new FileInputStream("/Users/danyangli/Desktop/stop_words.txt");
				StopwordsRemover rm = new StopwordsRemover(fis);
				char[] word={'y','o','u','p'};
				rm.isStopword(word);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	  /**
	   * initializes the stopwords (based on Rainbow).
	 * @return 
	 * @return 
	   */
	  public StopwordsRemover(  FileInputStream instream ) throws FileNotFoundException {
		  //initialzie fileinputstream and bufferedreader 
		    BufferedReader reader = null;    
	        reader = new BufferedReader(new InputStreamReader(instream));
     
        //Reading File line by line using BufferedReader
        //You can get next line using reader.readLine() method.
        
         
		
		try {

		   line = reader.readLine();
			while(line != null){
				m_Words.add(line);
				line = reader.readLine();
			}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	public boolean isStopword( char[] word ) {
		String s = String.valueOf(word);
				if(m_Words.contains(s)){
					//System.out.println(s+",true");
					return true;
				}
			
		
		// return true if the input word is a stopword, or false if not
		//System.out.println(s+",false");
		return false;
	}
}



