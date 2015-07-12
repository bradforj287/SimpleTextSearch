package com.bradforj287.SimpleTextSearch.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brad on 7/11/15.
 */
public class ParsedDocumentMetrics {
    private Corpus corpus;
    private ParsedDocument document;
    private Map<String, DocumentPostingCollection> termsToPostings;

    //metrics
    private Double magnitude;
    private HashMap<String, Double> tfidfCache;

    public ParsedDocumentMetrics(Corpus corpus, ParsedDocument document,
                                 Map<String, DocumentPostingCollection> termsToPostings) {
        this.corpus = corpus;
        this.document = document;
        this.termsToPostings = termsToPostings;
        this.tfidfCache = new HashMap<>();

        //init tfidf cache
        for (String word : document.getUniqueWords()) {
            tfidfCache.put(word, calcTfidf(word));
        }

        //prime magnitude cache
        getMagnitude();
    }

    public double getTfidf(String word) {
        Double retVal = tfidfCache.get(word);
        if (retVal == null) {
            return 0;
        }

        return retVal;
    }

    public double getMagnitude() {
        if (magnitude == null) {
            double sumOfSquares = 0;
            for (String word : document.getUniqueWords()) {
                double d = getTfidf(word);
                sumOfSquares += d * d;
            }

            magnitude = Math.sqrt(sumOfSquares);
        }

        return magnitude;
    }

    public ParsedDocument getDocument() {
        return this.document;
    }

    private double calcTfidf(String word) {
        int wordFreq = document.getWordFrequency(word);
        if (wordFreq == 0) {
            return 0;
        }
        return getInverseDocumentFrequency(word) * document.getWordFrequency(word);
    }

    private double getInverseDocumentFrequency(String word) {
        double totalNumDocuments = corpus.size();
        double numDocsWithTerm = numDocumentsTermIsIn(word);
        return Math.log((totalNumDocuments) / (1 + numDocsWithTerm));
    }

    private int numDocumentsTermIsIn(String term) {
        if (!termsToPostings.containsKey(term)) {
            return 0;
        }

        return termsToPostings.get(term).getUniqueDocuments().size();
    }

}
