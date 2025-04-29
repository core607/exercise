package com.example.service.impl;

import com.example.dto.WordSplitDTO;
import com.example.enums.DictionaryType;
import com.example.repository.DictionaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordSplitServiceImplTests {

    @Mock
    private DictionaryRepository repository;

    @InjectMocks
    private WordSplitServiceImpl wordSplitService;

    private final Set<String> mockDictionary = Set.of(
            "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man", "go"
    );


    @Test
    void wordSplit_ShouldThrowException_WhenInputIsNull() {
        WordSplitDTO input = new WordSplitDTO(null, null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> wordSplitService.wordSplit(input));

        assertEquals("Input sentence cannot be null or empty", exception.getMessage());
    }

    @Test
    void wordSplit_ShouldThrowException_WhenInputIsEmpty() {
        WordSplitDTO input = new WordSplitDTO("", null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> wordSplitService.wordSplit(input));

        assertEquals("Input sentence cannot be null or empty", exception.getMessage());
    }
    @Test
    void wordSplit_ShouldThrowException_WhenTypeIsEmpty() {
        WordSplitDTO input = new WordSplitDTO("abc", null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> wordSplitService.wordSplit(input));

        assertEquals("DictionaryType cannot be null", exception.getMessage());
    }

    @Test
    void wordSplit_ShouldReturnSingleWord_WhenInputIsInDictionary() {
        when(repository.getDictonary()).thenReturn(mockDictionary);
        WordSplitDTO input = new WordSplitDTO("sam", null, DictionaryType.DEFAULT_DICTIONARY);
        List<String> results = wordSplitService.wordSplit(input);

        assertEquals(1, results.size());
        assertEquals("sam", results.getFirst());
    }

    @Test
    void wordSplit_ShouldReturnMultipleCombinations_WhenMultipleSplitsExist() {
        when(repository.getDictonary()).thenReturn(mockDictionary);
        WordSplitDTO input = new WordSplitDTO("ilikesamsungmobile", null, DictionaryType.DEFAULT_DICTIONARY);

        List<String> results = wordSplitService.wordSplit(input);

        assertAll(
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.contains("i like sam sung mobile")),
                () -> assertTrue(results.contains("i like samsung mobile"))
        );
    }

    @Test
    void wordSplit_ShouldReturnEmptyList_WhenNoValidSplitExists() {
        when(repository.getDictonary()).thenReturn(mockDictionary);
        WordSplitDTO input = new WordSplitDTO("xyzabc", null, DictionaryType.DEFAULT_DICTIONARY);
        List<String> results = wordSplitService.wordSplit(input);
        assertTrue(results.isEmpty());
    }

    @Test
    void wordSplit_ShouldThrowException_WhenCustomDictionaryIsEmpty() {
        WordSplitDTO input = new WordSplitDTO("test", null, DictionaryType.CUSTOM_DICTIONARY);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> wordSplitService.wordSplit(input));

        assertEquals("Invalid input: customDictionary is null or empty.", exception.getMessage());
    }

    @Test
    void createDictionary_ShouldReturnCustomDictionary_WhenTypeIsCustom() {
        Set<String> customDict = Set.of("ilikesamsungmo", "bile");
        WordSplitDTO input = new WordSplitDTO("ilikesamsungmobile", customDict, DictionaryType.CUSTOM_DICTIONARY);

        List<String> results = wordSplitService.wordSplit(input);

        assertAll(
                () -> assertEquals(1, results.size()),
                () -> assertTrue(results.contains("ilikesamsungmo bile"))
        );
    }
}