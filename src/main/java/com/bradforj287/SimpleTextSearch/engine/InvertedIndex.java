package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.*;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex implements TextSearchIndex {

    private Corpus corpus;
    private ImmutableMap<String, DocumentPostingCollection> termToPostings;
    private ImmutableMap<ParsedDocument, ParsedDocumentMetrics> docToMetrics;

    public InvertedIndex(Corpus corpus) {
        this.corpus = corpus;
        init();
    }

    private void init() {
        // build term -> posting map
        Map<String, DocumentPostingCollection> termToPostingsMap = new HashMap<>();
        for (ParsedDocument document : corpus.getParsedDocuments()) {
            for (Term term : document.getTerms()) {
                final String word = term.getWord();
                if (!termToPostingsMap.containsKey(word)) {
                    termToPostingsMap.put(word, new DocumentPostingCollection(word));
                }
                termToPostingsMap.get(word).addPosting(term, document);
            }
        }

        termToPostings = ImmutableMap.copyOf(termToPostingsMap);

        //init metrics cache
        Map<ParsedDocument, ParsedDocumentMetrics> metricsMap = new HashMap<>();
        for (ParsedDocument document : corpus.getParsedDocuments()) {
            metricsMap.put(document, new ParsedDocumentMetrics(corpus, document, termToPostings));
        }
        docToMetrics = ImmutableMap.copyOf(metricsMap);
    }

    public int numDocuments() {
        return corpus.size();
    }

    public int termCount() {
        return termToPostings.keySet().size();
    }

    private Set<ParsedDocument> getRelevantDocuments(ParsedDocument searchDoc) {

        Set<ParsedDocument> retVal = new HashSet<>();
        for (String word : searchDoc.getUniqueWords()) {
            if (termToPostings.containsKey(word)) {
                retVal.addAll(termToPostings.get(word).getUniqueDocuments());
            }
        }

        return retVal;
    }

    @Override
    public SearchResultBatch search(String searchTerm, int maxResults) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        // figure out relevant documents to scan
        DocumentParser parser = new DocumentParser();
        ParsedDocument searchDocument = parser.parseDocument(new Document(searchTerm, null));
        Set<ParsedDocument> documentsToScan = getRelevantDocuments(searchDocument);

        // do scan
        List<SearchResult> results = new ArrayList<>();
        if (searchDocument.isEmpty() || documentsToScan.isEmpty()) {
            return buildResultBatch(results, stopwatch, 0);
        }
        ParsedDocumentMetrics pdm = new ParsedDocumentMetrics(corpus, searchDocument, termToPostings);
        for (ParsedDocument doc : documentsToScan) {
            double cosine = computeCosine(pdm, doc);

            SearchResult result = new SearchResult();
            result.setDocument(doc.getDocument());
            result.setRelevanceScore(cosine);
            results.add(result);
        }

        // sort results
        Collections.sort(results, new Comparator<SearchResult>() {
            @Override
            public int compare(SearchResult o1, SearchResult o2) {
                Double d1 = o1.getRelevanceScore();
                Double d2 = o2.getRelevanceScore();
                return d2.compareTo(d1);
            }
        });

        // return results
        ArrayList<SearchResult> r = new ArrayList<>();
        for (int i = 0; i < maxResults && i < results.size(); i++) {
            r.add(results.get(i));
        }

        return buildResultBatch(results, stopwatch, documentsToScan.size());

    }

    private SearchResultBatch buildResultBatch(List<SearchResult> results, Stopwatch sw, int numDocumentsSearched) {
        SearchResultBatch retVal = new SearchResultBatch();

        SearchStats stats = new SearchStats();
        stats.setDocumentsSearched(numDocumentsSearched);
        stats.setDurationNanos(sw.elapsed(TimeUnit.NANOSECONDS));

        retVal.setSearchResults(results);
        retVal.setStats(stats);

        return retVal;
    }


    private SearchResultBatch computeCosineSimilarityAndReturnResults(ParsedDocument searchDocument,
                                                                       Set<ParsedDocument> relevantDocuments,
                                                                       int maxResults) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        List<SearchResult> results = new ArrayList<>();

        if (searchDocument.isEmpty() || relevantDocuments.isEmpty()) {
            return buildResultBatch(results, stopwatch, 0);
        }

        ParsedDocumentMetrics searchDocumentMetrics = new ParsedDocumentMetrics(corpus, searchDocument, termToPostings);
        for (ParsedDocument doc : relevantDocuments) {
            double cosine = computeCosine(searchDocumentMetrics, doc);

            SearchResult result = new SearchResult();
            result.setDocument(doc.getDocument());
            result.setRelevanceScore(cosine);
            results.add(result);
        }

        Collections.sort(results, new Comparator<SearchResult>() {
            @Override
            public int compare(SearchResult o1, SearchResult o2) {
                Double d1 = o1.getRelevanceScore();
                Double d2 = o2.getRelevanceScore();
                return d2.compareTo(d1);
            }
        });

        ArrayList<SearchResult> r = new ArrayList<>();

        for (int i = 0; i < maxResults && i < results.size(); i++) {
            r.add(results.get(i));
        }

        return buildResultBatch(results, stopwatch, relevantDocuments.size());

    }

    private double computeCosine(ParsedDocumentMetrics searchDocMetrics, ParsedDocument d2) {
        double cosine = 0;

        HashSet<String> wordSet = searchDocMetrics.getDocument().getUniqueWords();
        ParsedDocument otherDocument = d2;
        if (d2.getUniqueWords().size() < wordSet.size()) {
            wordSet = d2.getUniqueWords();
            otherDocument = searchDocMetrics.getDocument();
        }
        for (String word : wordSet) {

            // optimization - if this is 0 then the TFIDF for d2 will be 0 and the term below is 0
            if (otherDocument.getWordFrequency(word) == 0) {
                continue;
            }

            double term = ((searchDocMetrics.getTfidf(word)) / searchDocMetrics.getMagnitude()) *
                    ( (docToMetrics.get(d2).getTfidf(word)) / docToMetrics.get(d2).getMagnitude());
            cosine = cosine + term;
        }
        return cosine;
    }


    public Map<String, DocumentPostingCollection> getTermToPostings() {
        return termToPostings;
    }
}
