package com.bradforj287.SimpleTextSearch;

/**
 * Created by brad on 6/7/15.
 */
public class SearchResult {

    private double relevanceScore;
    private Object uniqueIdentifier;

    public Object getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(Object uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }
}
