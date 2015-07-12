package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.Document;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brad on 6/7/15.
 */
public class DocumentParser {

    public DocumentParser() {

    }

    public ParsedDocument parseDocument(Document doc) {
        List<Term> terms = rawTextToTermList(doc.getRawText());

        ParsedDocument document = new ParsedDocument(terms, doc);
        return document;
    }

    private List<Term> rawTextToTermList(String rawText) {
        String text = rawText;

        if (StringUtils.isEmpty(text)) {
            return new ArrayList<Term>();
        }

        // TODO: strip HTML

        // to lower case
        text = text.toLowerCase();

        //TODO: implement stop words

        // iterate over parsed terms
        List<String> terms = TextParseUtils.tokenize(text);

        List<Term> retVal = new ArrayList<>();
        int pos = 0;
        for (String str : terms) {
            String stemmedTerm = TextParseUtils.stemWord(str);
            retVal.add(new Term(stemmedTerm, pos));
            pos++;
        }

        return retVal;
    }
}
