package com.gustavofao.textprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextProcessor {

    public enum ProcessMode {
        BLACKLIST_ONLY, WHITELIST_ONLY, BOTH
    }

    private static final String SPECIAL_CHARACTERS = ",.()-!@#$%¨&*()_=+[]{}/;<>:\'\"";
    private static final String[] META_CHARACTERS = {"\\?"};

    private static final String BLACKLIST_PATH = "C:\\Users\\gusta\\Documents\\documentos-unifra\\6_semestre\\Otimizacao\\Dicionario\\blacklist";
    private static final String WHITELIST_PATH = "C:\\Users\\gusta\\Documents\\documentos-unifra\\6_semestre\\Otimizacao\\Dicionario\\whitelist";

    private final ProcessMode processMode;

    private Rule blackList;
    private Rule whiteList;

    public TextProcessor(ProcessMode processMode) throws IOException {
        this.processMode = processMode;

        if (processMode != ProcessMode.WHITELIST_ONLY) {
            List<String> blackWords = readFile(BLACKLIST_PATH);
            if (blackWords != null && !blackWords.isEmpty()) {
                blackList = new Rule(blackWords);
            } else {
                blackList = new Rule();
            }
        }

        if (processMode != ProcessMode.BLACKLIST_ONLY) {
            List<String> whiteWords = readFile(WHITELIST_PATH);
            if (whiteWords != null && !whiteWords.isEmpty()) {
                whiteList = new Rule(whiteWords);
            } else {
                whiteList = new Rule();
            }
        }
    }

    private List<String> readFile(String fileName) throws IOException {
        List<String> words = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            for (String word : Arrays.asList(line.toLowerCase().split(" "))) {
                words.add(word);
            }
        }

        return words;
    }

    private String scapeWord(String word) {
        if (!word.isEmpty()) {
            for (char c : SPECIAL_CHARACTERS.toCharArray()) {
                String chr = "" + c;

                if (word.contains(chr)) {
                    word = word.replaceAll(chr, "");
                }
            }

            for (String metaChar : META_CHARACTERS) {
                if (word.contains(metaChar)) {
                    word = word.replaceAll(metaChar, "");
                }
            }
        }

        return word;
    }

    public List<String> process(String text) {
        List<String> words = new ArrayList<>();

        text = text.toLowerCase();

        for (String word : text.split(" ")) {
            word = scapeWord(word);
            if (word.isEmpty() || words.contains(word))
                continue;

            boolean accepted;

            if (processMode == ProcessMode.BOTH) {
                boolean onWhiteList = whiteList.validate(word);
                if (onWhiteList) {
                    accepted = true;
                } else {
                    accepted = !blackList.validate(word);
                }
            } else if (processMode == ProcessMode.BLACKLIST_ONLY) {
                accepted = !blackList.validate(word);
            } else {
                accepted = whiteList.validate(word);
            }

            if (accepted )
                words.add(word);
        }

        return words;
    }


}
