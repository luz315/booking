package com.demo.booking.common.domain.model;

import java.util.Objects;

public class SortCriteria {
    private final String property;
    private final Direction direction;
    
    public SortCriteria(String property) {
        this(property, Direction.DESC);
    }
    
    public SortCriteria(String property, Direction direction) {
        if (property == null || property.trim().isEmpty()) {
            throw new IllegalArgumentException("정렬 속성은 필수입니다");
        }
        this.property = property;
        this.direction = direction != null ? direction : Direction.DESC;
    }
    
    public String getProperty() {
        return property;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public enum Direction {
        ASC, DESC
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortCriteria that = (SortCriteria) o;
        return Objects.equals(property, that.property) && direction == that.direction;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(property, direction);
    }
    
    @Override
    public String toString() {
        return property + " " + direction.name();
    }
}