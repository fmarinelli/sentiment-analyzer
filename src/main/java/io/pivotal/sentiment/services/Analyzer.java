package io.pivotal.sentiment.services;

import io.pivotal.sentiment.domain.ScoreAndMagnitude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Analyzer {

    @Autowired
    private LanguageApi languageApi;

    @PostMapping("/sentiment")
    public ScoreAndMagnitude sentiment(@RequestBody String content) {
        return languageApi.analyze(content);
    }
}
