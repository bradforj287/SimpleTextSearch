package com.bradforj287.SimpleTextSearch.engine;

/**
 * Created by brad on 6/7/15.
 */
public class DocumentPosting {

    private Term term;
    private ParsedDocument parsedDocument;
    public DocumentPosting(Term term, ParsedDocument document) {
        this.term = term;
        this.parsedDocument = document;
    }

    public Term getTerm() {
        return term;
    }

    public ParsedDocument getParsedDocument() {
        return parsedDocument;
    }

}
