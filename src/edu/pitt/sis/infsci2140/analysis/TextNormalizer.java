package edu.pitt.sis.infsci2140.analysis;

public class TextNormalizer {
	/*public static void main(String[] args) throws IOException {
		char[] texts = { 'A', 'P', 'P', 'l', 'e', ' ', 'l', 'i', 'k', 'e' ,' ' ,'a', 'p', 'p', 'l', 'e', ' ', 'l', 'i', 'k', 'e'};

		TextNormalizer t = new TextNormalizer();
		t.normalize(texts);
	}*/
	// YOU MUST IMPLEMENT THIS METHOD
	
	
	public static char[] normalize( char[] chars ) {
		char letters[] = new char[chars.length];
		  for(int i=0;i<chars.length;i++){
			
			  char letter = chars[i];
			  if(letter>='A' && letter<='Z'){

				  letter = (char) (letter+32);
				  letters[i] = letter;

			  }
			  else{
				  letters[i] = letter;

			  }
		  }// return the normalized version of the word characters (replacing all uppercase characters into the corresponding lowercase characters)
			//System.out.print(letters);

		  return letters;
		  
	}

	
}
