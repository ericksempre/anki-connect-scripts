package com.erick.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhraseDTO {
    private String sentenceWithGap;
    private String sentenceWithAnswer;
    private String answer;
}
