package com.bradforj287.SimpleTextSearch.engine;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brad on 6/6/15.
 */
public class TextParseUtils {

    private TextParseUtils() {

    }

    public static String stemWord(String word) {
        englishStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(word);
        return stemmer.getCurrent();
    }

    public static List<String> tokenize(String rawText) {

        List<String> retVal = new ArrayList<>();
        if (StringUtils.isEmpty(rawText)) {
            return retVal;
        }

        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new StringReader(rawText),
                new CoreLabelTokenFactory(), "");
        while (ptbt.hasNext()) {
            CoreLabel label = ptbt.next();
            String str = label.toString();
            if (str == null) {
                continue;
            }

            str = str.replaceAll("[^a-zA-Z ]", "");

            if (str.isEmpty()) {
                continue;
            }

            retVal.add(str);
        }


        return retVal;
    }

}
