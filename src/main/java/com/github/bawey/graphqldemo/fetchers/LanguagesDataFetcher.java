package com.github.bawey.graphqldemo.fetchers;

import com.github.bawey.graphqldemo.generated.server.types.Language;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LanguagesDataFetcher implements DataFetcher<List<Language>> {

    private static final List<Language> languages = new ArrayList<>(3);

    static {
        Language de = new Language();
        de.setName("German");
        de.setShortIsoCode("de");
        languages.add(de);
        Language en = new Language();
        en.setName("English");
        en.setShortIsoCode("en");
        languages.add(en);
        Language fr = new Language();
        fr.setName("French");
        fr.setShortIsoCode("fr");
        languages.add(fr);
    }

    @Override
    public List<Language> get(DataFetchingEnvironment environment) throws Exception {
        return new ArrayList<>(languages);
    }

}
