package edu.pitt.sis.infsci2140.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A class for reading your index.
 */
public class MyIndexReader {

	protected File dir;
	// we are going to get the dictionary and posting file from files
	private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	private HashMap<String, HashMap<String, Integer>> postingInfo = new HashMap<String, HashMap<String, Integer>>();
	private HashMap<String, Integer> doc = new HashMap<String, Integer>();

	public HashMap<String, Integer> getDoc() {
		return doc;
	}

	public void setDoc(HashMap<String, Integer> doc) {
		this.doc = doc;
	}

	public HashMap<String, Integer> getDictionary() {
		return dictionary;
	}

	public void setDictionary(HashMap<String, Integer> dictionary) {
		this.dictionary = dictionary;
	}

	public HashMap<String, HashMap<String, Integer>> getPostingInfo() {
		return postingInfo;
	}

	public void setPostingInfo(
			HashMap<String, HashMap<String, Integer>> postingInfo) {
		this.postingInfo = postingInfo;
	}

	public MyIndexReader(File dir) throws IOException, ClassNotFoundException {
		this.dir = dir;
		readFileDictionary();
		readFilePostingList();
		readFileDoc();
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		MyIndexReader indexReader = new MyIndexReader("output");
		indexReader.readFileDictionary();
		indexReader.readFilePostingList();
	}

	private void readFileDictionary() throws FileNotFoundException,
			IOException, ClassNotFoundException {
		FileReader fileReader = new FileReader(dir.getAbsolutePath()
				+ "/DictionaryTermFile.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] str = line.split(",");
			int count = Integer.parseInt(str[1]);
			String termString = str[0];
			dictionary.put(termString, count);
		}
		reader.close();
		fileReader.close();
		/*
		 * ObjectInputStream dictionaryReadStream = new ObjectInputStream(new
		 * FileInputStream(dir.getPath()+"DictionaryTermFile.txt")); dictionary
		 * = (HashMap<String,Integer>)dictionaryReadStream.readObject();
		 * dictionaryReadStream.close(); ObjectInputStream postingFileStream =
		 * new ObjectInputStream(new
		 * FileInputStream(dir.getPath()+"PostingFile.txt")); HashMap<String,
		 * HashMap<String, Integer>> postingFileDictionary; postingInfo =
		 * ((HashMap<String, HashMap<String,
		 * Integer>>)postingFileDictionary.readObject();
		 * postingFileStream.close();
		 */
	}

	private void readFilePostingList() throws FileNotFoundException,
			IOException, ClassNotFoundException {
		HashMap<String, Integer> postingInfoForOneTerm = new HashMap<String, Integer>();

		FileReader fileReader = new FileReader(dir.getAbsolutePath()
				+ "/PostingFile.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String line = null;
		while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			String[] str = line.split(",");
			int count = Integer.parseInt(new String(str[2]));
			String Docid = new String(str[1]);
			String termString = new String(str[0]);
			if (!postingInfo.containsKey(termString)) {
				postingInfoForOneTerm = new HashMap<String, Integer>();
				postingInfoForOneTerm.put(Docid, count);
				postingInfo.put(termString, postingInfoForOneTerm);
			} else {
				postingInfoForOneTerm = postingInfo.get(termString);
				postingInfoForOneTerm.put(Docid, count);
			}
		}
		reader.close();
		fileReader.close();
	}

	private void readFileDoc() throws FileNotFoundException, IOException,
			ClassNotFoundException {

		FileReader fileReader = new FileReader(dir.getAbsolutePath()
				+ "/Doc.txt");
		BufferedReader reader = new BufferedReader(fileReader);
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] doclength = line.split(",");
			doc.put(doclength[0], Integer.parseInt(doclength[1]));
		}
		reader.close();
		fileReader.close();
	}

	public MyIndexReader(String path_dir) throws IOException,
			ClassNotFoundException {
		this(new File(path_dir));
	}

	/**
	 * Get the (non-negative) integer docid for the requested docno. If -1
	 * returned, it indicates the requested docno does not exist in the index.
	 * 
	 * @param docno
	 * @return
	 */
	public int getDocid(String docno) {
		// string to int

		// you should implement this method.

		// search the postingInfo

		Set<Entry<String, HashMap<String, Integer>>> entries = postingInfo
				.entrySet();

		for (Entry<String, HashMap<String, Integer>> eachEntry : entries) {
			String Docno = docno.replaceAll("-", "");
			String Docid;
			if (Docno.contains("WSJ")) {
				Docid = Docno.substring(5, 14);
			} else {
				Docid = Docno.substring(8);
			}
			if (eachEntry.getValue().containsKey(docno)) {
				return Integer.parseInt(Docid);
			}
		}
		return -1;
	}


	/**
	 * Retrive the docno for the integer docid.
	 * 
	 * @param docid
	 * @return
	 */
	public String getDocno(int docid) {
		// int to string
		// you should implement this method.

		Set<Entry<String, HashMap<String, Integer>>> entries = postingInfo
				.entrySet();
		for (Entry<String, HashMap<String, Integer>> eachEntry : entries) {
			Set<Entry<String, Integer>> post = eachEntry.getValue().entrySet();
			for (Entry<String, Integer> eachPost : post) {
				String Docno = eachPost.getKey().replaceAll("-", "");
				String Docid;
				if (Docno.contains("WSJ")) {
					Docid = Docno.substring(5, 14);
				} else {
					Docid = Docno.substring(8);
				}
				if (Integer.parseInt(Docid) == docid) {
					return eachPost.getKey();
				}
			}
		}
		return null;
	}

	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and
	 * corresponding frequencies of the term, such as:
	 * 
	 * [docid] [freq] 1 3 5 7 9 1 13 9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each
	 * document, and the second dimension records the docid and frequency.
	 * 
	 * For example: array[0][0] records the docid of the first document the
	 * token appears. array[0][1] records the frequency of the token in the
	 * documents with docid = array[0][0] ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from
	 * the smallest to the largest.
	 * 
	 * @param token
	 * @return
	 */
	public int[][] getPostingList(String token) throws IOException {

		int[][] array = null;
		if (postingInfo.containsKey(token)) {
			HashMap<String, Integer> posting = postingInfo.get(token);
			List<Map.Entry<String, Integer>> postingList = new ArrayList<Map.Entry<String, Integer>>(
					posting.entrySet());
			// sorting
			Collections.sort(postingList,
					new Comparator<Map.Entry<String, Integer>>() {

						@Override
						public int compare(Map.Entry<String, Integer> o1,
								Map.Entry<String, Integer> o2) {
							return getDocid(o1.getKey())
									- getDocid(o2.getKey());
						}
					});
			int size = postingList.size();
			array = new int[size][2];
			int i = 0;
			for (Entry<String, Integer> eachEntry : postingList) {
				array[i][0] = getDocid(eachEntry.getKey());
				array[i][1] = eachEntry.getValue();
				i++;
			}
			return array;
		}
		return array;
	}

	/**
	 * Return the number of documents that contains the token.
	 * 
	 * @param token
	 * @return
	 */
	public int DocFreq(String token) throws IOException {
		// you should implement this method.
		if (postingInfo.containsKey(token)) {
			HashMap<String, Integer> posting = postingInfo.get(token);
			Set<Entry<String, Integer>> entries = posting.entrySet();
			return entries.size();
		} else
			return 0;
	}
	public int DocLength(String docno){
		return doc.get(docno.trim());
	}
	public int CollectionLength(String term){
		return dictionary.get(term);
	}

	/**
	 * Return the total number of times the token appears in the collection.
	 * 
	 * @param token
	 * @return
	 */
	public long CollectionFreq(String token) throws IOException {
		if (dictionary.containsKey(token)) {
			return dictionary.get(token);
		} else
			return 0;
	}

	public void close() throws IOException {
		// you should implement this method when necessary
	}

}
