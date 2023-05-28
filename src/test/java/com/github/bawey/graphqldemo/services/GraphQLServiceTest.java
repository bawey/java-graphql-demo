package com.github.bawey.graphqldemo.services;

import com.github.bawey.graphqldemo.config.GraphQLContext;
import com.github.bawey.graphqldemo.entities.Language;
import com.github.bawey.graphqldemo.entities.Lexeme;
import com.github.bawey.graphqldemo.repositories.LanguagesRepository;
import com.github.bawey.graphqldemo.repositories.LexemesRepository;
import com.github.bawey.graphqldemo.resources.GraphQLRequest;
import graphql.ExecutionResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = GraphQLServiceTest.IntegrationTestContext.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
class GraphQLServiceTest {
    @Import(GraphQLContext.class)
    public static class IntegrationTestContext {
        @Bean
        @Primary
        public LanguagesRepository languagesRepository() {
            return Mockito.mock(LanguagesRepository.class);
        }

        @Bean
        @Primary
        public LexemesRepository lexemesRepository() {
            return Mockito.mock(LexemesRepository.class);
        }
    }


    @Autowired
    GraphQLService service;

    @Autowired
    LexemesRepository lexemesRepo;

    @Autowired
    LanguagesRepository languagesRepo;

    @Test
    @SneakyThrows
    void testThatLanguagesRepoIsNotCalledExcessively() {
        //given
        ClassPathResource resource = new ClassPathResource("fetch_lexemes.graphql");
        String queryString = Files.readString(Path.of(resource.getURI()));
        GraphQLRequest request = GraphQLRequest.builder().query(queryString).build();

        List<Lexeme> dbLexemes = List.of(
                Lexeme.builder().id(new Lexeme.LexemeId("EN", "one")).build(),
                Lexeme.builder().id(new Lexeme.LexemeId("EN", "two")).build(),
                Lexeme.builder().id(new Lexeme.LexemeId("DE", "eins")).build(),
                Lexeme.builder().id(new Lexeme.LexemeId("DE", "zwei")).build(),
                Lexeme.builder().id(new Lexeme.LexemeId("DE", "drei")).build()
        );

        Function<String, Language> languageFactoryFunction = code ->
                Language.builder().name(code + " Lang").code(code).build();

        //when
        doReturn(dbLexemes)
                .when(lexemesRepo).findAll();

        doAnswer(invocationOnMock -> languageFactoryFunction.apply(invocationOnMock.getArgument(0)))
                .when(languagesRepo).getById(any());

        doAnswer(invocationOnMock -> {
            Set<String> codes = invocationOnMock.getArgument(0);
            return codes.stream().map(languageFactoryFunction).collect(Collectors.toList());
        }).when(languagesRepo).findAllByIdIn(any());

        ExecutionResult result = service.execute(request);
        //then
        log.info("Result be like {}", result);
        verify(languagesRepo, never()).getById("EN");
        verify(languagesRepo, never()).getById("DE");
        verify(languagesRepo, times(1)).findAllByIdIn(any());

        assertThat(result.getErrors()).isEmpty();
        Map<String, List<Map<String, String>>> data = result.getData();
        assertThat(data).isNotEmpty();
        assertThat(data).containsKey("lexemes");
        assertThat(data.get("lexemes")).hasSize(dbLexemes.size());
        assertThat(data.get("lexemes").stream().map(e -> e.get("headword")).sorted().collect(Collectors.toList()))
                .containsAll(dbLexemes.stream().map(Lexeme::getId).map(Lexeme.LexemeId::getHeadword).sorted()
                        .collect(Collectors.toList()));
    }
}