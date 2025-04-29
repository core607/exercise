package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WordSplitIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnValidWordBreaks() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "ilikesamsungmobile",
                        "dictionaryType":"default"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.results[0]").value("i like sam sung mobile"))
                .andExpect(jsonPath("$.results[1]").value("i like samsung mobile"));
    }

    @Test
    void shouldReturnValidWordBreaksWithCustomerDictionary() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "ilikesamsungmobile",
                        "dictionaryType": "customer",
                        "customDictionary": ["ilikesam", "sungmobile"]
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(1))
                .andExpect(jsonPath("$.results[0]").value("ilikesam sungmobile"));
    }

    @Test
    void shouldHandleInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "",
                        "dictionaryType":"default"
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Input sentence cannot be null or empty"));
    }



    @Test
    void shouldReturn400WhenDictionaryTypeIsNull() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "test",
                        "dictionaryType": null
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("DictionaryType cannot be null"));
    }

    @Test
    void shouldReturn400WhenCustomDictionaryIsNull() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "test",
                        "dictionaryType": "customer",
                        "customDictionary": null
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input: customDictionary is null or empty."));
    }

    @Test
    void shouldReturn400WhenCustomDictionaryIsEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "test",
                        "dictionaryType": "customer",
                        "customDictionary": []
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input: customDictionary is null or empty."));
    }

    @Test
    void shouldReturn400WhenInvalidDictionaryType() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "test",
                        "dictionaryType": "invalid"
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("JSON parse error")));
    }


    @Test
    void shouldReturnEmptyListWhenNoPossibleSplits() throws Exception {
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "xyzabc",
                        "dictionaryType": "customer",
                        "customDictionary": ["test"]
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(0));
    }

}