package com.bookstore.service;

import com.bookstore.domain.UserShipping;

public interface UserShippingService {

	UserShipping findById(Long shippingAddress);

	void removeById(Long userShippingId);
}
