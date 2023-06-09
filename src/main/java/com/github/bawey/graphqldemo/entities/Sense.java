package com.github.bawey.graphqldemo.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class Sense {

    public enum PartOfSpeech {
        NOUN, VERB;
    }

    public enum Transitivity {
        INTRANSITIVE, MONOTRANSITIVE, DITRANSITIVE;
    }

    public enum Gender {
        FEMININE, MASCULINE, NEUTER;
    }

    @RequiredArgsConstructor
    @Getter
    @Builder
    @EqualsAndHashCode
    public static class SenseId {
        private final String languageCode;
        private final String headword;
        private final int ordinal;

        public static SenseId forLanguageHeadwordAndOrdinal(String code, String headword, int ordinal) {
            return new SenseId(code, headword, ordinal);
        }


        public Lexeme.LexemeId toLexemeId() {
            return Lexeme.LexemeId.builder().headword(headword).languageCode(languageCode).build();
        }
    }

    private final SenseId id;
    private final String value;
    //TODO: crude PoC
    private final PartOfSpeech partOfSpeech;
    private final Transitivity transitivity;
    private final Gender gender;
}
