package com.bradforj287.SimpleTextSearch;

import java.util.List;

/**
 * Created by brad on 7/11/15.
 */
public class SearchResultBatch {
    private List<SearchResult> searchResults;
    private SearchStats stats;

    public SearchStats getStats() {
        return stats;
    }

    public void setStats(SearchStats stats) {
        this.stats = stats;
    }

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }
}
