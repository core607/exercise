package com.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DictionaryType {
    @JsonProperty("default")
    DEFAULT_DICTIONARY,
    @JsonProperty("customer")
    CUSTOM_DICTIONARY,
    @JsonProperty("combine")
    COMBINE_DICTIONARY
}
