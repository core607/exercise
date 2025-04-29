package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        // 完整集成测试，不mock任何组件
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": "ilikesamsungmobile"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.results[0]").value("i like sam sung mobile"))
                .andExpect(jsonPath("$.results[1]").value("i like samsung mobile"));
    }

    @Test
    void shouldHandleInvalidInput() throws Exception {
        // 测试输入验证
        mockMvc.perform(post("/api/v1/word-split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "sentence": ""
                    }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Input sentence cannot be null or empty"));
    }

}