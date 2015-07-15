# SimpleTextSearch Overview
A lightweight and easy to use full text search implementation for Java. For data sets that can fit entirely in memory. Useful for situations where traditional search engines are overkill and overly complicated.

###Several assumptions are made in SimpleTextSearch:
* It is assumed your data can fit in memory. The Index is stored entirely in memory with nothing written to disk
* The Index itself is immutable. There is no support for automatic re-indexing of documents. Build a new index.
* Only the english language is supported (as of now) 
* This is only an Index and there is no sharding support. If you want sharding, you'd have to build it yourself. 
* Only freeform text searches are supported. No advanced search operators.

###Key Features:
* Inverted Index
* Cosine Similarity algorithm w/ TFIDF ranking
* MultiThreadded index creation and searching
* Word Stemming (snowball stemmer)
* Strips HTML tags automatically
* Stop words
* String tokenizer (Stanford NLP)
 
### Example
        List<Document> documents = new ArrayList<>();
        documents.add(new Document("mad", new Integer(1)));
        documents.add(new Document("in pursuit", new Integer(2)));
        documents.add(new Document("abcd", new Integer(3)));
        documents.add(new Document("possession so and", new Integer(4)));

        TextSearchIndex index = SearchIndexFactory.buildIndex(documents);

        String searchTerm = "Mad in pursuit and in possession so";

        SearchResultBatch batch = index.search(searchTerm, 10);
##
