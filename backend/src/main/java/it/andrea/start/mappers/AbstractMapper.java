package it.andrea.start.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import it.andrea.start.error.exception.mapping.MappingToDtoException;
import jakarta.persistence.EntityManager;

public abstract class AbstractMapper<T, E> implements Mapper<T, E> {

    private EntityManager entityManager;

    protected AbstractMapper(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public final Collection<T> toDtos(final Collection<E> elements) throws MappingToDtoException {
        if (elements == null) {
            return Collections.emptyList();
        }

        Collection<T> dtos = new ArrayList<>(elements.size());
        for (E element : elements) {
            if (element != null) {
                dtos.add(toDto(element));
            }
        }
        return dtos;
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

}
