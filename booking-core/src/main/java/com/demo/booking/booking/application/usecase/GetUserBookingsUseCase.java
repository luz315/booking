package com.demo.booking.booking.application.usecase;

import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;

import java.util.List;

public interface GetUserBookingsUseCase {
    List<Booking> getUserBookings(Long userId);
    PageResult<Booking> getUserBookings(Long userId, PageCriteria pageCriteria);
}