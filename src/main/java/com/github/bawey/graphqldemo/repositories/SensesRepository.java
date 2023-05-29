package com.github.bawey.graphqldemo.repositories;

import com.github.bawey.graphqldemo.entities.Lexeme;
import com.github.bawey.graphqldemo.entities.Sense;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SensesRepository implements DummyRepository<Sense, Sense.SenseId> {

    public static final List<Sense> DATA = new LinkedList<>();

    private static void registerSenses(String languageCode, String headword, String... senses) {
        for (int i = 0; i < senses.length; ++i) {
            DATA.add(Sense.builder().id(
                            Sense.SenseId.forLanguageHeadwordAndOrdinal(languageCode, headword, i + 1))
                    .value(senses[i]).build());
        }
    }

    static {
        registerSenses("EN", "pitch",
                "a thick black substance obtained from tar",
                "the field or ground for certain games",
                "the degree of highness or lowness of a musical note, voice etc.",
                "the act of throwing a baseball by a pitcher to a batter",
                "degree of deviation from a horizontal plane");
        registerSenses("EN", "mist",
                "a mass of minute globules of water suspended in the atmosphere, resembling fog but not as dense",
                "something that dims, obscures, or blurs");
    }

    @Override
    public List<Sense> findAll() {
        return new ArrayList<>(DATA);
    }

    @Override
    public Sense getById(Sense.SenseId id) {
        return DATA.stream().filter(e -> e.getId().equals(id)).findFirst().orElseThrow();
    }

    public List<Sense> findAllByLexemeIdIn(Set<Lexeme.LexemeId> lexemeIds) {
        return DATA.stream()
                .filter(d -> {
                    final var lexemeId = Lexeme.LexemeId.builder()
                            .languageCode(d.getId().getLanguageCode())
                            .headword(d.getId().getHeadword())
                            .build();
                    return lexemeIds.contains(lexemeId);
                })
                .collect(Collectors.toList());
    }
}
