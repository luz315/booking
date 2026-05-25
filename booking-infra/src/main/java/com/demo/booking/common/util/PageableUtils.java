package com.demo.booking.common.util;

import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.SortCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {
    
    private PageableUtils() {}
    
    public static Pageable createPageable(PageCriteria pageCriteria) {
        Sort sort = Sort.unsorted();
        
        if (pageCriteria.getSorts() != null && !pageCriteria.getSorts().isEmpty()) {
            sort = Sort.by(
                pageCriteria.getSorts().stream()
                        .map(sc -> sc.getDirection() == SortCriteria.Direction.ASC
                                ? Sort.Order.asc(sc.getProperty())
                                : Sort.Order.desc(sc.getProperty()))
                        .toList()
            );
        }
        
        return PageRequest.of(pageCriteria.getPage(), pageCriteria.getSize(), sort);
    }
}