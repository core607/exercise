package com.example.controller;

import com.example.mapping.WordSplitMapping;
import com.example.request.WordSplitRequest;
import com.example.response.WordSplitResponse;
import com.example.service.WordSplitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class WordSplitController {

    private final WordSplitService wordSplitService;
    private final WordSplitMapping wordSplitMapping;


    /**
     * Split sentence into words using dictionary
     *
     * @param request user inputted sentence
     * @return all possible word
     */
    @PostMapping("/v1/word-split")
	public ResponseEntity<WordSplitResponse> wordSplit(@RequestBody WordSplitRequest request) {
        List<String> results = wordSplitService.wordSplit(wordSplitMapping.toDTO(request));
        return ResponseEntity.ok(WordSplitResponse.builder().results(results).build());
    }

}
