package com.github.bawey.graphqldemo.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class Language {
    private final String code;
    private final String name;
}
