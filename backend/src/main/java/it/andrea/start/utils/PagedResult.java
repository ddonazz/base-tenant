package it.andrea.start.utils;

import java.io.Serializable;
import java.util.Collection;

public class PagedResult<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = -8138842272681687945L;

    private Collection<T> items;
    private Integer totalElements;
    private Integer pageNumber;
    private Integer pageSize;

    public PagedResult() {}

    public PagedResult(Collection<T> items, Integer totalElements, Integer pageNumber, Integer pageSize) {
	this.items = items;
	this.totalElements = totalElements;
	this.pageNumber = pageNumber;
	this.pageSize = pageSize;
    }

    public Collection<T> getItems() {
	return items;
    }

    public void setItems(Collection<T> items) {
	this.items = items;
    }

    public Integer getTotalElements() {
	return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
	this.totalElements = totalElements;
    }

    public Integer getPageNumber() {
	return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
	this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
	return pageSize;
    }

    public void setPageSize(Integer pageSize) {
	this.pageSize = pageSize;
    }

}
