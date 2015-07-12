package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.Document;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by brad on 6/6/15.
 */
public class ParsedDocument {

    private ImmutableList<Term> terms;
    private Document document;
    private ImmutableMap<String, Integer> wordFrequencyMap;
    private ImmutableSet<String> uniqueWords;

    public ParsedDocument(List<Term> terms, Document document) {
        Preconditions.checkNotNull(terms);
        Preconditions.checkNotNull(document);
        this.terms = ImmutableList.copyOf(terms);
        this.document = document;
        HashMap<String, Integer> wordFrequency = new HashMap<>();
        uniqueWords = null;

        for (Term t : terms) {
            String word = t.getWord();
            if (!wordFrequency.containsKey(word)) {
                wordFrequency.put(word, 0);

            }

            int count = wordFrequency.get(word);
            wordFrequency.put(word, count + 1);
        }

        wordFrequencyMap = ImmutableMap.copyOf(wordFrequency);
        uniqueWords = ImmutableSet.copyOf(getUniqueWordsHashSet());
    }


    public int getWordFrequency(String word) {
        if (!wordFrequencyMap.containsKey(word)) {
            return 0;
        }

        return wordFrequencyMap.get(word);
    }

    public boolean isEmpty() {
        return terms == null || terms.isEmpty();
    }

    public List<Term> getTerms() {
        return terms;
    }

    public Set<String> getUniqueWords() {
        return uniqueWords;
    }

    private HashSet<String> getUniqueWordsHashSet() {

        HashSet<String> w = new HashSet<>();
        for (Term t : terms) {
            w.add(t.getWord());
        }
        return w;
    }

    public Document getDocument() {
        return document;
    }
}
