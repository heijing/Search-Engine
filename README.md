# Search-Engine
###Project title: Search Engine for Document Collection
###Project summary and pre-description
    Object: It is a search engine you can get your top 500 results of your queries based on a TREC standard formatted collection (See more in background).
    Pre-description: 1. Queries for test are all set (See more in background).
                     2. The original document is two type, TREC doc and TREC text, do not worry about the format, I just extract the useful information (document id, document contents)
    Assumption: Every word in the document is independent, except the words in "" is a complete one.
###Description: 
    There are three functional packages.
1. analysis: is about preprocessing the document:
 1. remove the stop words in document (See more in background) 
 2. change every word to lower case 
 3. remove all the punctual and use space to separate them.
2. index: is about build a inverted index to map the words in collection. There are two types of inverted index: posting list, which stores the (term, document id, term frequency in document)
“DictionayTermFile” (term,term frequency in all documents) and one auxiliary document called "doc"(it records the document id and number of words in that document.)(See more in Background)
3. search: is about process the topic, build a language model and use smoothing probability method to compute the the word probability in every query, and based on the word probability in document, rank the document.

###Requirement:
    You only need to have java version JDK 1.6 and higher.
###User guide: 
    run the main method.
1. HW2Main1, if you run it, you can pre-processing your collection, and get the text-formatted inverted index from the TREC formatted collections. After running it, you will get three text file. One is “DictionaryTermFile.txt”, one is “PostingFile.txt”, and another file is called “Doc.txt”.
2. HW2Main2, if you want to search one word, and know how many time it appears in the collection and which document it appears, you can run it, you can change your word from "String token ="looser"; change any word you like, just replace the “looser” with it.
3. HW3Main, If you run it, you can completed build the search model, and get the sorted result of the every query.
The result format is:
             902     Q0 WSJ870520-0002 234 1.39051913440501E-9                MYRUN
explanation: topicid Q0     Documentid   rank-number-in-one-query Rank-score MYRUN

###Background:
  * Trectext file is in “Search-Engine/docset.trectext”
  * Trecweb file is in “Search-Engine/docset.trectweb”
  * Stopwords are in “Search-Engine/stopwors.txt”
  * Original query file is in” Search-Engine/topics.txt”
  * Formalized queries are in “Search-Engine/src/output/result.txt”
  * Output inverted index “DictionatyTermFile”  is in “Search-Engine/output/DictionaryTermFile.txt”
  * Output inverted index “Postingfile” is in “Search-Engine/output/Postingfile.txt”
  * Output Doc is in “Search-Engine/output/Doc.txt”
  * trectext format: each document starts with <DOC> and ends with </DOC>; <DOCNO> stores the unique identifier of each document; <TEXT> stores the main content of the document.
  * tretweb format: each document starts with <DOC> and ends with </DOC>; <DOCNO> stores the unique identifier of each document; <DOCHDR> usually stores the http response header information of accessing the web document; the content of the web document is stored between </DOCHDR> and </DOC>.
  * The smoothing probability I used is Dirichlet Prior Smoothing" probability.
  
