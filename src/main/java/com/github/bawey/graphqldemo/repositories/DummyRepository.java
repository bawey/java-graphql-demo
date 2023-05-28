package com.github.bawey.graphqldemo.repositories;

import java.util.List;

public interface DummyRepository<EntityType, KeyType> {
    public List<EntityType> findAll();

    public EntityType getById(KeyType id);
}
