package com.github.bawey.graphqldemo.repositories;

import com.github.bawey.graphqldemo.entities.Lexeme;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LexemesRepository implements DummyRepository<Lexeme, Lexeme.LexemeId> {

    private static final List<Lexeme> DATA = List.of(
            Lexeme.builder().id(new Lexeme.LexemeId("EN", "cat")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("FR", "chat")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("DE", "Katze")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("EN", "dog")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("FR", "chien")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("DE", "Hund")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("EN", "chat")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("DE", "Mist")).build(),
            Lexeme.builder().id(new Lexeme.LexemeId("EN", "mist")).build()
    );

    @Override
    public List<Lexeme> findAll() {
        return new ArrayList<>(DATA);
    }

    @Override
    public Lexeme getById(Lexeme.LexemeId id) {
        return DATA.stream().filter(e -> e.getId().equals(id)).findFirst().orElseThrow();
    }
}
