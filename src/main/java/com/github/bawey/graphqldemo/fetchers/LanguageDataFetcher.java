package com.github.bawey.graphqldemo.fetchers;


import com.github.bawey.graphqldemo.generated.server.types.Language;
import com.github.bawey.graphqldemo.generated.server.types.Lexeme;
import com.github.bawey.graphqldemo.loaders.LanguagesBatchLoader;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class LanguageDataFetcher implements DataFetcher<CompletableFuture<Language>> {

    @Override
    public CompletableFuture<Language> get(DataFetchingEnvironment environment) {
        // the "owning" element, so to speak
        Lexeme lexeme = environment.getSource();
        // the batch loader
        DataLoader<String, Language> batchLoader = environment.getDataLoader(LanguagesBatchLoader.class.getSimpleName());
        return batchLoader.load(lexeme.getLanguageCode());

    }
}
