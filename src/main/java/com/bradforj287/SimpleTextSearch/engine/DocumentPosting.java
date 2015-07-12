package com.bradforj287.SimpleTextSearch.engine;

/**
 * Created by brad on 6/7/15.
 */
public class DocumentPosting {

    private DocumentTerm documentTerm;
    private ParsedDocument parsedDocument;
    public DocumentPosting(DocumentTerm documentTerm, ParsedDocument document) {
        this.documentTerm = documentTerm;
        this.parsedDocument = document;
    }

    public DocumentTerm getDocumentTerm() {
        return documentTerm;
    }

    public ParsedDocument getParsedDocument() {
        return parsedDocument;
    }

}
