package it.andrea.start.utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PagedResult<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8138842272681687945L;

    private List<T> items;
    private Integer totalElements;
    private Integer pageNumber;
    private Integer pageSize;

    public PagedResult() {
    }

    public PagedResult(List<T> items, Integer totalElements, Integer pageNumber, Integer pageSize) {
        this.items = items;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
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
