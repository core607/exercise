package com.example.request;

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
public class WordSplitRequest {
    /**
     * Input sentence to be split into words
     * Example: "ilikesamsungmobile"
     */
    private String sentence;

    /**
     * Custom dictionary words provided by the user
     * Only used when dictionaryType is CUSTOM_DICTIONARY or COMBINE
     */
    private Set<String> customDictionary;

    /**
     * Type of dictionary to use for word splitting
     * @see DictionaryType
     */
    private DictionaryType dictionaryType;
}
