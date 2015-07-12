package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.Document;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by brad on 6/6/15.
 */
public class ParsedDocument {

    private List<Term> terms;
    private Document document;
    private Map<String, Integer> wordFrequency;
    private HashSet<String> uniqueWords;

    public ParsedDocument(List<Term> terms, Document document) {
        Preconditions.checkNotNull(terms);
        Preconditions.checkNotNull(document);
        this.terms = terms;
        this.document = document;
        wordFrequency = new HashMap<>();
        uniqueWords = null;
        init();
    }

    private void init() {
        for (Term t : terms) {
            String word = t.getWord();
            if (!wordFrequency.containsKey(word)) {
                wordFrequency.put(word, 0);

            }

            int count = wordFrequency.get(word);
            wordFrequency.put(word, count + 1);
        }

        //prime unique words
        getUniqueWords();
    }

    public int getWordFrequency(String word) {
        if (!wordFrequency.containsKey(word)) {
            return 0;
        }

        return wordFrequency.get(word);
    }

    public boolean isEmpty() {
        return terms == null || terms.isEmpty();
    }

    public List<Term> getTerms() {
        return terms;
    }

    public HashSet<String> getUniqueWords() {
        if (uniqueWords == null) {
            HashSet<String> w = new HashSet<>();
            for (Term t : terms) {
                w.add(t.getWord());
            }
            uniqueWords = w;
        }

        return uniqueWords;
    }

    public Document getDocument() {
        return document;
    }
}
