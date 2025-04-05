package it.andrea.start.dto;

import java.io.Serializable;
import java.util.Collection;

public abstract class ListBaseDTO<T> implements Serializable {

    private static final long serialVersionUID = 5530464188458934192L;

    private Collection<T> items;

    public ListBaseDTO() {
	super();
    }

    public ListBaseDTO(Collection<T> items) {
	super();
	this.items = items;
    }

    public Collection<T> getItems() {
	return items;
    }

    public void setItems(Collection<T> items) {
	this.items = items;
    }

    @Override
    public String toString() {
	return "ListBaseDTO [items=" + items + "]";
    }

}
