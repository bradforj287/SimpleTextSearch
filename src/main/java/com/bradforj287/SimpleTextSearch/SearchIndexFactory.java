package com.bradforj287.SimpleTextSearch;

import com.bradforj287.SimpleTextSearch.engine.Corpus;
import com.bradforj287.SimpleTextSearch.engine.DocumentParser;
import com.bradforj287.SimpleTextSearch.engine.InvertedIndex;
import com.bradforj287.SimpleTextSearch.engine.ParsedDocument;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by brad on 6/6/15.
 */
public class SearchIndexFactory {

    private SearchIndexFactory() {

    }

    private static List<ParsedDocument> buildParsedDocuments(Collection<Document> docs, DocumentParser parser) {
        List<ParsedDocument> retVal = new ArrayList<>();

        for (Document doc : docs) {
            retVal.add(parser.parseDocument(doc));
        }

        return retVal;
    }

    private static Collection<ParsedDocument> buildParsedDocumentsParrallel(Collection<Document> theDocs) {
        int cores = Math.max(1, Runtime.getRuntime().availableProcessors());

        final Collection<ParsedDocument> parsedDocuments = new ConcurrentLinkedQueue<>();

        List<Document> docsList = new ArrayList<>(theDocs);
        List<Thread> threads = new ArrayList<>();

        final DocumentParser parser = new DocumentParser(true, true);
        for (final List<Document> partition : Lists.partition(docsList, cores)) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    parsedDocuments.addAll(buildParsedDocuments(partition, parser));
                }
            });
            threads.add(t);
            t.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return parsedDocuments;
    }

    public static TextSearchIndex buildIndex(Collection<Document> documents) {

        Collection<ParsedDocument> parsedDocuments = buildParsedDocumentsParrallel(documents);

        Corpus corpus = new Corpus(new ArrayList<>(parsedDocuments));
        InvertedIndex invertedIndex = new InvertedIndex(corpus);

        return invertedIndex;
    }
}
