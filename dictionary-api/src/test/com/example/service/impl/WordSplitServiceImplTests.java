package com.example.service.impl;

import com.example.repository.DictionaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordSplitServiceImplTest {

    @Mock
    private DictionaryRepository repository;

    @InjectMocks
    private WordSplitServiceImpl wordSplitService;

    private final Set<String> mockDictionary = Set.of(
            "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man", "go"
    );


    @Test
    void wordSplit_ShouldThrowException_WhenInputIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> wordSplitService.wordSplit(null));

        assertEquals("Input sentence cannot be null or empty", exception.getMessage());
    }

    @Test
    void wordSplit_ShouldThrowException_WhenInputIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> wordSplitService.wordSplit(""));

        assertEquals("Input sentence cannot be null or empty", exception.getMessage());
    }

    @Test
    void wordSplit_ShouldReturnSingleWord_WhenInputIsInDictionary() {
        when(repository.getDictonary()).thenReturn(mockDictionary);

        List<String> results = wordSplitService.wordSplit("sam");

        assertEquals(1, results.size());
        assertEquals("sam", results.getFirst());
    }

    @Test
    void wordSplit_ShouldReturnMultipleCombinations_WhenMultipleSplitsExist() {
        when(repository.getDictonary()).thenReturn(mockDictionary);

        List<String> results = wordSplitService.wordSplit("ilikesamsungmobile");

        assertAll(
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.contains("i like sam sung mobile")),
                () -> assertTrue(results.contains("i like samsung mobile"))
        );
    }

    @Test
    void wordSplit_ShouldReturnEmptyList_WhenNoValidSplitExists() {
        when(repository.getDictonary()).thenReturn(mockDictionary);

        List<String> results = wordSplitService.wordSplit("xyzabc");

        assertTrue(results.isEmpty());
    }


}