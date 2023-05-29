package com.github.bawey.graphqldemo.loaders;


import com.github.bawey.graphqldemo.entities.Lexeme;
import com.github.bawey.graphqldemo.generated.server.types.*;
import com.github.bawey.graphqldemo.repositories.SensesRepository;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SensesBatchLoader implements MappedBatchLoader<Lexeme.LexemeId, List<Sense>> {

    private final SensesRepository repo;

    private static GrammaticalProperties createGrammaticalProperties(com.github.bawey.graphqldemo.entities.Sense dbSense) {
        if (dbSense.getPartOfSpeech() == com.github.bawey.graphqldemo.entities.Sense.PartOfSpeech.NOUN) {
            return NounProperties.builder().setPartOfSpeech(PartOfSpeech.NOUN).setGender(
                    Optional.ofNullable(dbSense.getGender()).map(Enum::name).map(GrammaticalGender::valueOf).orElse(null)
            ).build();
        }
        if (dbSense.getPartOfSpeech() == com.github.bawey.graphqldemo.entities.Sense.PartOfSpeech.VERB) {
            return VerbProperties.builder().setPartOfSpeech(PartOfSpeech.VERB).setTransitivity(
                    Optional.ofNullable(dbSense.getTransitivity()).map(Enum::name).map(Transitivity::valueOf).orElse(null)
            ).build();
        }
        return null;
    }

    @Override
    public CompletionStage<Map<Lexeme.LexemeId, List<Sense>>> load(Set<Lexeme.LexemeId> set) {

        return CompletableFuture.supplyAsync(() -> repo.findAllByLexemeIdIn(set))
                .thenApply(l -> l.stream().map(sense -> Sense.builder()
                                .setLanguageCode(sense.getId().getLanguageCode())
                                .setHeadword(sense.getId().getHeadword())
                                .setOrdinal(sense.getId().getOrdinal())
                                .setValue(sense.getValue())
                                .setProperties(createGrammaticalProperties(sense))
                                .build())
                        .collect(Collectors.groupingBy(e -> Lexeme.LexemeId.builder()
                                .headword(e.getHeadword())
                                .languageCode(e.getLanguageCode())
                                .build()
                        )));

    }
}
