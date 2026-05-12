package com.apiletsshopecom.payloads.response;

import java.util.List;

public class ViewCartResponse {

	private List<Product> products;
	private int count;
	private String message;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ViewCartResponse [products=" + products + ", count=" + count + ", message=" + message + "]";
	}

}
