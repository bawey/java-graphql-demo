package com.github.bawey.graphqldemo.loaders;


import com.github.bawey.graphqldemo.entities.Lexeme;
import com.github.bawey.graphqldemo.generated.server.types.Sense;
import com.github.bawey.graphqldemo.repositories.SensesRepository;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SensesBatchLoader implements MappedBatchLoader<Lexeme.LexemeId, List<Sense>> {

    private final SensesRepository repo;

    @Override
    public CompletionStage<Map<Lexeme.LexemeId, List<Sense>>> load(Set<Lexeme.LexemeId> set) {

        return CompletableFuture.supplyAsync(() -> repo.findAllByLexemeIdIn(set))
                .thenApply(l -> l.stream().map(e -> Sense.builder()
                                .setLanguageCode(e.getId().getLanguageCode())
                                .setHeadword(e.getId().getHeadword())
                                .setOrdinal(e.getId().getOrdinal())
                                .setValue(e.getValue())
                                .build())
                        .collect(Collectors.groupingBy(e -> Lexeme.LexemeId.builder()
                                .headword(e.getHeadword())
                                .languageCode(e.getLanguageCode())
                                .build()
                        )));

    }
}
