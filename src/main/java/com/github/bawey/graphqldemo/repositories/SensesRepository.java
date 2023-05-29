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
        registerSenses("DE", "Hund", "ein als Haustier gehaltenes Tier, das vom Wolf abstammt");
        registerSenses("DE", "Katze", "ein (Haus)Tier mit scharfen Zähnen und Krallen, das Mäuse fängt");
        registerSenses("FR", "chat",
                "Mammifère carnivore au museau court et arrondi, aux griffes rétractiles, " +
                        "dont il existe des espèces domestiques et des espèces sauvages",
                "En informatique, espace virtuel de dialogue en ligne réunissant des internautes qui communiquent " +
                        "par échanges de messages électroniques"
        );
        registerSenses("DE", "Mist", "eine Mischung aus Kot, Urin und Stroh, die man als Dünger verwendet");
        registerSenses("FR", "chien", "Mammifère à odorat développé, rapide à la course, dont l'homme s'est fait un compagnon de vie, de loisirs ou de travail");
        registerSenses("EN", "dog", "a member of the genus Canis (probably descended from the common wolf) that has been domesticated by man since prehistoric times; occurs in many breeds");
        registerSenses("EN", "chat", "an informal, light conversation");
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
