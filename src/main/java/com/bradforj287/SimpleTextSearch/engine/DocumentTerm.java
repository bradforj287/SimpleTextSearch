package com.bradforj287.SimpleTextSearch.engine;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by brad on 6/6/15.
 */
public class DocumentTerm {

    private String word;
    private int positionInDoc;

    public DocumentTerm(String word, int positionInDoc) {
        Preconditions.checkArgument(!StringUtils.isEmpty(word));
        this.word = word;
        this.positionInDoc = positionInDoc;
    }

    public String getWord() {
        return word;
    }

    public int getPositionInDoc() {
        return positionInDoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DocumentTerm documentTerm = (DocumentTerm) o;

        return new EqualsBuilder()
                .append(positionInDoc, documentTerm.positionInDoc)
                .append(word, documentTerm.word)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(word)
                .append(positionInDoc)
                .toHashCode();
    }
}
