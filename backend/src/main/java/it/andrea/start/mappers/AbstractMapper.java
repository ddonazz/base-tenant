package it.andrea.start.mappers;

import jakarta.persistence.EntityManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMapper<T, E> implements Mapper<T, E> {

    private final EntityManager entityManager;

    protected AbstractMapper(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public final List<T> toDtos(final Collection<E> elements) {
        if (elements == null || elements.isEmpty()) {
            return Collections.emptyList();
        }

        return elements
                .stream()
                .filter(Objects::nonNull)
                .map(this::toDto).toList();
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

}
