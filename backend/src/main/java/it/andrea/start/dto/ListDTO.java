package it.andrea.start.dto;

import java.util.Collection;

public class ListDTO extends ListBaseDTO<ListItemDTO> {

    private static final long serialVersionUID = -2960092039348176201L;

    public ListDTO() {
	super();
    }

    public ListDTO(Collection<ListItemDTO> items) {
	super(items);
    }

    @Override
    public String toString() {
	return "ListDTO [items=" + getItems() + "]";
    }

}
