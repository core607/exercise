package com.example.mapping;


import com.example.dto.WordSplitDTO;
import com.example.request.WordSplitRequest;
import org.springframework.stereotype.Component;

@Component
public class WordSplitMapping {
    public WordSplitDTO toDTO(WordSplitRequest request) {
        return WordSplitDTO.builder()
                .sentence(request.getSentence())
                .customDictionary(request.getCustomDictionary())
                .dictionaryType(request.getDictionaryType()).build();

    }

}
