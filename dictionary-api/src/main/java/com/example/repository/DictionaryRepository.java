package com.example.repository;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class DictionaryRepository {
    private static final Set<String> DEFAULT_DICTIONARY = Set.of(
            "i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "man", "go"
    );
    public Set<String> getDictonary() {
        return DEFAULT_DICTIONARY;
    }
}
