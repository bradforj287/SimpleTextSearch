# SimpleTextSearch Overview
A lightweight and easy to use full text search implementation for Java. Optimized for smaller data sets that can fit entirely in memory. Useful for situations where traditional search engines are overkill and overly complicated.

Several assumptions are made in SimpleTextSearch to simplfy the problem:
* It is assumed your data can fit in memory. The Index is stored entirely in memory with nothing written to disk
* The Index itself is immutable. There is no support for automatic re-indexing of documents. Build a new index.
* Only the english language is supported (as of now) 
* This is only an Index and there is no sharding support. If you want sharding, you'd have to build it yourself. 

