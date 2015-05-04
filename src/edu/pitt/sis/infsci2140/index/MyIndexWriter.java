package edu.pitt.sis.infsci2140.index;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.pitt.sis.infsci2140.analysis.StopwordsRemover;
import edu.pitt.sis.infsci2140.analysis.TextNormalizer;
import edu.pitt.sis.infsci2140.analysis.TextTokenizer;

public class MyIndexWriter {

	protected File dir;

	HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Integer>> postingInfo = new HashMap<String, HashMap<String, Integer>>();
	HashMap<String, Integer> doc = new HashMap<String, Integer>();

	// for the postinginfo,I put the first string as the term and second string
	// in hashmap as docno // the

	public MyIndexWriter(File dir) throws IOException {
		this.dir = dir;
	}

	public MyIndexWriter(String path_dir) throws IOException {
		this.dir = new File(path_dir);
		if (!this.dir.exists()) {
			this.dir.mkdir();
		}
	}

	/**
	 * This method build index for each document. NOTE THAT: in your
	 * implementation of the index, you should transform your string docnos into
	 * non-negative integer docids !!! In MyIndexReader, you should be able to
	 * request the integer docid for docnos.
	 * 
	 * @param docno
	 *            Docno
	 * @param tokenizer
	 *            A tokenizer that iteratively gives out each token in the
	 *            document.
	 * @throws IOException
	 */

	public void addByOne(String docId, String term) {
		HashMap<String, Integer> postingInfoForOneTerm = postingInfo.get(term);
		if (postingInfoForOneTerm.containsKey(docId)) {
			int freq = postingInfoForOneTerm.get(docId);
			postingInfoForOneTerm.put(docId, freq + 1);
		} else {
			postingInfoForOneTerm.put(docId, 1);
			postingInfo.put(term, postingInfoForOneTerm);
		}
	}

	public void index(String docno, TextTokenizer tokenizer,
			StopwordsRemover stoprmv) throws IOException {
		char[] term;

		int docWord = 0;
		while ((term = tokenizer.nextWord()) != null) {
			if (!stoprmv.isStopword(term)) {
				docWord++;
				term = TextNormalizer.normalize(term);
				String termString = new String(term);
				// normalize each word
				// System.out.println();
				if (dictionary.containsKey(termString)) {
					int freq = dictionary.get(termString);
					dictionary.put(termString, freq + 1);
					addByOne(docno, termString);
				} else {
					// adding a new term
					dictionary.put(termString, 1);
					HashMap<String, Integer> newPostingInfo = new HashMap<String, Integer>();
					newPostingInfo.put(docno, 1);
					postingInfo.put(termString, newPostingInfo);
				}
			}
			if (!doc.containsKey(docno)) {
				doc.put(docno.trim(), docWord);
			} else {
				doc.put(docno.trim(), doc.get(docno) + docWord);
			}
		}
	}

	// you should implement this method to build index for each document

	/**
	 * Close the index writer, and you should output all the buffered content
	 * (if any).
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		// you should implement this method if necessary

		/* My comments */
		// you need to implement this method to export all the dictionary and
		// posting info a file and store is persistently,
		// the MyIndexReader is going to read the same file to get the whole
		// dictionary and posting info.

		// the file path is the path that is passed through conctructor. and the
		// File has been created too.

		FileWriter writer2 = new FileWriter(dir.getAbsolutePath() + "/Doc.txt");
		Iterator<Entry<String, Integer>> it2 = doc.entrySet().iterator();

		while (it2.hasNext()) {
			Entry<String, Integer> next = it2.next();
			writer2.write(next.getKey() + "," + next.getValue() + "\n");
			writer2.flush();
		}
		writer2.close();
		FileWriter writer = new FileWriter(dir.getAbsolutePath()
				+ "/DictionaryTermFile.txt");
		Iterator<Entry<String, Integer>> it = dictionary.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, Integer> next = it.next();
			writer.write(next.getKey() + "," + next.getValue() + "\n");
			writer.flush();
		}
		writer.close();

		FileWriter writer1 = new FileWriter(dir.getAbsolutePath()
				+ "/Postingfile.txt");
		Set<Map.Entry<String, HashMap<String, Integer>>> postingSet = postingInfo
				.entrySet();
		Iterator<Map.Entry<String, HashMap<String, Integer>>> postingIt = postingSet
				.iterator();
		while (postingIt.hasNext()) {
			Map.Entry<String, HashMap<String, Integer>> postingNext = postingIt
					.next();
			String postingTerm = postingNext.getKey();
			HashMap<String, Integer> postingDoc = postingNext.getValue();
			Set<Map.Entry<String, Integer>> postingDocSet = postingDoc
					.entrySet();
			Iterator<Map.Entry<String, Integer>> postingDocIt = postingDocSet
					.iterator();
			while (postingDocIt.hasNext()) {
				Map.Entry<String, Integer> postingDocNext = postingDocIt.next();
				String postingDocno = postingDocNext.getKey();
				Integer postingDocfreq = postingDocNext.getValue();
				writer1.write(postingTerm + "," + postingDocno + ","
						+ postingDocfreq + "\n");
				writer1.flush();
			}
		}
		writer1.close();
	}

	/*
	 * ObjectOutputStream dictionaryStream = new ObjectOutputStream( new
	 * FileOutputStream(dir.getAbsolutePath() + "/DictionaryTermFile.txt"));
	 * dictionaryStream.writeObject(dictionary); dictionaryStream.flush();
	 * dictionaryStream.close();
	 * 
	 * ObjectOutputStream postringFileStream = new ObjectOutputStream( new
	 * FileOutputStream(dir.getAbsolutePath() + "/PostingFile.txt"));
	 * postringFileStream.writeObject(postingInfo); postringFileStream.flush();
	 * postringFileStream.close();
	 */
}
