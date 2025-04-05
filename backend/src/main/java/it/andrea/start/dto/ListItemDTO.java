package it.andrea.start.dto;

import java.io.Serializable;

public class ListItemDTO implements Serializable {

    private static final long serialVersionUID = 4669251700174320174L;

    private String id;
    private String description;

    public ListItemDTO() {
	super();
    }

    public ListItemDTO(String id, String description) {
	super();
	this.id = id;
	this.description = description;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return "ListItemDTO [id=" + id + ", description=" + description + "]";
    }

}
