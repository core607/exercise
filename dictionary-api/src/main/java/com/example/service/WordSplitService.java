package com.example.service;


import com.example.dto.WordSplitDTO;

import java.util.List;

public interface WordSplitService {

    List<String> wordSplit(WordSplitDTO wordSplitDTO);
}
