package it.andrea.start.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ListBaseDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5530464188458934192L;

    private Collection<T> items;

}
