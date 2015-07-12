package com.bradforj287.SimpleTextSearch.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by brad on 6/7/15.
 */
public class DocumentPostingCollection {

    private String word;
    private List<DocumentPosting> postings;
    private HashSet<ParsedDocument> uniqueDocuments;

    public DocumentPostingCollection(String word) {
        this.word = word;
        this.postings = new ArrayList<>();
        this.uniqueDocuments = new HashSet<>();
    }

    public void addPosting(DocumentTerm documentTerm, ParsedDocument doc) {
        postings.add(new DocumentPosting(documentTerm, doc));
        uniqueDocuments.add(doc);
    }

    public String getWord() {
        return word;
    }

    public List<DocumentPosting> getPostings() {
        return postings;
    }

    public HashSet<ParsedDocument> getUniqueDocuments() {
        return uniqueDocuments;
    }
}
