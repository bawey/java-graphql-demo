package com.github.bawey.graphqldemo.disposable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Brutal and straight to the point. IRL you might want to consider plugging some library instead.
 * @param <T>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GraphQLResponse<T> {
    private T data;
    private boolean dataPresent;
    private List<Object> errors;
    private List<Object> extensions;

    public boolean areErrorsPresent() {
        return Optional.ofNullable(errors).filter(Predicate.not(List::isEmpty)).isPresent();
    }
}
