package com.github.bawey.graphqldemo.loaders;

import com.github.bawey.graphqldemo.generated.server.types.Language;
import com.github.bawey.graphqldemo.repositories.LanguagesRepository;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LanguagesBatchLoader implements MappedBatchLoader<String, Language> {
    private final LanguagesRepository languagesRepository;

    @Override
    public CompletionStage<Map<String, Language>> load(Set<String> set) {
        return CompletableFuture.supplyAsync(() -> languagesRepository.findAllByIdIn(set)
                .stream().map(e -> Language.builder().setShortIsoCode(e.getCode()).setName(e.getName()).build())
                .collect(Collectors.toMap(Language::getShortIsoCode, e -> e)));
    }
}
