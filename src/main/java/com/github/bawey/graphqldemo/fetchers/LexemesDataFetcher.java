package com.github.bawey.graphqldemo.fetchers;

import com.github.bawey.graphqldemo.generated.server.types.Lexeme;
import com.github.bawey.graphqldemo.generated.server.types.LexemesFilter;
import com.github.bawey.graphqldemo.repositories.LexemesRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LexemesDataFetcher implements DataFetcher<List<Lexeme>> {

    private final LexemesRepository repo;

    @Override
    public List<Lexeme> get(DataFetchingEnvironment environment) {
        // extract the filters from environment
        // TODO: any way to have the map auto-converted ?
        List<LexemesFilter> filters = Optional.ofNullable(environment.<List<Map<String, String>>>getArgument("filters"))
                .orElse(List.of()).stream().map(map -> LexemesFilter.builder().setToken(map.get("token"))
                        .setLanguageCode(map.get("languageCode")).build()).collect(Collectors.toList());
        //TODO: map the filter to an object from persistence realm and delegate the filtering!
        Predicate<com.github.bawey.graphqldemo.entities.Lexeme> adHocFilter = lexeme ->
                filters.isEmpty() || filters.stream()
                        .anyMatch(filter -> Optional.ofNullable(filter.getLanguageCode())
                                .filter(Predicate.not(lexeme.getId().getLanguageCode()::equalsIgnoreCase)).isEmpty()
                                && Optional.ofNullable(filter.getToken()).map(String::toLowerCase)
                                .filter(Predicate.not(lexeme.getId().getHeadword().toLowerCase()::contains)).isEmpty()
                        );
        return repo.findAll().stream().filter(adHocFilter).map(e -> Lexeme.builder()
                .setHeadword(e.getId().getHeadword())
                .setLanguageCode(e.getId().getLanguageCode())
                .build()).collect(Collectors.toList());
    }
}
