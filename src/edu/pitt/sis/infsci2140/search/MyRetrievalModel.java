package edu.pitt.sis.infsci2140.search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.pitt.sis.infsci2140.analysis.StopwordsRemover;
import edu.pitt.sis.infsci2140.analysis.TextNormalizer;
import edu.pitt.sis.infsci2140.analysis.TextTokenizer;
import edu.pitt.sis.infsci2140.index.MyIndexReader;

public class MyRetrievalModel {

	private static final double u = 500;

	protected MyIndexReader ixreader;
	protected long collectionFrq;
	protected long CollectionLength;
	protected StopwordsRemover stoprmv;

	public MyRetrievalModel() throws FileNotFoundException {
		FileInputStream instream_stopwords = new FileInputStream("stopword.txt");
		stoprmv = new StopwordsRemover(instream_stopwords);
		// you should implement this method
	}

	public MyRetrievalModel setIndex(MyIndexReader ixreader) {
		this.ixreader = ixreader;
		return this;
	}

	/**
	 * Search for the topic information. The returned results should be ranked
	 * by the score (from the most relevant to the least). max_return specifies
	 * the maximum number of results to be returned.
	 * 
	 * @param topic
	 *            The topic information to be searched for.
	 * @param max_return
	 *            The maximum number of returned document
	 * @return
	 * @throws IOException
	 */
	public double ProbabilitySmothing(String docno) throws IOException {
		int len = ixreader.DocLength(docno);
		double p = u * ((double) collectionFrq / CollectionLength) / (len + u);
		return p;
	}

	public double ProbabilityNormal(int docFrq, int documentsWordsT2) {
		double p = (docFrq + u * ((double) collectionFrq / CollectionLength))
				/ (documentsWordsT2 + u);
		return p;
	}

	public double ProbabilityNotInPrevious(Long collectinFq, String docno) {
		double p = u * ((double) collectinFq / CollectionLength)
				/ ((ixreader.DocLength(docno) + u));
		return p;

	}

	public List<SearchResult> search(Topic topic, int max_return)
			throws IOException {
		List<SearchResult> resultList = new ArrayList<SearchResult>();
		for (Entry<String, Integer> entry : ixreader.getDoc().entrySet()) {
			SearchResult searchResult = new SearchResult();
			searchResult.setDocno(entry.getKey());
			searchResult.setScore(1d);
			searchResult
					.setDocid(ixreader.getDocid(" " + entry.getKey() + " "));
			resultList.add(searchResult);
		}
		List<Long> storeCollectionFreq = new ArrayList<Long>();
		TextTokenizer tt;
		for (Entry<String, Integer> it : ixreader.getDictionary().entrySet()) {
			CollectionLength += it.getValue();
		}
		char[] words = topic.getTitle().toCharArray();
		//System.out.print(words[1]);
		tt = new TextTokenizer(words);
		boolean flag[] = new boolean[resultList.size()];
		while ((words = tt.nextWord()) != null) {
			words = TextNormalizer.normalize(words);
			if (!stoprmv.isStopword(words)) {
				String word = new String(words);
				int[][] posting = ixreader.getPostingList(word);
				List<SearchResult> tempRate = new ArrayList<SearchResult>();
				collectionFrq = (int) ixreader.CollectionFreq(word);
				int docFrq = ixreader.DocFreq(word);
				if (docFrq > 0) {//to judge if the doc has the word in topic , if the answer is yes, I can continue to munipulate it
					for (int ix = 0; ix < posting.length; ix++) {//for one doc,get the docno and doclength
						int freq = posting[ix][1];
						String docno = ixreader.getDocno(posting[ix][0]);//get the docno
						int docLength = ixreader.DocLength(docno.trim());//get the doclength,I add a method in MyindexReader to calculate the doclength
						tempRate.add(new SearchResult(posting[ix][0], docno,
								ProbabilityNormal(freq, docLength)));
					}
					/*iterator the resultlist,to judge whether or not the the current word have the same doc with the previous one,
					if the answer is yes,use the ProbabilityNormal method,if the answer is no,use the ProbabilitySmoothing method*/
					Iterator<SearchResult> it = resultList.iterator();
					int index = 0;
					while (it.hasNext()) {
						SearchResult searchResult = it.next();
						SearchResult tmpSearchResult = null;
						for (SearchResult sr : tempRate) {
							if (sr.docid == searchResult.docid) {
								tmpSearchResult = sr;
								break;
							}
						}
						if (null == tmpSearchResult && flag[index]) {
							searchResult.setScore(searchResult.score
									* ProbabilitySmothing(searchResult.docno));
						} else if (null != tmpSearchResult) {
							searchResult.setScore(searchResult.score
									* tmpSearchResult.score);
						}
					}
					//iterator the current tempRate, to judge whether or not the previous word have the same doc with the current doc
					Iterator<SearchResult> it2 = tempRate.iterator();
					while (it2.hasNext()) {
						index = 0;
						SearchResult searchResult = it2.next();
						SearchResult tmpSearchResult = null;
						for (SearchResult sr : resultList) {
							index++;
							if (sr.docid == searchResult.docid) {
								tmpSearchResult = sr;
								break;
							}
						}
						flag[index] = true;
						Iterator<Long> itCF = storeCollectionFreq.iterator();
						while (itCF.hasNext()) {
							Long collectinFq = (Long) itCF.next();
							tmpSearchResult.setScore(searchResult.score
									* ProbabilityNotInPrevious(collectinFq,
											searchResult.docno));
						}
					}
					storeCollectionFreq.add(collectionFrq);
				} else {
					continue;
				}
			}
		}//sort the resultlist
		List<SearchResult> tmpList = new ArrayList<SearchResult>();
		for (SearchResult sr : resultList) {
			if (Double.compare(sr.score, 1d) == 0) {
				tmpList.add(sr);
			}
		}
		resultList.removeAll(tmpList);
		Collections.sort(resultList, new Comparator<SearchResult>() {

			@Override
			public int compare(SearchResult o1, SearchResult o2) {
				return -Double.compare(o1.score, o2.score);
			}
		});
		return resultList.size() > max_return ? resultList.subList(0,
				max_return) : resultList;
	}
}