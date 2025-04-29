package com.example.service.impl;


import com.example.repository.DictionaryRepository;
import com.example.service.WordSplitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Word Split Service Implementation
 *
 */
@Slf4j
@Service
@AllArgsConstructor
public class WordSplitServiceImpl implements WordSplitService {

    private final DictionaryRepository repository;

    /**
     * Split sentence into words using dictionary
     *
     * @param sentence user inputted sentence
     * @return all possible word
     */
    @Override
    public List<String> wordSplit(String sentence) {
        // Validate input
        if (!StringUtils.hasLength(sentence)) {
            log.warn("Invalid input: sentence is null or empty. sentence: {}", sentence); // 基础版
            throw new IllegalArgumentException("Input sentence cannot be null or empty");
        }
        // Get dictionary from repository(fake code)
        Set<String> dictionary = repository.getDictonary();
        return splitAlgorithm(sentence, dictionary);
    }


    /**
     * Recursive backtracking algorithm for word splitting
     *
     * @param remainingText remaining text to be split
     * @param dictionary    dictionary
     * @return all possible word
     */
    private List<String> splitAlgorithm(String remainingText, Set<String> dictionary) {
        List<String> results = new ArrayList<>();

        if (remainingText.isEmpty()) {
            results.add("");
            return results;
        }
        for (int i = 1; i <= remainingText.length(); i++) {
            String prefix = remainingText.substring(0, i);
            // Example: isamsung
            // First iteration in this if block: prefix will be 'i', remainingText will be 'samsung'
            // Second iteration: prefix will be 'sam', remainingText will be 'sung'
            // Third iteration: prefix will be 'sung', remainingText will be empty, then we backtrack and add the result to the list

            // When we return to prefix being 'i' and remainingText being 'samsung', we continue the loop
            // and find that 'samsung' is also in the dictionary. We process it similarly, saving results and backtracking
            // Finally, we try prefixes like 'is', 'isa', etc., but none of them are found in the dictionary
            // So the final results are 'i sam sung' and 'i samsung'
            if (dictionary.contains(prefix)) {
                List<String> subResults = splitAlgorithm(remainingText.substring(i), dictionary);
                for (String subResult : subResults) {
                    results.add(prefix + (subResult.isEmpty() ? "" : " " + subResult));
                }
            }
        }

        return results;
    }
}
