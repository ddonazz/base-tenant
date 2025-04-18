package it.andrea.start.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4669251700174320174L;

    private String id;
    private String description;

}
