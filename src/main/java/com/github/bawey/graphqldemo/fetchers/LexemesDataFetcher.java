package com.github.bawey.graphqldemo.fetchers;

import com.github.bawey.graphqldemo.generated.server.types.Lexeme;
import com.github.bawey.graphqldemo.repositories.LexemesRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LexemesDataFetcher implements DataFetcher<List<Lexeme>> {

    private final LexemesRepository repo;

    @Override
    public List<Lexeme> get(DataFetchingEnvironment environment) {
        return repo.findAll().stream().map(e -> Lexeme.builder()
                .setHeadword(e.getId().getHeadword())
                .setLanguageCode(e.getId().getLanguageCode())
                .build()).collect(Collectors.toList());
    }
}
