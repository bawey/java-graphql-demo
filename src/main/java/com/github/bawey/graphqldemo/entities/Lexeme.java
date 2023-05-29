package com.github.bawey.graphqldemo.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class Lexeme {

    @RequiredArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Builder
    public static class LexemeId {
        private final String languageCode;
        private final String headword;
    }

    private final LexemeId id;

}
