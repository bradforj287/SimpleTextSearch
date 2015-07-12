package com.bradforj287.SimpleTextSearch.engine;

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

    private ImmutableList<DocumentTerm> documentTerms;
    private ImmutableMap<String, Integer> wordFrequencyMap;
    private ImmutableSet<String> uniqueWords;
    private Object uniqueId;

    public ParsedDocument(List<DocumentTerm> documentTerms, Object uniqueId) {
        Preconditions.checkNotNull(uniqueId);
        Preconditions.checkNotNull(documentTerms);
        this.documentTerms = ImmutableList.copyOf(documentTerms);
        this.uniqueId = uniqueId;
        HashMap<String, Integer> wordFrequency = new HashMap<>();
        uniqueWords = null;

        for (DocumentTerm t : documentTerms) {
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
        return documentTerms == null || documentTerms.isEmpty();
    }

    public List<DocumentTerm> getDocumentTerms() {
        return documentTerms;
    }

    public Set<String> getUniqueWords() {
        return uniqueWords;
    }

    private HashSet<String> getUniqueWordsHashSet() {

        HashSet<String> w = new HashSet<>();
        for (DocumentTerm t : documentTerms) {
            w.add(t.getWord());
        }
        return w;
    }

    public Object getUniqueId() {
        return uniqueId;
    }
}
