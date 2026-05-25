package com.demo.booking.common.domain.model;

import java.util.List;
import java.util.Objects;

public class PageResult<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int number;
    private final int size;
    private final int numberOfElements;
    
    public PageResult(List<T> content, long totalElements, int number, int size) {
        this.content = List.copyOf(content != null ? content : List.of());
        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
        this.numberOfElements = this.content.size();
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
    }
    
    public List<T> getContent() {
        return content;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public int getNumber() {
        return number;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getNumberOfElements() {
        return numberOfElements;
    }
    
    public boolean hasContent() {
        return !content.isEmpty();
    }
    
    public boolean hasNext() {
        return number + 1 < totalPages;
    }
    
    public boolean hasPrevious() {
        return number > 0;
    }
    
    public boolean isFirst() {
        return number == 0;
    }
    
    public boolean isLast() {
        return number + 1 >= totalPages;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageResult<?> that = (PageResult<?>) o;
        return totalElements == that.totalElements && 
               totalPages == that.totalPages && 
               number == that.number && 
               size == that.size && 
               numberOfElements == that.numberOfElements && 
               Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements, totalPages, number, size, numberOfElements);
    }
    
    @Override
    public String toString() {
        return "PageResult{" +
                "content=" + content.size() + " items" +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", number=" + number +
                ", size=" + size +
                ", numberOfElements=" + numberOfElements +
                '}';
    }
}