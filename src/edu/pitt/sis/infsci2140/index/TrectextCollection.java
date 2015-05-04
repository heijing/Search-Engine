package edu.pitt.sis.infsci2140.index;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TrectextCollection implements DocumentCollection {

	BufferedReader reader;
	/*public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream(
				"//Users//danyangli//Desktop//IR//Archive//docset//docset.trectext");
		TrectextCollection tex = new TrectextCollection(fis);
		Map<String, Object> doc;
		
	
		while( ( doc=tex.nextDocument() ) != null ) { // iteratively reading each document from the collection
			
			String docno = (String) doc.get("DOCNO"); // load docno of the document and output
			char[] content = (char[]) doc.get("CONTENT"); // document content
			
		}	

	}*/

	

	// YOU SHOULD IMPLEMENT THIS METHOD
	public TrectextCollection(FileInputStream instream) throws IOException {

		 reader = new BufferedReader(new InputStreamReader(
				instream));

	}

	// This constructor should take an inputstream of the collection file as the
	// input parameter.

	// YOU SHOULD IMPLEMENT THIS METHOD

	public Map<String, Object> nextDocument() throws IOException {
		Map<String, Object> doc = new HashMap<String, Object>();

		// Reading File line by line using BufferedReader
		// You can get next line using reader.readLine() method.
		String line = reader.readLine();
		int flag = 1;

		while ((line != null)) {
			if (line.contains("</DOC>")) {
				return doc;
			} else if (line.contains("<DOCNO>")) {
				//System.out.println(line);
				line = line.replace("<DOCNO>", "");
				line = line.replace("</DOCNO>", "");
				doc.put("DOCNO", line);
			} else if (line.contains("<TEXT>")) {
				flag = 1;
				//System.out.println(line);

				line = reader.readLine();
				String Templine = "";

				while (flag == 1) {

					//System.out.println(line);
					Templine += line;
					line = reader.readLine();
					
					

					if (line.contains("</TEXT>")) {
						//System.out.println(line);
						flag = 0;
						doc.put("CONTENT", Templine.toCharArray());
					}
				}

			}
			line = reader.readLine();

		}

		return null;

		// Read the definition of this method from
		// edu.pitt.sis.infsci2140.index.DocumentCollection interface
		// and follow the assignment instructions to implement this method.
		// return null;
	}

	

}

