package it.andrea.start.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

@ToString
public class ListDTO extends ListBaseDTO<ListItemDTO> {

    @Serial
    private static final long serialVersionUID = -2960092039348176201L;

}