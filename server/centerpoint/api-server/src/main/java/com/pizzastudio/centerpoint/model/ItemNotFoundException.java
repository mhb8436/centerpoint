package com.pizzastudio.centerpoint.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Item doesn't exist")
public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3801138054894136097L;
}
