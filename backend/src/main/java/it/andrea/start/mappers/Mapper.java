package it.andrea.start.mappers;

import java.util.Collection;

import it.andrea.start.error.exception.mapping.MappingToDtoException;
import it.andrea.start.error.exception.mapping.MappingToEntityException;

public interface Mapper<T, E> {

    T toDto(E paramE) throws MappingToDtoException;

    void toEntity(T paramT, E paramE) throws MappingToEntityException;

    Collection<T> toDtos(Collection<E> paramCollection) throws MappingToDtoException;

}
