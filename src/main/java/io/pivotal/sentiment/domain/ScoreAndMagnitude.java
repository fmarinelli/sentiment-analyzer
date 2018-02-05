package io.pivotal.sentiment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreAndMagnitude {

    private Float score;
    private Float magnitude;
}
