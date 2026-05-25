package com.demo.booking.booking.application.usecase;

import com.demo.booking.booking.application.command.CreateFlashSaleBookCommand;

public interface CreateFlashSaleBookingUseCase {

    Long createFlashSaleBooking(CreateFlashSaleBookCommand command);

}