package com.bradforj287.SimpleTextSearch;

/**
 * Created by brad on 6/7/15.
 */
public class SearchResult {

    private double relevanceScore;
    private Document document;

    public Object getUniqueIdentifier() {
        return document.getUniqueIdentifier();
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
