package edu.pitt.sis.infsci2140.analysis;


/**
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class TextTokenizer {
	private String total="";
	String[] m;
	int index = 0;

/*	public static void main(String[] args) throws IOException {
		String s_test = "After upgrading from 10.6 Snow Leopard to 10.9 Mavericks, I've noticed that all the MP4 files taken with my Samsung smartphone no longer play in QuickTime (or with Quick Look), whereas they played just fine in Snow Leopard. The MP4 files use the H.264 codec. Some other MP4s I have, which use the same codec, will play in QuickTime and Quick Look. I've also found some older MOV files (using the SVQ-3 codec) that don't play now.Has anyone else encountered this? Is there any fix, or does it perhaps require an OS update? Since I skipped OS 10.7 and 10.8, I don't know what the situation was there.";
		char[] texts = s_test.toCharArray();
		TextTokenizer t = new TextTokenizer(texts);
		while (t.nextWord() != null) {

			char[] z = t.nextWord();
		}
	}*/

	// YOU MUST IMPLEMENT THIS METHOD
	public TextTokenizer(char[] texts) {
		// this constructor will tokenize the input texts (usually it is a char
		// array for a whole document)
		//delete the punction get all of the words,and split with whitespace;
		String str = new String(texts);
		str = str.trim().replaceAll(" +", " ");
		str = str.replace(",", "");
		str = str.replace("?", "");
		str = str.replace("!", "");
		str = str.replace("&", "");
		str = str.replace("#", "");
		str = str.replace("!", "");
		str = str.replace("(", "");
		str = str.replace(")", "");
		str = str.replace("$", "");
		str = str.replace(".", "");
		str = str.replace("%", "");
		str = str.replace(":", "");
		str = str.replace(";", "");
		str = str.replace("+", "");
		str = str.replace("/", "");
		str = str.replace("\"", "");
		str = str.replace("*", "");
		str = str.replace("-", "");
		str.replaceAll("[0-9]","");
		total = str;
		m = total.split(" ");
	}

	// YOU MUST IMPLEMENT THIS METHOD
	//return every word
	public char[] nextWord() {
		
		if (index<m.length&&m[index]!=null) {
			//System.out.print(m[index]);
			return m[index++].toCharArray();
		} else
			return null;

	}
}

