package edu.pitt.sis.infsci2140.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that stores topic information.
 */
public class Topic {

	private String title;
	private String number;
	private List<String> qs;//store the words in the double quote

	public Topic() {
	}

	public Topic(String number, String title) {
		this.number = number;
		this.title = title;
	}

	public String topicId() {
		      return number;
	  
		// you should implement this method
		
	}
   
	     /*public static void main(String[] args) {
		URL inputStream = Topic.class.getClassLoader()
				.getResource("topics.txt");
		for (Topic t : parse(new File(inputStream.getPath()))) {
			System.out.println(t.getTitle() + "---" + t.getNumber());
			System.out.println();
		}
	}*/
     // write to a single file
	public static void writeToFile(List<Topic> topicList) {
		FileWriter out = null;
		BufferedWriter writer = null;
		try {
			out = new FileWriter(new File("result.txt"));
			writer = new BufferedWriter(out);
			for (Topic t : topicList) {
				writer.write(t.getNumber() + "\n" + t.getTitle()+"\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Parse a list of TREC topics from the provided f.
	 * 
	 * @param f
	 * @return
	 */
	public static List<Topic> parse(File f) {
		Pattern numberPattern = Pattern.compile("<num> Number:\\s*(\\d+)");
		Pattern titlePattern = Pattern.compile("<title>(.*)");
		FileInputStream instream = null;
		BufferedReader reader = null;
		List<Topic> topiclist = null;
		try {
			instream = new FileInputStream(f);
			reader = new BufferedReader(new InputStreamReader(instream));
			topiclist = new ArrayList<Topic>();
			// Reading File line by line using BufferedReader
			String line = null;
			Topic topic = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("<top>")) {
					topic = new Topic();
					continue;
				}
				if (line.startsWith("</top>")) {
					topiclist.add(topic);
					continue;
				}
				Matcher matcher = numberPattern.matcher(line);
				if (matcher.find()) {
					if (topic != null)
						topic.setNumber(matcher.group(1));
					continue;
				}
				matcher = titlePattern.matcher(line);
				if (matcher.find()) {
					if (topic != null) {
						topic.setTitle(matcher.group(1));
					}
					continue;
				}
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Exist!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOError Occurred!");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (topiclist != null)
			writeToFile(topiclist);
		return topiclist == null ? new ArrayList<Topic>() : topiclist;
		

	}

	public String getTitle() {
		return title;
	}

	public String getNumber() {
		return number;
	}
    //deal with the double quote, to put the words in double quote in one string
	private void setTitle(String line) {
		String lineNew="";	
		qs = new ArrayList<String>();
		String temp="";
		String[]query=line.split(" ");
		int flag=0;
		for(int i=0;i<query.length;i++){
			
			if(flag==0&&query[i].contains("\"")){
				 temp +=query[i]+"-";
				 flag=1;
				 continue; 
			}else if(flag==1&&!query[i].contains("\"")){
				temp +=query[i]+"-";
				continue;
			}else if(flag==1&&query[i].contains("\"")){
				temp +=query[i];
				qs.add(temp);
				flag=0;
			
			}else{
				qs.add(query[i]);
				
			}
			
		}
	
		for(String w:qs){
			lineNew+=w+" ";
		}
		title = lineNew;
		//System.out.println(title);
	}

	private void setNumber(String line) {
		number = line;
		//System.out.println(number);
	}

	
}
