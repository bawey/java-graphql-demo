package com.github.bawey.graphqldemo.repositories;

import com.github.bawey.graphqldemo.entities.Language;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LanguagesRepository implements DummyRepository<Language, String> {

    private final static List<Language> DATA = List.of(
            Language.builder().code("DE").name("German").build(),
            Language.builder().code("EN").name("English").build(),
            Language.builder().code("FR").name("French").build()
    );

    @Override
    public List<Language> findAll() {
        return new ArrayList<>(DATA);
    }

    @Override
    public Language getById(String id) {
        return DATA.stream().filter(l -> l.getCode().equalsIgnoreCase(id)).findFirst().orElseThrow();
    }

    public List<Language> findAllByIdIn(Collection<String> ids) {
        return DATA.stream().filter(e -> ids.contains(e.getCode())).collect(Collectors.toList());
    }
}
