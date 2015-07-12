package com.bradforj287.SimpleTextSearch;

/**
 * Created by brad on 6/6/15.
 */
public interface TextSearchIndex {
    SearchResultBatch search(String searchTerm, int maxResults);

    int numDocuments();

    int termCount();
}
