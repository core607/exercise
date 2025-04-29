package com.example.dto;

import com.example.enums.DictionaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordSplitDTO {
    private String sentence;
    private Set<String> customDictionary;
    private DictionaryType dictionaryType;
}
