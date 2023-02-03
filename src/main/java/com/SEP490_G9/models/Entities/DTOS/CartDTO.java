package com.SEP490_G9.models.Entities.DTOS;

import java.util.List;

import com.SEP490_G9.models.Entities.CartItem;
import com.SEP490_G9.models.Entities.User;

public class CartDTO {
	private Long id;
	private User user;
	private List<CartItem> items;
	private int totalPrice;
}
