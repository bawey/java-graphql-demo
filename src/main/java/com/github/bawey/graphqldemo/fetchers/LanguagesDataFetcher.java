package com.github.bawey.graphqldemo.fetchers;

import com.github.bawey.graphqldemo.generated.server.types.Language;
import com.github.bawey.graphqldemo.repositories.LanguagesRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LanguagesDataFetcher implements DataFetcher<List<Language>> {

    private final LanguagesRepository repo;

    @Override
    public List<Language> get(DataFetchingEnvironment environment) {
        return repo.findAll().stream().map(e -> Language.builder()
                .setName(e.getName()).setShortIsoCode(e.getCode())
                .build()).collect(Collectors.toList());
    }

}
