package com.example.service.impl;


import com.example.dto.WordSplitDTO;
import com.example.enums.DictionaryType;
import com.example.repository.DictionaryRepository;
import com.example.service.WordSplitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param wordSplitDTO user inputted sentence and customized dictionary
     * @return all possible word
     */
    @Override
    public List<String> wordSplit(WordSplitDTO wordSplitDTO) {
        // Validate input
        validateDto(wordSplitDTO);
        Set<String> dictionary = createDictionary(wordSplitDTO);
        return splitAlgorithm(wordSplitDTO.getSentence(), dictionary);
    }

    private void validateDto(WordSplitDTO wordSplitDTO) {
        if (!StringUtils.hasLength(wordSplitDTO.getSentence())) {
            log.error("Invalid input: sentence is null or empty. wordSplitDTO: {}", wordSplitDTO);
            throw new IllegalArgumentException("Input sentence cannot be null or empty");
        }
        if (wordSplitDTO.getDictionaryType() == null) {
            log.error("DictionaryType is null. wordSplitDTO: {}", wordSplitDTO);
            throw new IllegalArgumentException("DictionaryType cannot be null");
        }
        if ((wordSplitDTO.getDictionaryType().equals(DictionaryType.CUSTOM_DICTIONARY)
                || wordSplitDTO.getDictionaryType().equals(DictionaryType.COMBINE_DICTIONARY)) &&
                CollectionUtils.isEmpty(wordSplitDTO.getCustomDictionary())){
            log.error("Invalid input: customDictionary is null or empty. wordSplitDTO: {}", wordSplitDTO);
            throw new IllegalArgumentException("Invalid input: customDictionary is null or empty.");
        }
    }

    private Set<String> createDictionary(WordSplitDTO wordSplitDTO) {
        return switch (wordSplitDTO.getDictionaryType()) {
            case DEFAULT_DICTIONARY -> repository.getDictonary();
            case CUSTOM_DICTIONARY -> wordSplitDTO.getCustomDictionary();
            case COMBINE_DICTIONARY -> Stream.concat(repository.getDictonary().stream(),
                    wordSplitDTO.getCustomDictionary().stream()).collect(Collectors.toSet());
        };
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
