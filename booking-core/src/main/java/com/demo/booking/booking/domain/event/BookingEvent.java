package com.demo.booking.booking.domain.event;

import java.time.LocalDateTime;

public abstract class BookingEvent {
    private final String eventId;
    private final String bookingId;
    private final LocalDateTime occurredAt;
    
    protected BookingEvent(String eventId, String bookingId) {
        this.eventId = eventId;
        this.bookingId = bookingId;
        this.occurredAt = LocalDateTime.now();
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public String getBookingId() {
        return bookingId;
    }
    
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}