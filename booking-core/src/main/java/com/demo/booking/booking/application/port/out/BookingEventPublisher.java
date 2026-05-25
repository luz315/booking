package com.demo.booking.booking.application.port.out;

import com.demo.booking.booking.domain.event.BookingEvent;

public interface BookingEventPublisher {
    
    /**
     * 예약 관련 이벤트를 발행합니다.
     * 
     * @param event 발행할 이벤트
     */
    void publish(BookingEvent event);
    
    /**
     * 여러 이벤트를 한 번에 발행합니다.
     * 
     * @param events 발행할 이벤트들
     */
    void publishAll(BookingEvent... events);
}