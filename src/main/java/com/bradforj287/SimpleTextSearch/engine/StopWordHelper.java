package com.bradforj287.SimpleTextSearch.engine;

import com.bradforj287.SimpleTextSearch.SearchResult;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by brad on 7/12/15.
 */
public class StopWordHelper {

    //for now only english

    private static HashSet<String> stopWordSet = getStopWords();

    private StopWordHelper() {

    }

    private static HashSet<String> getStopWords() {

        HashSet<String> retVal = new HashSet<>();
        //Get file from resources folder
        ClassLoader classLoader = SearchResult.class.getClassLoader();

        File file = new File(classLoader.getResource("stopwords/en.txt").getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line == null) {
                    continue;
                }
                line = line.trim();
                line = line.toLowerCase();
                if (line.isEmpty()) {
                    continue;
                }
                retVal.add(line);
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static boolean isStopWord(String txt) {
        if (txt == null || txt.isEmpty()) {
            return true;
        }

        return stopWordSet.contains(txt);
    }

}
