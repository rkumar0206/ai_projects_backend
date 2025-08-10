package com.rtb.the_random_value.story.service;

import com.google.genai.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomStoryService {

    private final Client client;

}
