package it.andrea.start.mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import it.andrea.start.exception.MappingToDtoException;
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
        return elements.stream()
                       .filter(Objects::nonNull)
                       .map(element -> {
                           try {
                               return toDto(element);
                           } catch (MappingToDtoException e) {
                               throw new RuntimeException("Errore durante il mapping dell'entità", e);
                           }
                       })
                       .toList();
    }

    protected EntityManager getEntityManager() {
	return this.entityManager;
    }

}
