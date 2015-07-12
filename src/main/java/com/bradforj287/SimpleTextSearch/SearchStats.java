package com.bradforj287.SimpleTextSearch;

/**
 * Created by brad on 7/11/15.
 */
public class SearchStats {

    private int documentsSearched;
    private long durationNanos;

    public int getDocumentsSearched() {
        return documentsSearched;
    }

    public void setDocumentsSearched(int documentsSearched) {
        this.documentsSearched = documentsSearched;
    }

    public long getDurationNanos() {
        return durationNanos;
    }

    public void setDurationNanos(long durationNanos) {
        this.durationNanos = durationNanos;
    }

}
