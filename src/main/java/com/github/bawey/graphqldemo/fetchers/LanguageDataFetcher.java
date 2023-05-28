package com.github.bawey.graphqldemo.fetchers;


import com.github.bawey.graphqldemo.generated.server.types.Language;
import com.github.bawey.graphqldemo.generated.server.types.Lexeme;
import com.github.bawey.graphqldemo.repositories.LanguagesRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LanguageDataFetcher implements DataFetcher<Language> {

    private final LanguagesRepository repo;

    @Override
    public Language get(DataFetchingEnvironment environment) {
        // the "owning" element, so to speak
        Lexeme lexeme = environment.getSource();
        return Optional.of(repo.getById(lexeme.getLanguageCode()))
                .map(e -> Language.builder().setName(e.getName()).setShortIsoCode(e.getCode())
                        .build()).orElseThrow();
    }
}
