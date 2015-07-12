package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.*;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MinMaxPriorityQueue;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by brad on 6/6/15.
 */
public class InvertedIndex implements TextSearchIndex {

    private static int THREAD_POOL_SIZE = Math.max(1, Runtime.getRuntime().availableProcessors());

    private Corpus corpus;
    private ImmutableMap<String, DocumentPostingCollection> termToPostings;
    private ImmutableMap<ParsedDocument, ParsedDocumentMetrics> docToMetrics;
    private ExecutorService executorService;
    private DocumentParser searchTermParser;

    public InvertedIndex(Corpus corpus) {
        this.corpus = corpus;
        init();
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        searchTermParser = new DocumentParser(false, false);
    }

    private void init() {
        // build term -> posting map
        Map<String, DocumentPostingCollection> termToPostingsMap = new HashMap<>();
        for (ParsedDocument document : corpus.getParsedDocuments()) {
            for (DocumentTerm documentTerm : document.getDocumentTerms()) {
                final String word = documentTerm.getWord();
                if (!termToPostingsMap.containsKey(word)) {
                    termToPostingsMap.put(word, new DocumentPostingCollection(word));
                }
                termToPostingsMap.get(word).addPosting(documentTerm, document);
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

        ParsedDocument searchDocument = searchTermParser.parseDocument(new Document(searchTerm, new Object()));
        Set<ParsedDocument> documentsToScanSet = getRelevantDocuments(searchDocument);

        if (searchDocument.isEmpty() || documentsToScanSet.isEmpty()) {
            return buildResultBatch(new ArrayList<SearchResult>(), stopwatch, 0);
        }

        // do scan
        final Collection<SearchResult> resultsP = new ConcurrentLinkedQueue<>();

        List<ParsedDocument> documentsToScan = new ArrayList<>(documentsToScanSet);
        final ParsedDocumentMetrics pdm = new ParsedDocumentMetrics(corpus, searchDocument, termToPostings);
        List<Future> futures = new ArrayList<>();

        for (final List<ParsedDocument> partition : Lists.partition(documentsToScan, THREAD_POOL_SIZE)) {

            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (ParsedDocument doc : partition) {
                        double cosine = computeCosine(pdm, doc);

                        SearchResult result = new SearchResult();
                        result.setRelevanceScore(cosine);
                        result.setUniqueIdentifier(doc.getUniqueId());
                        resultsP.add(result);
                    }
                }
            });

            futures.add(future);
        }

        for (Future f : futures) {
            try {
                f.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        int heapSize = Math.min(resultsP.size(), maxResults);

        MinMaxPriorityQueue<SearchResult> maxHeap = MinMaxPriorityQueue.
                orderedBy(new Comparator<SearchResult>() {
                    @Override
                    public int compare(SearchResult o1, SearchResult o2) {
                        if (o1.getRelevanceScore() <= o2.getRelevanceScore()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }).
                maximumSize(heapSize).
                expectedSize(heapSize).create(resultsP);


        // return results
        ArrayList<SearchResult> r = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            SearchResult rs = maxHeap.removeFirst();
            r.add(rs);
        }

        return buildResultBatch(r, stopwatch, documentsToScan.size());
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


    private double computeCosine(ParsedDocumentMetrics searchDocMetrics, ParsedDocument d2) {
        double cosine = 0;

        Set<String> wordSet = searchDocMetrics.getDocument().getUniqueWords();
        ParsedDocument otherDocument = d2;
        if (d2.getUniqueWords().size() < wordSet.size()) {
            wordSet = d2.getUniqueWords();
            otherDocument = searchDocMetrics.getDocument();
        }
        for (String word : wordSet) {

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
