package com.github.bawey.graphqldemo.fetchers;

import com.github.bawey.graphqldemo.generated.server.types.Language;
import com.github.bawey.graphqldemo.generated.server.types.Lexeme;
import com.github.bawey.graphqldemo.repositories.LanguagesRepository;
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
    private final LanguagesRepository langRepo;

    @Override
    public List<Lexeme> get(DataFetchingEnvironment environment) {
        return repo.findAll().stream().map(e -> {
            final var dbLang = langRepo.getById(e.getId().getLanguageCode());
            return Lexeme.builder()
                    .setLanguage(Language.builder().setShortIsoCode(dbLang.getCode()).setName(dbLang.getName()).build())
                    .setHeadword(e.getId().getHeadword())
                    .build();
        }).collect(Collectors.toList());
    }
}
