package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(final String prefix) {
        Random random=new Random();

        Comparator<String> comparator=new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.startsWith(s2))
                    return 0;
                return s1.compareTo(s2);
            }
        };
        int index=random.nextInt(words.size());
        if (prefix==null){
            return words.get(index);
        }
        else {
            index=Collections.binarySearch(words,prefix,comparator);
            if (index<0)
                return null;
        }
        return words.get(index);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
