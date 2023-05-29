package com.github.bawey.graphqldemo.fetchers;


import com.github.bawey.graphqldemo.entities.Lexeme;
import com.github.bawey.graphqldemo.generated.server.types.Sense;
import com.github.bawey.graphqldemo.loaders.SensesBatchLoader;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Component
public class SensesDataFetcher implements DataFetcher<CompletableFuture<List<Sense>>> {
    @Override
    public CompletableFuture<List<Sense>> get(DataFetchingEnvironment environment) {
        // get the owning object
        Lexeme.LexemeId id =
                Optional.<com.github.bawey.graphqldemo.generated.server.types.Lexeme>of(environment.getSource())
                        .map(l -> Lexeme.LexemeId.builder().languageCode(l.getLanguageCode()).headword(l.getHeadword()).build()).orElseThrow();
        // get the data loader
        DataLoader<Lexeme.LexemeId, List<Sense>> dataLoader = environment.getDataLoader(SensesBatchLoader.class.getSimpleName());
        return dataLoader.load(id);
    }
}
