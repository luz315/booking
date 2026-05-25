package com.demo.booking.common.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PageCriteria {
    private final int page;
    private final int size;
    private final List<SortCriteria> sorts;
    
    public PageCriteria() {
        this(0, 20, Collections.emptyList());
    }
    
    public PageCriteria(int page, int size) {
        this(page, size, Collections.emptyList());
    }
    
    public PageCriteria(int page, int size, List<SortCriteria> sorts) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지는 0 이상이어야 합니다");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("페이지 크기는 0보다 커야 합니다");
        }
        
        this.page = page;
        this.size = size;
        this.sorts = sorts != null ? List.copyOf(sorts) : Collections.emptyList();
    }
    
    public int getPage() {
        return page;
    }
    
    public int getSize() {
        return size;
    }
    
    public List<SortCriteria> getSorts() {
        return sorts;
    }
    
    public long getOffset() {
        return (long) page * size;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageCriteria that = (PageCriteria) o;
        return page == that.page && size == that.size && Objects.equals(sorts, that.sorts);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(page, size, sorts);
    }
    
    @Override
    public String toString() {
        return "PageCriteria{" +
                "page=" + page +
                ", size=" + size +
                ", sorts=" + sorts +
                '}';
    }
}